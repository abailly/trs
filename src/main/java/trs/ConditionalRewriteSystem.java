package trs;

import java.util.List;

/**
 * An extension of RewriteSystem interface to build Conditional Rewrite System.
 *
 * @author bailly
 * @version $Id: ConditionalRewriteSystem.java,v 1.1 2003/05/02 11:50:44 bailly Exp $
 */
public interface ConditionalRewriteSystem extends RewriteSystem {

    /**
     * Adds a new conditional rule to this Conditional rewrite system
     *
     * @param conditions the list of conditions - Equation pairs - to be met
     *                   for application of the rule
     * @param left       left hand side of rule
     * @param right      right hand side of rule
     */
    Rule makeConditionalRule(List conditions, Term left, Term right);
}
