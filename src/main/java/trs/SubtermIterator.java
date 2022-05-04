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
 * Created on Oct 18, 2004
 *
 */
package trs;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that iterates over all subterms of some term, from leaves to root.
 *
 * @author nono
 * @version $Id$
 */
public class SubtermIterator implements TermWalker {

    List ctx = new ArrayList();

    int i = -1;

    Term next;

    Signature sig;

    public SubtermIterator(Term t, Signature sig) {
        try {
            this.sig = sig;
            t.walk(this);
            i = ctx.size() - 1;
        } catch (TermWalkerException e) {
        }
    }

    /*
     * Generate a new term where last term is replaced by given term within same
     * context
     *
     * @param rl
     */
    public Term makeContextualReplacement(Term rl) {
        /* sanity check */
        if (next == null)
            return null;
        /* rebuild expression */
        List nexp = new ArrayList(ctx);
        nexp.set(i, rl);
        return makeTermFromList(nexp);
    }

    /**
     * Generate a term from a depth-first listing of terms
     *
     * @param nexp
     * @return
     */
    private Term makeTermFromList(List nexp) {
        Term cur = (Term) nexp.get(0);
        if (!(cur instanceof Function))
            return cur;
        Function f = (Function) cur;
        Symbol s = f.getSymbol();
        int len = nexp.size();
        int ar = s.arity();
        List subs = new ArrayList();
        int i = 1;
        while (subs.size() < (ar)) {
            Term t = (Term) nexp.get(i);
            if (t instanceof Function) {
                t = makeTermFromList(nexp.subList(i, len));
                i += ((Function) t).getSymbol().arity() + 1;
            } else if (t instanceof Symbol) {
                t = sig.getSymbol(t.toString());
                i++;
            } else if (t instanceof Variable) {
                t = sig.makeVariable(t.toString());
                i++;
            }
            subs.add(t);
        }
        return sig.makeFunction(s, subs);
    }

    public Term next() {
        next = (Term) ((i == -1) ? null : ctx.get(i--));
        return next;
    }

    public boolean hasNext() {
        return i != -1;
    }

    /*
     * (non-Javadoc)
     *
     * @see trs.TermWalker#walk(trs.Function)
     */
    public Object walk(Function t) throws TermWalkerException {
        Term[] subs = t.getSubTerms();
        ctx.add(t);
        for (int i = 0; i < subs.length; i++) {
            subs[i].walk(this);
        }
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see trs.TermWalker#walk(trs.Symbol)
     */
    public Object walk(Symbol s) throws TermWalkerException {
        ctx.add(s);
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see trs.TermWalker#walk(trs.Variable)
     */
    public Object walk(Variable v) throws TermWalkerException {
        ctx.add(v);
        return null;
    }

}
/*
 * $Log$
 */