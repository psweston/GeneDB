package org.genedb.querying.tmpquery;

import org.genedb.querying.core.HqlQuery;
import org.genedb.querying.core.QueryClass;
import org.genedb.querying.core.QueryParam;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@QueryClass(
        title="Coding and pseudogenes by protein mass",
        shortDesc="Get a list of transcripts ",
        longDesc=""
    )
public class ProteinMassQuery extends OrganismHqlQuery {

    @QueryParam(
            order=1,
            title="Minimum mass of protein"
    )
    private int min = 10000;

    @QueryParam(
            order=2,
            title="Maximum mass of protein"
    )
    private int max = 50000;

    @Override
    protected String getHql() {
        return "select f.uniqueName, f.organism.abbreviation from Feature f where f.type.name='polypeptide' and f.seqLen >= :min and f.seqLen <= :max @ORGANISM@ order by f.organism, f.uniqueName";
    }
    

    // ------ Autogenerated code below here

    public void setMin(int min) {
        this.min = min;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    @Override
    protected String[] getParamNames() {
        return new String[] {"min", "max"};
    }

    @Override
    protected void populateQueryWithParams(org.hibernate.Query query) {
    	super.populateQueryWithParams(query);
        query.setInteger("min", min);
        query.setInteger("max", max);
    }


            @Override
            @SuppressWarnings("unused")
            public void validate(Object target, Errors errors) {
                return;
            }

            @Override
            @SuppressWarnings("unchecked")
            public boolean supports(Class clazz) {
                return ProteinMassQuery.class.isAssignableFrom(clazz);
            }




}
