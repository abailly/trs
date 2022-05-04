/*
 * Created on Apr 28, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package trs;


/**
 * A Function is a term which consists in a Symbol and a list of sub-terms
 *
 * @author bailly
 * @version $Id: Function.java,v 1.3 2003/05/15 08:32:46 bailly Exp $
 */
public interface Function extends Term {


    /**
     * This method returns all the sub-terms in this term as an array of Term object
     * ordered from left to right. The list's size must be equal to the
     * arity of this term's symbol :
     * <ul>
     * <li><code>this.getSubTerms().length == this.getSymbol().arity()</code></li>
     * </ul>
     *
     * @return the list ordered from left to right of all subterms
     * in this term
     */
    Term[] getSubTerms();


    /**
     * This method returns the symbol from which this term is constructed as
     * a Symbol object. This method never returns null.
     *
     * @return a Symbol object
     */
    Symbol getSymbol();

    /**
     * Visit this Term object.
     *
     * @param tw
     * @return
     */
    Object walk(TermWalker tw) throws TermWalkerException;


}
