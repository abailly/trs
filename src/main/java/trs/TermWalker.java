/*
 * Created on Apr 28, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package trs;

/**
 * An implementation of Visitor design pattern to Term walking.
 *
 * @author bailly
 * @version $Id: TermWalker.java,v 1.2 2003/05/15 08:32:46 bailly Exp $
 */
public interface TermWalker {

    /**
     * Walk on a Function object
     *
     * @param t
     * @return an implementation dependent object
     */
    Object walk(Function t) throws TermWalkerException;

    /**
     * Walk on a Symbol objec
     *
     * @param s
     * @return an implementation dependent object
     */
    Object walk(Symbol s) throws TermWalkerException;

    /**
     * Walk a Variable object
     *
     * @param v
     * @return an implementation dependnet object
     */
    Object walk(Variable v) throws TermWalkerException;

}
