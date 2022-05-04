/*______________________________________________________________________________
 *
 * Copyright 2004 Arnaud Bailly - NORSYS/LIFL
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * (1) Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 * (2) Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in
 *     the documentation and/or other materials provided with the
 *     distribution.
 *
 * (3) The name of the author may not be used to endorse or promote
 *     products derived from this software without specific prior
 *     written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *______________________________________________________________________________
 *
 * Created on Oct 20, 2004
 *
 */
package trs;

import java.util.HashSet;
import java.util.Set;

/**
 * @author nono
 * @version $Id$
 */
public class EquationalTheoryImpl implements EquationalTheory {

    private final Signature signature;

    private final Set equations;

    public EquationalTheoryImpl() {
        this.signature = new SignatureImpl();
        this.equations = new HashSet();
    }

    /* (non-Javadoc)
     * @see trs.EquationalTheory#getSignature()
     */
    public Signature getSignature() {
        return signature;
    }

    /* (non-Javadoc)
     * @see trs.EquationalTheory#makeEquation(trs.Term, trs.Term)
     */
    public Equation makeEquation(Term left, Term right) {
        Set s1 = left.getVariables();
        Set s2 = right.getVariables();
        if (!left.getVariables().containsAll(right.getVariables()))
            throw new RewriteException(
                    "There are free variables in consequence of rule : " + s2 + "!in"
                            + s1);
        /* newly created equation */
        EquationImpl r = new EquationImpl(left, right);
        equations.add(r);
        return r;
    }

    /* (non-Javadoc)
     * @see trs.EquationalTheory#getEquations()
     */
    public Set getEquations() {
        return new HashSet(equations);
    }

    /* (non-Javadoc)
     * @see trs.EquationalTheory#complete()
     */
    public RewriteSystem complete() {
        return new KnuthBendixCompletion().complete(this);
    }

    /* (non-Javadoc)
     * @see trs.EquationalTheory#complete(trs.RewriteSystem)
     */
    public RewriteSystem complete(RewriteSystem from) {
        return new KnuthBendixCompletion().complete(this, from);
    }

}

/*
 * $Log$
 */