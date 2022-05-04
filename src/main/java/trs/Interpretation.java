/*
 * Created on May 6, 2003
 *
 */
package trs;

/**
 * An interface defining Interpretation maps, that is a mapping from terms to elements of a set
 * <p>
 * An Interpretation object is used to interpret, as it names implies, terms which
 * are then assigned meanings in a certain context.
 *
 * @author bailly
 * @version $Id: Interpretation.java,v 1.1 2003/05/06 15:00:18 bailly Exp $
 */
public interface Interpretation {

    /**
     * Returns this Intepretation view of the given Term.
     *
     * @param t
     * @return
     */
    Object eval(Term t);

}
