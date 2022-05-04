/*
 * Created on Apr 28, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package trs;

import java.util.Iterator;
import java.util.List;

/**
 * A collection of symbols.
 *
 * @author bailly
 * @version $Id: Signature.java,v 1.3 2003/05/06 15:00:18 bailly Exp $
 */
public interface Signature {

    /**
     * This constant denotes non associative symbols
     */
    int NON_ASSOC = 0;

    /**
     * This constant denotes right-associative symbols : a + b + c = (a + (b + c))
     */
    int RIGHT_ASSOC = 1;

    /**
     * This constant denotes left-associative symbols : a + b + c = ((a + b) + c)
     */
    int LEFT_ASSOC = 2;

    /**
     * This method returns an Iterator object ranging over all
     * symbols from this Signature.
     *
     * @return an Iterator object
     * @see java.util.Iterator
     */
    Iterator iterator();

    /**
     * This method returns the size of this Signature. It returns -1 if the
     * Signature is constitued from an infinite collection of symbols
     *
     * @return an integer greater than  0 or -1
     */
    int size();

    /**
     * Checks that given symbol is part of this signature.
     *
     * @param sym
     * @return true is sym is part of this signature, false otherwise
     */
    boolean contains(Symbol sym);

    /**
     * Returns the symbol object associated with given string representation. This
     * function may return null if no symbol is associated with this string.
     *
     * @param str
     * @return a Symbol object or null
     */
    Symbol getSymbol(String str);

    /**
     * A factory method to create symbols given a String and an arity.
     *
     * @param str   the name of this symbol
     * @param arity an integer greater than or equal to 0
     * @return a Symbol object which is also made part of this Signature
     * @throws RewriteException if str is already associated with a symbol.
     */
    Symbol makeSymbol(String str, int arity) throws RewriteException;

    /**
     * Factory method to construct terms with given Symbol and List of terms.
     * <p>
     * This method returns a new Term built from the given Symbol object and
     * sub terms. If <code>sym</code> arity if not equal to <code>terms</code> size,
     * this method throws a RewriteException.
     *
     * @param sym   a Symbol object
     * @param terms the list of subterms - may not be null/
     * @return a new Term
     * @throws RewriteException if <code>sym.arity() != terms.size()</code>
     */
    Term makeFunction(Symbol sym, List terms) throws RewriteException;

    /**
     * Factory method to construct terms with given Symbol and array of terms.
     * <p>
     * This method returns a new Term built from the given Symbol object and
     * sub terms. If <code>sym</code> arity if not equal to <code>terms</code> size,
     * this method throws a RewriteException.
     *
     * @param sym   a Symbol object
     * @param terms an array of subterms - may not be null
     * @return a new Term
     * @throws RewriteException if <code>sym.arity() != terms.size()</code>
     */
    Term makeFunction(Symbol sym, Term[] terms) throws RewriteException;

    /**
     * Factory method to create variable from a Signature. The only restriction imposed
     * on str is that it cannot conflict with a symbol defined in this signature. A Variable
     * object with the same name can be created several times.
     *
     * @param str name of variable to create.
     * @return an instance of Variable
     * @throws RewriteException is name clashes with defined symbol in this signature
     */
    Variable makeVariable(String str) throws RewriteException;

}
