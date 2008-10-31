/*
 * Copyright (c) 2006 Genome Research Limited.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Library General Public License as published
 * by  the Free Software Foundation; either version 2 of the License or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public License
 * along with this program; see the file COPYING.LIB.  If not, write to
 * the Free Software Foundation Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307 USA
 */

package org.genedb.web.mvc.controller.download;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.spring.web.servlet.view.JsonView;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.genedb.db.dao.SequenceDao;
import org.genedb.querying.core.LuceneIndex;
import org.genedb.querying.core.LuceneIndexFactory;
import org.genedb.querying.history.HistoryItem;
import org.genedb.querying.history.HistoryManager;
import org.genedb.web.mvc.controller.HistoryManagerFactory;
import org.genedb.web.mvc.controller.PostOrGetFormController;
import org.genedb.web.mvc.controller.ResultHit;
import org.genedb.web.utils.DownloadUtils;

import org.gmod.schema.feature.AbstractGene;
import org.gmod.schema.mapped.Feature;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;



/**
 * List Download
 *
 * @author Chinmay Patel (cp2)
 * @author Adrian Tivey (art)
 */
public class DownloadFeaturesController extends PostOrGetFormController {

    private SequenceDao sequenceDao;
    private LuceneIndexFactory luceneIndexFactory;
    private HistoryManagerFactory historyManagerFactory;
    private String downloadView;
    private JsonView jsonView;

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request,
            HttpServletResponse response, Object command,
            BindException be) throws Exception {

        DownloadBean db = (DownloadBean) command;
        if(!db.isJson()) {
            return new ModelAndView(downloadView);
        }

        int historyItem = db.getHistoryItem()-1;

        HistoryManager historyManager = historyManagerFactory.getHistoryManager(request.getSession());
        List<HistoryItem> historyItems = historyManager.getHistoryItems();
        if(historyItem >= historyItems.size()) {
            response.sendError(511);
            return null;
        }

        HistoryItem hItem = historyItems.get(historyItem);
        List<String> ids = hItem.getIds();
        Hits hits = lookupInLucene(ids);

        OutputFormat format = db.getOutputFormat();
        SequenceType sequenceType = db.getSequenceType();

        String file = request.getSession().getId();
        String columns[] = request.getParameter("columns").split(",");

        File output = null;

        switch (format) {
            case EXCEL:
                    OutputStream outStream = response.getOutputStream();
                    response.setContentType("application/vnd.ms-excel");
                    response.setHeader("Content-Disposition", "attachment");
                    response.setHeader("filename", output.getName());
                    createExcel(hits,file,columns, outStream);
                    return null;
            case CSV:
            case TAB:
                    String outString = createCsv(hits,file,columns,format);
                    response.setContentType("application/x-download");
                    response.setHeader("Content-Disposition", "attachment");
                    response.setHeader("filename", "results.txt");
                    Writer w = response.getWriter();
                    w.write(outString);
                    return null;
            case HTML:
                    Map<String,Object> model = new HashMap<String,Object>();
                    List<ResultHit> jHits = new ArrayList<ResultHit>();
                    for(int i=0;i<hits.length();i++) {
                        Document doc = hits.doc(i);
                        ResultHit rh = new ResultHit();
                        for (String column : columns) {
                            if(column.equals("chr")) {
                                rh.setChromosome(getValue(column, doc));
                            } else if(column.equals("locs")) {
                                rh.setLocation(getValue(column, doc));
                            } else if(column.equals("uniqueName")) {
                                rh.setName(getValue(column, doc));
                            } else if(column.equals("product")) {
                                rh.setProduct(getValue(column, doc));
                            } else if(column.equals("synonym")) {
                                rh.setSynonym(getValue(column, doc));
                            } else if(column.equals("organism.commonName")) {
                                rh.setOrganism(getValue(column, doc));
                            }
                        }
                        jHits.add(rh);
                    }

                    model.put("hits",jHits );
                    model.put("hitsLength", hits.length());
                    return new ModelAndView(jsonView,model);
            case FASTA:
                    output = createFasta(hits,file,columns,sequenceType);
                    response.setContentType("application/x-download");
                    response.setHeader("Content-Disposition", "attachment");
                    response.setHeader("filename", output.getName());
            }
        return null;
    }

    private Hits lookupInLucene(List<String> ids) throws IOException {
        logger.error(String.format("#~# luceneIndexFactory is '%s'", luceneIndexFactory));
        LuceneIndex luceneIndex = luceneIndexFactory.getIndex("org.gmod.schema.mapped.Feature");
        BooleanQuery bQuery = new BooleanQuery();
        for (String id : ids) {
            bQuery.add(new TermQuery(new Term("uniqueName",id)), Occur.SHOULD);
        }
        logger.error(String.format("#~# luceneIndex is '%s'", luceneIndex));
        Hits hits = luceneIndex.search(bQuery);
        return hits;
    }


    private File createFasta(Hits hits,String file,String[] columns, SequenceType sequenceType) {

        StringBuffer whole = new StringBuffer();
        StringBuffer row = new StringBuffer();

        //add data
        for(int i=0;i<hits.length();i++) {
            row = new StringBuffer();
            Document doc = null;
            try {
                doc = hits.doc(i);
            } catch (CorruptIndexException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (String column: columns) {
                if(!column.equals("sequence")) {
                    row.append(getValue(column,doc) + ";");
                }
            }
            row.deleteCharAt(row.length()-1);

            AbstractGene gene =  (AbstractGene)sequenceDao.getFeatureByUniqueName(doc.get("uniqueName"), Feature.class);
            logger.info(String.format(" %d Gene %s Type %s",i,doc.get("uniqueName"),gene.getType().getName() ));
            String sequence = DownloadUtils.getSequence(gene, sequenceType);
            String entry;
            if(sequence != null) {
                entry = DownloadUtils.writeFasta(row.toString(), sequence);
            } else {
                entry = String.format("%s \n Alternately spliced or sequence not attached ", row.toString());
            }

            whole.append(entry);
            whole.append("\n\n");
        }



        BufferedWriter out = null;
        File outFile = new File(getServletContext().getRealPath("/" + file));
        try {
            out = new BufferedWriter(new FileWriter(outFile));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

        try {
            out.write(whole.toString());
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return outFile;
    }

    private void createExcel(Hits hits,String file,String[] columns, OutputStream out) throws IOException {
        int rcount = 2;
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();
        HSSFRow heading = sheet.createRow(rcount++);
        short count = 1;

        //add headers
        for (String column: columns) {
            HSSFCell cell = heading.createCell(count);
            HSSFRichTextString value = null;
            if(column.equals("organism.commonName")) {
                value = new HSSFRichTextString("Organism");
            } else if (column.equals("uniqueName")) {
                value = new HSSFRichTextString("Systematic Id");
            } else if (column.equals("synonym")) {
                value = new HSSFRichTextString("Synonyms");
            } else if (column.equals("locs")) {
                value = new HSSFRichTextString("Location");
            } else if (column.equals("chr")) {
                value = new HSSFRichTextString("Chromosome");
            } else if (column.equals("product")) {
                value = new HSSFRichTextString("Product");
            }

            cell.setCellValue(value);
            count++;
        }
        rcount++;

        //add data
        for(int i=0;i<hits.length();i++) {
            Document doc = null;
            try {
                doc = hits.doc(i);
            } catch (CorruptIndexException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            HSSFRow row = sheet.createRow(rcount++);
            count = 1;
            for (String column: columns) {
                HSSFCell cell = row.createCell(count);
                HSSFRichTextString value = new HSSFRichTextString(getValue(column,doc));
                cell.setCellValue(value);
                count++;
            }
        }

//        File dir = new File(getServletContext().getRealPath("/"));
//        File f = File.createTempFile(file, ".xls",dir);
//
//        FileOutputStream stream=null;
//        try {
//            stream = new FileOutputStream(f);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
        try {
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //return f;
    }

    private String getValue(String column, Document doc) {
        if(column.equals("locs")) {
            int start = Integer.parseInt(doc.get("start"));
            int stop = Integer.parseInt(doc.get("stop"));
            String strand = doc.get("strand");

            String locs = String.format("%d-%d", start,stop);
            if(strand.equals("-1")) {
                locs = String.format("(%d-%d)", start,stop);
            }
            return locs;
        }
        return doc.get(column);
    }


    private String createCsv(Hits hits,String file,String[] columns,OutputFormat format) {
        String separator = ",";
        StringBuffer whole = new StringBuffer();
        StringBuffer row = new StringBuffer();

        if(format == OutputFormat.TAB) {
            separator = "\t";
        }

        //add headers
        for (String column: columns) {

            if(column.equals("organism.commonName")) {
                row.append("Organism");
            } else if (column.equals("uniqueName")) {
                row.append("Systematic Id");
            } else if (column.equals("synonym")) {
                row.append("Synonyms");
            } else if (column.equals("locs")) {
                row.append("Location");
            } else if (column.equals("chr")) {
                row.append("Chromosome");
            } else if (column.equals("product")) {
                row.append("Product");
            }
            row.append(separator);
        }
        row.deleteCharAt(row.length()-1);
        whole.append(row);
        whole.append("\n");
        //add data
        for(int i=0;i<hits.length();i++) {
            row = new StringBuffer();
            Document doc = null;
            try {
                doc = hits.doc(i);
            } catch (CorruptIndexException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (String column: columns) {
                row.append(getValue(column,doc));
                row.append(separator);
            }
            row.deleteCharAt(row.length()-1);
            whole.append(row);
            whole.append("\n");
        }

//        BufferedWriter out = null;
//        File outFile = new File(getServletContext().getRealPath("/" + file));
//        try {
//            out = new BufferedWriter(new FileWriter(outFile));
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (IllegalStateException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            out.write(whole.toString());
//            out.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return whole.toString();
    }

    public void setSequenceDao(SequenceDao sequenceDao) {
        this.sequenceDao = sequenceDao;
    }

    public void setHistoryManagerFactory(HistoryManagerFactory historyManagerFactory) {
        this.historyManagerFactory = historyManagerFactory;
    }

    public String getDownloadView() {
        return downloadView;
    }

    public void setDownloadView(String downloadView) {
        this.downloadView = downloadView;
    }

//    public LuceneDao getLuceneDao() {
//        return luceneDao;
//    }

    public void setLuceneIndexFactory(LuceneIndexFactory luceneIndexFactory) {
        this.luceneIndexFactory = luceneIndexFactory;
    }

    public JsonView getJsonView() {
        return jsonView;
    }


    public void setJsonView(JsonView jsonView) {
        this.jsonView = jsonView;
    }

}

