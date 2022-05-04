/*
 * Created on May 6, 2003 by Arnaud Bailly - bailly@lifl.fr
 * Copyright 2003 - Arnaud Bailly
 */
package trs;

import java.util.Set;

/**
 * An interface to equational systems, defined from a signature and equalities between terms
 * <p>
 * An equational theory is
 *
 * @author bailly
 * @version $Id: EquationalTheory.java,v 1.3 2004/02/09 20:52:43 nono Exp $
 */
public interface EquationalTheory {

    /**
     * Returns the Signature object associated with this equational theory.
     *
     * @return a Signature instance
     */
    Signature getSignature();

    /**
     * Adds a new Equation to this theory.
     * <p>
     * This method takes two terms and return an Equation object
     * which is added to this theory.
     *
     * @param l a Term
     * @param r a Term
     * @return a new Equation object
     */
    Equation makeEquation(Term l, Term r);

    /**
     * Return the set of equations defining this Equational theory
     *
     * @return an instance of Set, possibly empty
     */
    Set getEquations();

    /**
     * Returns a - directed - RewriteSystem which is the completion
     * of this EquationalTheory through the Knuth-Bendix classical
     * algorithm.
     *
     * @return a RewriteSystem which is equivalent to this Equational Theory
     * and can be used to complete terms
     */
    RewriteSystem complete();

    /**
     * Create a RewriteSystem from a starting rewrite system
     * modulo this equational theory
     *
     * @param from the original rewrite system
     * @return a new Rewrite System to
     */
    RewriteSystem complete(RewriteSystem from);

}