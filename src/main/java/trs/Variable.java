/*
 * Created on Apr 28, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package trs;

/**
 * Definition of a Variable as Term
 *
 * @author bailly
 * @version $Id: Variable.java,v 1.2 2003/05/15 08:32:46 bailly Exp $
 */
public interface Variable extends Term {

    /**
     * Returns a string representation of this variable.
     *
     * @return a representation of this variable as a String object
     */
    String toString();

    /**
     * @see trs.Term#walk(trs.TermWalker)
     */
    Object walk(TermWalker tw) throws TermWalkerException;

}
