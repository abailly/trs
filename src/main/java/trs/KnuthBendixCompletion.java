/*______________________________________________________________________________
 *
 * Copyright 2003 Arnaud Bailly - NORSYS/LIFL
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
 * Created on Nov 29, 2003
 * $Log: KnuthBendixCompletion.java,v $
 * Revision 1.2  2004/04/07 16:00:20  bailly
 * added .project file
 *
 * Revision 1.1  2004/02/09 20:50:43  nono
 * *** empty log message ***
 *
 */
package trs;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Implementation of the Knuth-Bendix completion algorithm for equational
 * systems.
 * <p>
 * J.W. Klop, <i>Term Rewriting Systems </i>, p.52
 *
 * @author nono
 * @version $Id: KnuthBendixCompletion.java,v 1.2 2004/04/07 16:00:20 bailly Exp $
 */
public class KnuthBendixCompletion {

    public RewriteSystem complete(EquationalTheory theory) {
        /* intialize target rs */
        RewriteSystemImpl r = new RewriteSystemImpl();
        r.setSignature(theory.getSignature());
        return complete(theory, r);
    }

    /**
     * Create a new RewriteSystem from another one modulo some equational theory.
     *
     * @param impl
     * @param from
     * @return null if no rewrite system can be found
     */
    public RewriteSystem complete(EquationalTheory theory, RewriteSystem r) {
        Engine red = new Engine(r);
        TermOrdering ord = new LexicographicPathOrdering();
        List e = new LinkedList(theory.getEquations());
        while (!e.isEmpty()) {
            Equation eq = (Equation) e.remove(0);
            /* reduce equation terms to normal forms */
            Term sprime = red.rewrite(eq.getLeft());
            Term tprime = red.rewrite(eq.getRight());
            Rule rule = null;
            /*
             * remove equation if NFs are equals else create new rule in R according
             * to ordering of terms
             */
            switch (ord.compare(sprime, tprime)) {
                case TermOrdering.EQ:
                    continue;
                case TermOrdering.GT:
                    rule = r.makeRule(sprime, tprime);
                    break;
                case TermOrdering.LT:
                    rule = r.makeRule(tprime, sprime);
                    break;
                case TermOrdering.NE:
                    return null;
            }
            /* normalize lhs of rules in R U {rule} */
            Iterator it2 = r.getRules().iterator();
            while (it2.hasNext()) {
                Rule ri = (Rule) it2.next();
                Term nl = red.rewrite(ri.getRight());
                if (ord.compare(ri.getRight(), nl) != TermOrdering.EQ) {
                    r.makeRule(ri.getLeft(), nl);
                    r.removeRule(ri);
                }
            }
            /* compute critical pairs from r to new rule*/
            Set cp = r.makeCriticalPairs(rule);
            /* normalize pairs and create new rules */
            e.addAll(cp);
            /* done */
        }
        return r;
    }


}