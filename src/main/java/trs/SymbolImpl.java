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
 * Default implementation for a Symbol
 *
 * @author bailly
 * @version $Id: SymbolImpl.java,v 1.7 2004/02/09 20:52:43 nono Exp $
 */
class SymbolImpl extends TermImpl implements Symbol {

    private boolean infix;

    private final String name;

    private final int arity;

    private int associativity = Signature.NON_ASSOC;

    /* number identifying symbol */
    private final int id;

    /**
     * @param rs
     */
    SymbolImpl(String name, int arity, int id) {
        this.name = name;
        this.arity = arity;
        this.id = id;
    }

    /**
     * @param rs
     */
    SymbolImpl(String name, int id) {
        this.name = name;
        this.arity = 0;
        this.id = id;
    }

    /* (non-Javadoc)
     * @see trs.Symbol#arity()
     */
    public int arity() {
        return arity;
    }

    /* (non-Javadoc)
     * @see trs.Term#walk(trs.TermWalker)
     */
    public Object walk(TermWalker tw) throws TermWalkerException {
        return tw.walk(this);
    }

    /* (non-Javadoc)
     * @see trs.Term#getVariables()
     */
    public Set getVariables() {
        return new HashSet();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return name;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof SymbolImpl))
            return false;
        return id == ((SymbolImpl) obj).id;
    }

    /* (non-Javadoc)
     * @see trs.Symbol#isInfix()
     */
    public boolean isInfix() {
        return infix;
    }

    /**
     * Sets this symbol infix status
     *
     * @param b
     */
    public void setInfix(boolean b) {
        infix = b;
    }

    /**
     * @return
     */
    int getId() {
        return id;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return id;
    }

    /**
     * @return
     */
    public int getAssociativity() {
        return associativity;
    }

    /**
     * @param i
     */
    public void setAssociativity(int i) {
        associativity = i;
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object o) {
        if (!(o instanceof SymbolImpl))
            throw new IllegalArgumentException("Cannot compare a symbol with something not a symbol");
        SymbolImpl si = (SymbolImpl) o;
        if (id > si.id)
            return 1;
        else if (id < si.id)
            return -1;
        else
            return 0;
    }

}
