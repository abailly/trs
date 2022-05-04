/*
 * Created on Apr 28, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package trs;

import java.util.HashSet;
import java.util.Set;

/**
 * Provides a default implementation for the Function interface
 *
 * @author bailly
 * @version $Id: FunctionImpl.java,v 1.5 2003/05/15 08:32:46 bailly Exp $
 */
class FunctionImpl extends TermImpl implements Function {

    Term[] subterms;

    private final Symbol sym;

    FunctionImpl(Symbol sym, Term[] subterms) {
        this.sym = sym;
        this.subterms = subterms;
    }

    /* (non-Javadoc)
     * @see trs.Term#getSubTerms()
     */
    public Term[] getSubTerms() {
        return subterms;
    }

    /* (non-Javadoc)
     * @see trs.Term#getSymbol()
     */
    public Symbol getSymbol() {
        return sym;
    }

    /* (non-Javadoc)
     * @see trs.Term#getVariables()
     */
    public Set getVariables() {
        Set s = new HashSet();
        int l = subterms.length;
        for (int i = 0; i < l; i++) {
            s.addAll(subterms[i].getVariables());
        }
        return s;
    }

    /* (non-Javadoc)
     * @see trs.Term#walk(trs.TermWalker)
     */
    public Object walk(TermWalker tw) throws TermWalkerException {
        return tw.walk(this);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        Symbol sym = getSymbol();
        String symstr = sym.toString();
        if (sym.isInfix())
            return infixMode();
        StringBuffer sb = new StringBuffer(symstr);
        sb.append('(');
        int l = subterms.length;
        for (int i = 0; i < l; i++) {
            sb.append(subterms[i]);
            if (i + 1 < l)
                sb.append(',');
        }
        sb.append(')');
        return sb.toString();
    }

    /**
     * @return
     */
    private String infixMode() {
        StringBuffer sb = new StringBuffer();
        sb
                .append('(')
                .append(subterms[0])
                .append(' ')
                .append(getSymbol().toString())
                .append(' ')
                .append(subterms[1])
                .append(')');
        return sb.toString();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof FunctionImpl))
            return false;
        FunctionImpl fi = (FunctionImpl) obj;
        if (!sym.equals(fi.sym))
            return false;
        int i = 0, j = 0;
        for (; i < subterms.length && j < fi.subterms.length; i++, j++)
            if (!subterms[i].equals(fi.subterms[j]))
                return false;
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        int h = sym.hashCode();
        int l = subterms.length;
        for (int i = 0; i < l; i++) {
            h <<= 3;
            h ^= subterms[i].hashCode();
        }
        return h;
    }

    /**
     * Replace the subterm at position i with given term and return the
     * replaced term
     *
     * @param i the position
     * @param t the term
     * @throws if i <0 or i>arity-1
     */
    Term setSubterm(int i, Term t) {
        Term old = this.subterms[i];
        this.subterms[i] = t;
        return old;
    }
}
