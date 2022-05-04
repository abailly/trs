/*
 * Created on Apr 28, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package trs;

/**
 * @author bailly
 * @version $Id: RuleImpl.java,v 1.4 2004/02/09 20:52:43 nono Exp $
 */
class RuleImpl implements Rule {

    private Term lhs;

    private final Term rhs;

    /* identifier for rule */
    private int id;

    /**
     *
     */
    RuleImpl(Term l, Term r) {
        this.lhs = l;
        this.rhs = r;
    }

    /* (non-Javadoc)
     * @see trs.Rule#getCondition()
     */
    public Term getLeft() {
        return lhs;
    }

    /* (non-Javadoc)
     * @see trs.Rule#getConclusion()
     */
    public Term getRight() {
        return rhs;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(lhs.getVariables()).append(" : ").append(lhs).append(
                " -> ").append(
                rhs);
        return sb.toString();
    }

    /**
     * @param i
     */
    void setId(int i) {
        this.id = i;
    }

    /**
     * @return
     */
    int getId() {
        return id;
    }

    /**
     * @param term
     */
    public void setLeft(Term term) {
        this.lhs = term;
    }

}
