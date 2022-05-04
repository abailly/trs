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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test iteration over subterms of some term.
 *
 * @author nono
 * @version $Id$
 */
public class SubtermIteratorTest {

    private final RewriteSystemImpl rs = new RewriteSystemImpl();
    private final Signature sig = rs.getSignature();
    private Term term;

    private final Command command = new Command() {
        /* (non-Javadoc)
         * @see trs.Command#rewrite(trs.Term)
         */
        public void rewrite(Term t) {
            term = t;
        }

        /* (non-Javadoc)
         * @see trs.Command#quit()
         */
        public void quit() {
            // TODO Auto-generated method stub

        }

        /* (non-Javadoc)
         * @see trs.Command#load(java.lang.String)
         */
        public void load(String file) {
            // TODO Auto-generated method stub

        }

        /* (non-Javadoc)
         * @see trs.Command#save(java.lang.String)
         */
        public void save(String file) {
            // TODO Auto-generated method stub

        }

        /* (non-Javadoc)
         * @see trs.Command#clear()
         */
        public void clear() {
            // TODO Auto-generated method stub

        }

        /* (non-Javadoc)
         * @see trs.Command#debug()
         */
        public void debug() {
            // TODO Auto-generated method stub

        }

        /* (non-Javadoc)
         * @see trs.Command#equalize(trs.Term, trs.Term)
         */
        public void equalize(Term term, Term term2) {
            // TODO Auto-generated method stub

        }
    };
    private final Yyparser parser = new Yyparser();

    /*
     * @see TestCase#setUp()
     */
    @BeforeEach
    protected void setUp() throws Exception {
        parser.setRewriteSystem(rs);
        parser.setSignature(sig);
        parser.setCommand(command);
        InputStream stream = getClass().getClassLoader().getResourceAsStream("sort.trs");
        parser.parse(stream);
    }

    /*
     * unary terms
     */

    @Test
    public void testSimple() {
        String tr = "? s(s(s(nil)))";
        parser.parse(new StringReader(tr));
        SubtermIterator si = new SubtermIterator(term, sig);
        int i = 0;
        while (si.hasNext()) {
            si.next();
            i++;
        }
        assertEquals(4, i);
    }

    /*
     * symbol
     */
    @Test
    public void testSimple2() {
        String tr = "? nil";
        parser.parse(new StringReader(tr));
        SubtermIterator si = new SubtermIterator(term, sig);
        int i = 0;
        while (si.hasNext()) {
            si.next();
            i++;
        }
        assertEquals(1, i);
    }

    public void test3() {
        String tr = "? max(s(x),s(min(t,u)))";
        parser.parse(new StringReader(tr));
        SubtermIterator si = new SubtermIterator(term, sig);
        int i = 0;
        while (si.hasNext()) {
            si.next();
            i++;
        }
        assertEquals(7, i);
    }

    public void testReplace() {
        String tr = "? max(s(x),s(min(t,u)))";
        parser.parse(new StringReader(tr));
        SubtermIterator si = new SubtermIterator(term, sig);
        int i = 0;
        Term t = si.next();
        System.err.println(t);
        t = si.makeContextualReplacement(sig.getSymbol("nil"));
        assertEquals("max(s(x),s(min(nil,u)))", t.toString());
        t = si.next();
        assertEquals("t", t.toString());
        t = si.makeContextualReplacement(sig.getSymbol("nil"));
        assertEquals("max(s(x),s(nil))", t.toString());
        t = si.next();
        t = si.next();
        t = si.makeContextualReplacement(sig.getSymbol("nil"));
        assertEquals("max(s(nil),s(min(t,u)))", t.toString());
    }
}

/*
 * $Log$
 */