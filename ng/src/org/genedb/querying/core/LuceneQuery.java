/*
 * Copyright (c) 2007 Genome Research Limited.
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

package org.genedb.querying.core;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.BooleanClause.Occur;
import org.genedb.querying.tmpquery.GeneSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

@Configurable
public abstract class LuceneQuery implements PagedQuery {

    private static transient final Logger logger = Logger.getLogger(LuceneQuery.class);

    private int order;

    @Autowired
    public transient LuceneIndexFactory luceneIndexFactory;

    protected transient LuceneIndex luceneIndex;
    
    /**
     * Size of result retrieved
     */
    protected boolean isActualResultSizeSameAsMax;


    @PostConstruct
    public void afterPropertiesSet() {
        luceneIndex = luceneIndexFactory.getIndex(getluceneIndexName());
    }


    protected abstract String getluceneIndexName();

    //private List<CachedParamDetails> cachedParamDetailsList = new ArrayList<CachedParamDetails>();
    //private Map<String, CachedParamDetails> cachedParamDetailsMap = new HashMap<String, CachedParamDetails>();

    public String getParseableDescription() {
        return QueryUtils.makeParseableDescription(getQueryName(), getParamNames(), this);
    }
    
    public abstract class Pager <T> {
    	abstract public T convert(Document doc);
    	
    	public List<T> getResults(int start, int end) throws QueryException {
        	
        	List<T> res = new ArrayList<T>();
            try {
            	
            	logger.info(getQueryName() + " pager looking up in lucene");
                TopDocs topDocs = lookupInLucene();
                ScoreDoc[] scoreDocs = topDocs.scoreDocs;
                
                // while we have a handle on the scoredocs, let's store the total size
                totalResultSize= scoreDocs.length;
                
//                int start = page * length;
//                int end = start + length;
                
                int max = scoreDocs.length - 1;
                
                if (max < 0) {
                	return res;
                }
                
                if (end > max) {
                	end = max;
                }
                
                logger.info(getQueryName() + " pager paging " + start + "-" +end + " of " + scoreDocs.length);
                
                for (int i = start; i <= end; i++) {
                	ScoreDoc scoreDoc = scoreDocs[i];
                	Document document = fetchDocument(scoreDoc.doc);
                	res.add(convert(document));
                }
                
                if(luceneIndex.getMaxResults() == res.size()){
                    isActualResultSizeSameAsMax = true;
                }
                
                //logger.info(getQueryName() + " pager paged results for page " + page + ", length " +length + ", size : " + res.size());
                return res;
            } catch (CorruptIndexException exp) {
                throw new QueryException(exp);
            } catch (IOException exp) {
                throw new QueryException(exp);
            }
        }
        
    }
    
    
    public String getGeneUniqueNameOrUniqueName(Document document) {
    	
    	String documentUniqueName = document.get("uniqueName");
    	String geneName = document.get("gene");
    	
    	//logger.info("uniqueName: " + documentUniqueName + " geneName:" + geneName);
    	
//    	for (Field field : (List<Field>) document.getFields()) {
//    		logger.info(field.name() + "\t" + field.isTokenized() + "\t"
//					+ field.stringValue());
//    	}
    	
    	//logger.debug(StringUtils.collectionToCommaDelimitedString(document.getFields()));
    	
    	if (geneName == null) {
    		return documentUniqueName;
    	}
    	
    	String alternateTranscriptNumberString = document.get("alternateTranscriptNumber");
    	//logger.warn("alternative transcripts for " + geneName + " (" + documentUniqueName + ") : " + alternateTranscriptNumberString );
    	if (alternateTranscriptNumberString != null && alternateTranscriptNumberString.length() > 0) {
    		int alternateTranscriptNumber = Integer.parseInt(alternateTranscriptNumberString);
        	
        	if (alternateTranscriptNumber > 1) {
        		return documentUniqueName;
        	}
    	}
    	
    	return geneName;
    	
    }
    
	
	protected Pager<String> uniqueNamePager = new Pager<String>() {
		@Override public String convert(Document doc) {
			return doc.get("uniqueName");
		}
	};
    

    
    @Override
    public List<String> getResults(int page, int length) throws QueryException {
    	return uniqueNamePager.getResults(page, length);
    }
    
    private int totalResultSize = -1;
    
    @Override
	public int getTotalResultsSize() {
    	// this might have already been calculated by a pager
    	// no need to count them more than once (query parameters should not change)
        // logger.info( this + " totalResultSize " + totalResultSize);
    	if (totalResultSize == -1) {
    	    logger.info( this + " looking up");
    		TopDocs topDocs = lookupInLucene();
    		totalResultSize = topDocs.totalHits;
    	}
    	// logger.info( this + " totalResultSize " + totalResultSize);
        return totalResultSize;
	}
	
    
//    public <T extends Comparable<? super T>> List<T> getResults(Class<T> clazz) throws QueryException {
//        List<T> names = new ArrayList<T>();
//        try {
//            TopDocs topDocs = lookupInLucene();
//
//            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
//                Document document = fetchDocument(scoreDoc.doc);
//                //logger.trace(StringUtils.collectionToCommaDelimitedString(document.getFields()));
//                //T t = convertDocumentToReturnType(document, clazz);
//                Object t = convertDocumentToReturnType(document);
//                names.add((T)t);
//            }
//            Collections.sort(names);
//            return names;
//        } catch (CorruptIndexException exp) {
//            throw new QueryException(exp);
//        } catch (IOException exp) {
//            throw new QueryException(exp);
//        }
//    }

    @SuppressWarnings("unchecked")
    public List<String> getResults() throws QueryException {
        List<String> names = new ArrayList();
        try {
            TopDocs topDocs = lookupInLucene();

            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                Document document = fetchDocument(scoreDoc.doc);
//                doc.get("uniqueName");
//                //logger.trace(StringUtils.collectionToCommaDelimitedString(document.getFields()));
//                //T t = convertDocumentToReturnType(document, clazz);
//                Object t = convertDocumentToReturnType(document);
                
                //String name = getGeneUniqueNameOrUniqueName(document);
                names.add(document.get("uniqueName"));
                
            }
            Collections.sort(names);

            if(luceneIndex.getMaxResults() == names.size()){
                isActualResultSizeSameAsMax = true;
            }

            return names;
        } catch (CorruptIndexException exp) {
            throw new QueryException(exp);
        } catch (IOException exp) {
            throw new QueryException(exp);
        }
    }

    @Override
    public boolean isMaxResultsReached() {
        return isActualResultSizeSameAsMax;
    }


    //    protected abstract <T> T convertDocumentToReturnType(Document document, Class<T> clazz);
    //protected abstract Object convertDocumentToReturnType(Document document);

    protected abstract String[] getParamNames();

    public List<HtmlFormDetails> getFormDetails() {
        List<HtmlFormDetails> ret = new ArrayList<HtmlFormDetails>();

        /*
        for (String name : getParamNames()) {
            HtmlFormDetails htd = new HtmlFormDetails();
            //htd.setName(name);
            //htd.setDefaultValue
        }
         */

        return ret;
    }

    public Map<String, Object> prepareModelData() {
    	return Collections.emptyMap();
    }


    private TopDocs lookupInLucene(List<org.apache.lucene.search.Query> queries) {
    	
        TopDocs hits = null;
        if (queries.size() > 1) {
            BooleanQuery booleanQuery = new BooleanQuery();
            for (org.apache.lucene.search.Query query : queries) {
                booleanQuery.add(new BooleanClause(query, Occur.MUST));
            }
            
            logger.debug(String.format("Lucene query is '%s'", booleanQuery.toString()));
            hits = luceneIndex.search(booleanQuery);
            logger.debug(String.format("Results size is '%d'", hits.totalHits));
        } else {
        	logger.debug(String.format("Lucene query is '%s'", queries.get(0).toString()));
            hits = luceneIndex.search(queries.get(0));
            logger.debug(String.format("Results size is '%d'", hits.totalHits));
        }
        return hits;
    }
    
    private TopDocs lookupInLucene(List<org.apache.lucene.search.Query> queries, int maxResults) {
    	
        TopDocs hits = null;
        if (queries.size() > 1) {
            BooleanQuery booleanQuery = new BooleanQuery();
            for (org.apache.lucene.search.Query query : queries) {
                booleanQuery.add(new BooleanClause(query, Occur.MUST));
            }
            
            logger.debug(String.format("Lucene query is '%s'", booleanQuery.toString()));
            hits = luceneIndex.search(booleanQuery, maxResults);
            logger.debug(String.format("Results size is '%d'", hits.totalHits));
        } else {
        	logger.debug(String.format("Lucene query is '%s'", queries.get(0).toString()));
            hits = luceneIndex.search(queries.get(0), maxResults);
            logger.debug(String.format("Results size is '%d'", hits.totalHits));
        }
        return hits;
    }


    protected Document fetchDocument(int docId) throws CorruptIndexException, IOException {
        return luceneIndex.getDocument(docId);
    }
    
    protected TopDocs lookupInLucene(int maxResults) {
    	List<org.apache.lucene.search.Query> queries = new ArrayList<org.apache.lucene.search.Query>();
        getQueryTerms(queries);
        
        return lookupInLucene(queries, maxResults);
    }
    
    protected TopDocs lookupInLucene() {

        List<org.apache.lucene.search.Query> queries = new ArrayList<org.apache.lucene.search.Query>();
        getQueryTerms(queries);

        return lookupInLucene(queries);
    }

    protected abstract void getQueryTerms(List<org.apache.lucene.search.Query> queries);



    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getQueryDescription() {
        return "";
    }

    //@Override
    public void validate(Object target, Errors errors) {
        //@SuppressWarnings("unchecked") T query = (T) target;
//        ClassValidator queryValidator = new ClassValidator(this.getClass());
//        InvalidValue[] invalids = queryValidator.getInvalidValues(target);
//        for (InvalidValue invalidValue: invalids){
//            errors.rejectValue(invalidValue.getPropertyPath(), null, invalidValue.getMessage());
//        }
//
//        extraValidation(errors);
    }

    protected abstract void extraValidation(Errors errors);

    //@Override
    @SuppressWarnings("unchecked")
    public boolean supports(Class clazz) {
        return this.getClass().isAssignableFrom(clazz);
    }
    
    

}
