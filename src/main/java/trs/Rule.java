/*
 * Created on Apr 28, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package trs;


/**
 * An interface defining rules.
 *
 * @author bailly
 * @version $Id: Rule.java,v 1.4 2004/02/09 20:52:43 nono Exp $
 */
public interface Rule {

    /**
     * Returns the right-hand side of this rule as  a Term object.
     *
     * @return a Term object
     */
    Term getLeft();

    /**
     * Returns the left-hand side of this rule as a Term object.
     *
     * @return a Term object
     */
    Term getRight();

    /**
     * @param term
     */
    void setLeft(Term term);

}
