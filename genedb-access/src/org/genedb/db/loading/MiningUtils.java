/*
 * Copyright (c) 2002 Genome Research Limited.
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

/**
 *
 *
 * @author <a href="mailto:art@sanger.ac.uk">Adrian Tivey</a>
 */
package org.genedb.db.loading;

import org.biojava.bio.Annotation;
import org.biojava.bio.SmallAnnotation;
import org.biojava.bio.seq.Feature;
import org.biojava.utils.ChangeVetoException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

public class MiningUtils {

    public static boolean balancedBrackets(String string, char open, char close){
	int depth = 0;

	for (int i = 0; i < string.length(); i++){
	    if (string.charAt(i) == open) {
		depth++;
	    }
	    if (string.charAt(i) == close) {
		depth--;
	    }
	    if (depth < 0 ) {
		return false;
	    }
	}

	if (depth != 0 ){
	    return false;
	}
	return true;
    }

    @SuppressWarnings("unchecked")
    public static String getProperty(String key, Annotation an, String id) {
	if (!an.containsProperty(key)) {
	    return null;
	}

	Object o = an.getProperty(key);
	if ( o == null) {
	    return null;
	}

	if ( o instanceof Boolean ) {
	    return o.toString();
	}
	
	if ( o instanceof List ) {
	    Set<Object> s = new HashSet<Object>();
	    s.addAll((List<Object>) o);
	    if (s.size()>1) {
		if ( id != null) {
		    System.err.println("WARN: Returning first value of List for key " + key +" in "+id+" but there are "+s);
		} else {
		    System.err.println("WARN: Returning first value of List for key " + key+" but there are "+s);
		}
	    } else {
		if ( id != null) {
		    System.err.println("WARN: Returning first (identical) value of List for key " + key +" in "+id);
		} else {
		    System.err.println("WARN: Returning first (identical) value of List for key " + key);
		}
	    }

	    return ((ArrayList) o).get(0).toString();
	}
	return (String) o;
    }


    @SuppressWarnings("unchecked")
    public static List<String> getProperties(String key, Annotation an) {
	if ( !an.containsProperty(key) ) {
	    return null;
	}
	Object o = an.getProperty(key);

	if ( o instanceof List ) {
	    return (List<String>) o;
	}

	if ( o instanceof String ) {
	    List<String> tmp = new ArrayList<String>();
	    tmp.add((String)o);
	    return tmp;
	}
	return null;
    }


    public static void setProperty(Annotation an, String key, String value) {
	List<String> current = getProperties(key, an);
	if (current == null || current.size() == 0) {
	    try {
		an.setProperty(key, value);
	    } catch (IllegalArgumentException e) {
		// Deliberately empty
	    } catch (ChangeVetoException e) {
		// Deliberately empty
	    }
	} else {
	    current.add(value);
	}
    }


    public static void setProperty(Annotation an, String key, List<String> values) {
	List<String> current =getProperties(key, an);
	if (current == null || current.size() == 0) {
	    try {
		an.setProperty(key, values);
	    } catch (IllegalArgumentException e) {
		// Deliberately empty
	    } catch (ChangeVetoException e) {
		// Deliberately empty
	    }
	} else {
	    current.addAll(values);
	}
    }

    /**
     * Utility method to check what annotation a feature has
     * 
     * 
     * @param f The feature to check
     * @param requiredSingle Array of keys that MUST appear, with only a single value
     * @param requiredMultiple Array of keys that MUST appear, with multiple possible values
     * @param optionalSingle Array of keys that MAY appear, with only a single value
     * @param optionalMultiple Array of keys that MAY appear, with multiple possible values
     * @param fatal Should a problem stop the VM
     * @param reportExtra Report any keys not in above lists?
     * @return true if all conditions met, and no keys left over
     */
    public static boolean sanityCheckAnnotation(Feature f,
	    String[] requiredSingle, String[] requiredMultiple,
	    String[] optionalSingle, String[] optionalMultiple, boolean fatal,
	    boolean reportExtra) {

	Annotation copy = new SmallAnnotation(f.getAnnotation());

	if ((copy = checkRequiredSingle(f, requiredSingle, fatal, copy)) == null) {
	    return false;
	}
    //System.err.println("AfterRequiredSingle: "+copy);
	if ((copy = checkRequiredMultiple(f, requiredMultiple, fatal, copy)) == null) {
	    return false;
	}
    //System.err.println("AfterRequiredMultiple: "+copy);
	if ((copy =checkOptionalSingle(f, optionalSingle, fatal, copy)) == null) {
	    return false;
	}
    //System.err.println("AfterOptionalSingle: "+copy);
	if ((copy = checkOptionalMultiple(f, optionalMultiple, fatal, copy)) == null) {
	    return false;
	}
    //System.err.println("AfterOptionalMultiple: "+copy);
	if (reportExtra && !copy.keys().isEmpty()) {
	    StringBuffer keys = new StringBuffer();
        boolean problem = false;
	    for (Iterator it = copy.keys().iterator(); it.hasNext();) {
            String tmpKey = (String) it.next();
            if (!"internal_data".equals(tmpKey)) {
                keys.append(tmpKey);
                keys.append("|");
                problem = true;
            }
	    }
        if (problem) {
            problem(f, keys.toString(), "Unexpected annotation", fatal, copy);
        }
	    return false;
	}

	return true;
    }

    /**
     * @param f
     * @param requiredSingle
     * @param fatal
     * @param copy
     * @return
     */
    private static Annotation checkRequiredSingle(Feature f, String[] requiredSingle, boolean fatal, Annotation copy) {
    	for (int i = 0; i < requiredSingle.length; i++) {
    		String check = requiredSingle[i];
    		List matches = getProperties(check, copy);
    		if (matches == null) {
    			problem(f, check, "No value (required single)", fatal, copy)	;
    			return null;
    		}
    		if (matches.size()==0 || matches.size() >1) {
    			problem(f, check, "Wrong number of values (required multiple)", fatal, copy);
    			return null;
    		}
    		copy = makeAnnotationCopyWithoutKey(check, copy);
    	}
    	return copy;
    }

    @SuppressWarnings("unchecked")
	private static Annotation makeAnnotationCopyWithoutKey(String without, Annotation an) {
        //System.err.println("Want to remove '"+without+"' from annotation '"+an+"'");
		Annotation ret = new SmallAnnotation();
		for (String key : ((Set<String>) an.asMap().keySet())) {
			if (!key.equals(without)) {
				try {
					ret.setProperty(key, an.getProperty(key));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (NoSuchElementException e) {
					e.printStackTrace();
				} catch (ChangeVetoException e) {
					e.printStackTrace();
				}
			}
		}
		return ret;
	}

	/**
     * @param f
     * @param requiredSingle
     * @param fatal
     * @param copy
     * @return
     */
    private static Annotation checkRequiredMultiple(Feature f, String[] requiredMultiple, boolean fatal, Annotation copy) {
    	for (int i = 0; i < requiredMultiple.length; i++) {
    		String check = requiredMultiple[i];
    		List matches = getProperties(check, copy);
    		if (matches == null) {
    			problem(f, check, "No value (required multiple)", fatal, copy);
    			return null;
    		}
    		if (matches.size()==0) {
    			problem(f, check, "Wrong number of values (required multiple)", fatal, copy);
    			return null;
    		}
    		copy = makeAnnotationCopyWithoutKey(check, copy);
    	}
    	return copy;
    }

    /**
     * @param f
     * @param requiredSingle
     * @param fatal
     * @param copy
     * @return
     */
    private static Annotation checkOptionalMultiple(Feature f, String[] optionalMultiple, boolean fatal, Annotation copy) {
    	for (int i = 0; i < optionalMultiple.length; i++) {
    		String check = optionalMultiple[i];
            //System.err.println("checkOptionalMultiple for '"+check+"'");
    		List matches = getProperties(check, copy);
    		if (matches != null && matches.size()>0) {
    		    copy = makeAnnotationCopyWithoutKey(check, copy);
            }
    	}
    	return copy;
    }

    /**
     * @param f
     * @param requiredSingle
     * @param fatal
     * @param copy
     * @return
     */
    private static Annotation checkOptionalSingle(Feature f, String[] optionalSingle, boolean fatal, Annotation copy) {
	for (int i = 0; i < optionalSingle.length; i++) {
	    String check = optionalSingle[i];
	    List matches = getProperties(check, copy);
	    if (matches != null && matches.size() >1) {
		problem(f, check, "Wrong number of values (optional single)", fatal, copy);
		return null;
	    }
	    copy = makeAnnotationCopyWithoutKey(check, copy);
	}
	return copy;
    }

    private static void problem(Feature f, String check, String msg, boolean fatal, Annotation copy) {
	System.err.println(msg+" for '"+check+"' in '"+f.getType()+"' at '"+f.getLocation()+"'");
    System.err.println(copy);
	if (fatal) {
	    System.exit(-1);
	}
    }

}
