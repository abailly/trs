/*
 * Created on May 15, 2003
 * Copyright 2003 Arnaud Bailly
 */
package trs;

/**
 * An interface defining a relation order between terms.
 * <p>
 * This interface is implemented by classes wishing to provide
 * an ordering between terms. This interface does not specify the
 * meaning of this ordering, neither its properties (total, partial,
 * reduction, simplification).
 *
 * @author bailly
 * @version $Id: TermOrdering.java,v 1.1 2003/05/15 20:13:30 bailly Exp $
 */
public interface TermOrdering {

    /**
     * Constant denoting that two terms are equal.
     */
    int EQ = 0;

    /**
     * Constant denoting that a term is greater than another one.
     */
    int GT = 1;

    /**
     * Constant denoting that a term is lower than another one.
     */
    int LT = -1;

    /**
     * Constant denoting that two terms are incomparable.
     */
    int NE = Integer.MAX_VALUE;

    /**
     * Main comparison method between terms.
     * <p>
     * This method returns a integer denoting the ordering relation
     * between the two terms. This integer must be one of the
     * constant defines in this interface :
     * <ul>
     * <li><code>GT</code> if <code>t1 &gt;  t2</code>,</li>
     * <li><code>EQ</code> if <code>t1 = t2</code>,</li>
     * <li><code>LT</code> if <code>t1 &lt;  t2</code>,</li>
     * <li><code>NE</code> if <code>t1 !=  t2</code> and no other
     * constant apply,</li>
     * </ul>
     * The last constant allows implementation to be partial orders on
     * terms.
     *
     * @param t1
     * @param t2
     * @return a constant integer defined in this class
     */
    int compare(Term t1, Term t2);

}
