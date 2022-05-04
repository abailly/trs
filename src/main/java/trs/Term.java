package trs;

import java.util.Set;

/**
 * @author bailly
 * @version $Id: Term.java,v 1.4 2003/05/15 08:32:46 bailly Exp $
 */
public interface Term {


    /**
     * This method returns a String representation of this Term. This representation
     * is compliant with the following convention:
     * <ul>
     * <li>if <code>this.getSymbol().arity() == 0</code>, then <code>this.toString().equals(this.getSymbol().toString()</code> ,</li>
     * <li>else, the symbol is shown first and then the list of of sub-terms enclosed with parentheses.</li>
     * </ul>
     *
     * @return a String denoting this term
     */
    String toString();

    /**
     * This method returns the Set of all Variable objects present
     * in this Term.
     *
     * @return
     */
    Set getVariables();

    /**
     * Visit this Term object.
     *
     * @param tw
     * @return
     */
    Object walk(TermWalker tw) throws TermWalkerException;

}
