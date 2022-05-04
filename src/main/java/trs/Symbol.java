/*
 * Created on Apr 28, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package trs;

/**
 * An interface that represents a Symbol from the signature of the term-rewrite system
 * <p>
 * A Symbol is also a Term.
 *
 * @author bailly
 * @version $Id: Symbol.java,v 1.5 2003/05/15 20:13:30 bailly Exp $
 */
public interface Symbol extends Term, Comparable {

    /**
     * Returns a string representation of this symbol.
     *
     * @return a representation of this symbol as a String object
     */
    String toString();

    /**
     * Returns the associativity of this symbol. Associativit is
     * defined as integer constants in Signature interface.
     *
     * @return a constant from Signature denoting associativity of this symbol
     * @see Signature
     */
    int getAssociativity();

    void setAssociativity(int i);

    /**
     * Returns the arity of this symbol.
     *
     * @return an int greater than or equal to 0
     */
    int arity();

    /**
     * @see trs.Term#walk(trs.TermWalker)
     */
    Object walk(TermWalker tw) throws TermWalkerException;

    /**
     * Returns true if this symbol preferred layout is infix notation. If this
     * is the case, then this symbol must have arity 2.
     *
     * @return true
     */
    boolean isInfix();

    /**
     * Sets this symbol infix status
     *
     * @param b
     */
    void setInfix(boolean b);

}
