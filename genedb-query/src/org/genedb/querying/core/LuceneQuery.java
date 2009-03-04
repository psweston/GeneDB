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
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Hit;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.BooleanClause.Occur;
import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

@Configurable
public abstract class LuceneQuery implements Query {

    private static final Logger logger = Logger.getLogger(LuceneQuery.class);

    private int order;

    @Autowired
    private LuceneIndexFactory luceneIndexFactory;

    private LuceneIndex luceneIndex;

    protected String name;


    @PostConstruct
    protected void afterPropertiesSet() {
        luceneIndex = luceneIndexFactory.getIndex(getluceneIndexName());
    }


    protected abstract String getluceneIndexName();

    protected static final TermQuery isCurrentQuery = new TermQuery(new Term("obsolete", "false"));
    protected static final TermQuery geneQuery = new TermQuery(new Term("type.name","gene"));
    protected static final TermQuery pseudogeneQuery = new TermQuery(new Term("type.name","pseudogene"));
    protected static final BooleanQuery geneOrPseudogeneQuery = new BooleanQuery();
    static {
        geneOrPseudogeneQuery.add(geneQuery, Occur.SHOULD);
        geneOrPseudogeneQuery.add(pseudogeneQuery, Occur.SHOULD);
    }

    //private List<CachedParamDetails> cachedParamDetailsList = new ArrayList<CachedParamDetails>();
    //private Map<String, CachedParamDetails> cachedParamDetailsMap = new HashMap<String, CachedParamDetails>();

    public String getParseableDescription() {
        return QueryUtils.makeParseableDescription(name, getParamNames(), this);
    }

    public List getResults() throws QueryException {
        List names;
        try {
            Hits hits = lookupInLucene();
            names = new ArrayList();

            @SuppressWarnings("unchecked")
            Iterator<Hit> it = hits.iterator();
            while (it.hasNext()) {
                Hit hit = it.next();
                Document document = hit.getDocument();
                logger.debug(StringUtils.collectionToCommaDelimitedString(document.getFields()));
                Object o = convertDocumentToReturnType(document);
                names.add(o);
            }
            Collections.sort(names);
            return names;
        } catch (CorruptIndexException exp) {
            throw new QueryException(exp);
        } catch (IOException exp) {
            throw new QueryException(exp);
        }
    }

    protected abstract Object convertDocumentToReturnType(Document document);


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


    private Hits lookupInLucene(List<org.apache.lucene.search.Query> queries) {

        BooleanQuery booleanQuery = new BooleanQuery();
        for (org.apache.lucene.search.Query query : queries) {
            booleanQuery.add(new BooleanClause(query, Occur.MUST));
        }

        logger.error(String.format("Lucene query is '%s'", booleanQuery.toString()));
        Hits hits = luceneIndex.search(booleanQuery);
        return hits;
    }


    private Hits lookupInLucene() {

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

    @Override
    public void validate(Object target, Errors errors) {
        //@SuppressWarnings("unchecked") T query = (T) target;
        ClassValidator queryValidator = new ClassValidator(this.getClass());
        InvalidValue[] invalids = queryValidator.getInvalidValues(target);
        for (InvalidValue invalidValue: invalids){
            errors.rejectValue(invalidValue.getPropertyPath(), null, invalidValue.getMessage());
        }

        extraValidation(errors);
    }

    protected abstract void extraValidation(Errors errors);

    @Override
    @SuppressWarnings("unchecked")
    public boolean supports(Class clazz) {
        return this.getClass().isAssignableFrom(clazz);
    }

}
