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

package org.genedb.web.mvc.controller;

import org.genedb.db.dao.DaoFactory;
import org.genedb.db.dao.FeatureDao;
import org.genedb.db.dao.NameLookup;
import org.genedb.db.hibernate3gen.FeatureRelationship;
import org.genedb.db.jpa.Feature;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;



/**
 * Looks up a feature by uniquename, and possibly synonyms
 * 
 * @author Chinmay Patel (cp2)
 * @author Adrian Tivey (art)
 */
public class NamedFeatureController extends SimpleFormController {

    private DaoFactory daoFactory;
    private String listResultsView;

    @Override
    protected boolean isFormSubmission(HttpServletRequest request) {
        return true;
    }

    @Override
    protected ModelAndView onSubmit(Object command) throws Exception {
        NameLookup nl = (NameLookup) command;
        FeatureDao featureDao = this.daoFactory.getFeatureDao();
        List<Feature> results = featureDao.findByAnyName(nl);
        if (results == null || results.size() == 0) {
            
            // TODO Fail page
        }
        
        Map<String, Object> model = new HashMap<String, Object>(3);
        
        String viewName = null;
        
        if (results.size() > 1) {
            // Go to list results page
            viewName = listResultsView;
            model.put("results", results);
        } else {
            Feature feature = results.get(0);
            model.put("feature", feature);
            String type = feature.getCvTerm().getName();
            if (type != null && type.equals("gene")) {
                viewName = "features/gene";
                Feature mRNA = null;
                Set<FeatureRelationship> frs = feature.getFeatureRelationshipsForObjectId(); 
                for (FeatureRelationship fr : frs) {
                    mRNA = fr.getFeatureBySubjectId();
                    break;
                }
                Feature polypeptide = null;
                Set<FeatureRelationship> frs2 = mRNA.getFeatureRelationshipsForObjectId(); 
                for (FeatureRelationship fr : frs2) {
                    Feature f = fr.getFeatureBySubjectId();
                    if ("polypeptide".equals(f.getCvTerm().getName())) {
                        polypeptide = f;
                    }
                }
                model.put("polypeptide", polypeptide);
            }

        }

        return new ModelAndView(viewName, model);
    }

    public void setDaoFactory(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    public void setListResultsView(String listResultsView) {
        this.listResultsView = listResultsView;
    }
    
    
    
    

}
