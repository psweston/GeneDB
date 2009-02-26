package org.genedb.querying.tmpquery;

import org.genedb.querying.core.QueryClass;
import org.genedb.querying.core.QueryParam;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.RangeQuery;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

@QueryClass(
        title="Coding and pseudogenes by protein length",
        shortDesc="Get a list of transcripts ",
        longDesc=""
    )
public class ProteinNumTMQuery extends OrganismLuceneQuery {

    @QueryParam(
            order=1,
            title="Minimum length of protein in bases"
    )
    private int min = 1;

    @QueryParam(
            order=2,
            title="Minimum length of protein in bases"
    )
    private int max = 10;


    @Override
    protected String getluceneIndexName() {
        return "org.gmod.schema.mapped.Feature";
    }

    @Override
    protected void getQueryTerms(List<org.apache.lucene.search.Query> queries) {

        Term lowerTerm = new Term("numberTMDomains", Integer.toString(min));
        Term upperTerm = new Term("numberTMDomains", Integer.toString(max));
        RangeQuery rq = new RangeQuery(lowerTerm, upperTerm, true);

        queries.add(rq);
        //queries.add(geneOrPseudogeneQuery);

        makeQueryForOrganisms(taxons, queries);
    }

    // ------ Autogenerated code below here



    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    @Override
    protected String[] getParamNames() {
        return new String[] {"min", "max"};
    }

 
            @Override
            @SuppressWarnings("unused")
            public void validate(Object target, Errors errors) {
                return;
            }

            @Override
            @SuppressWarnings("unchecked")
            public boolean supports(Class clazz) {
                return ProteinNumTMQuery.class.isAssignableFrom(clazz);
            }

}
