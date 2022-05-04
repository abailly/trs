/*
 * Created on Apr 28, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package trs;

import java.util.Set;

/**
 * Definition of a terms rewrite system.
 * <p>
 * A rewrite system is constituted of a Signature - a collection of symbols with a positive or
 * null arity - and a set of  Rules applying to terms constituted from symbols, parenthesis
 * and variables.
 *
 * @author bailly
 * @version $Id: RewriteSystem.java,v 1.2 2003/04/29 09:22:52 bailly Exp $
 */
public interface RewriteSystem {

    /**
     * Returns the Signature from this RewriteSystem
     *
     * @return a Signature object
     */
	Signature getSignature();

    /**
     * Return the set of rules from this RewriteSystem
     *
     * @return a Set object containg Rule instances
     */
	Set getRules();

    /**
     * Returns a subset of all rules whose head matches the give symbol. This
     * subset may be empty. The null symbol matches all rules that do not
     * have conditions - ie all rules whose condition is a variable.
     *
     * @param sym a Symbol to find a match for
     * @return a Set object which is a subset of <code>getRules()</code>
     */
	Set getRulesFor(Symbol sym);

    /**
     * Factory method to craeate rules in a rewrite system
     */
	Rule makeRule(Term left, Term right)
            throws RewriteException;

    void removeRule(Rule ri);

    Set makeCriticalPairs(Rule rule);

}
