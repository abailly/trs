/*
 * Created on May 6, 2003 by Arnaud Bailly - bailly@lifl.fr
 * Copyright 2003 - Arnaud Bailly
 */
package trs;

/**
 * @author bailly
 * @version $Id: EquationImpl.java,v 1.1 2003/05/06 15:00:18 bailly Exp $
 */
class EquationImpl extends RuleImpl implements Equation {

    /**
     * @param rs
     * @param l
     * @param r
     */
    public EquationImpl(Term l, Term r) {
        super(l, r);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb
                .append(getLeft().getVariables())
                .append(" : ")
                .append(getLeft())
                .append(" = ")
                .append(getRight());
        return sb.toString();
    }

}
