package trs;

import java.util.List;

/**
 * @author bailly
 * @version $Id: ConditionalRule.java,v 1.1 2003/05/02 11:50:44 bailly Exp $
 */
public interface ConditionalRule extends Rule {

    /**
     * Returns the list of conditions to be met before this
     * rule can be applied
     *
     * @return a List of Equation objects
     */
    List getConditions();


}
