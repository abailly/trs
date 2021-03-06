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

import java.util.Set;

/**
 * Test computation of critical pairs in TRS.
 *
 * @author nono
 * @version $Id$
 */
public class CriticalPairTest {

    private RewriteSystemImpl rs;

    private Signature sig;

    /*
     * @see TestCase#setUp()
     */
    @BeforeEach
    protected void setUp() throws Exception {
        Yyparser parser = new Yyparser();
        rs = new RewriteSystemImpl();
        sig = rs.getSignature();
        parser.setRewriteSystem(rs);
        parser.setSignature(sig);
        parser.parse(getClass().getClassLoader().getResourceAsStream("sort.trs"));
    }

    @Test
    public void testSimple() {
        Term t1 = sig.makeFunction(sig.getSymbol("s"), new Term[]{sig
                .makeVariable("t")});
        Term t2 = sig.makeFunction(sig.getSymbol("s"),
                new Term[]{sig.makeFunction(sig.getSymbol("s"), new Term[]{sig
                        .makeVariable("t")})});
        Rule r = rs.makeRule(t1, t2);
        Set s = rs.makeCriticalPairs(r);
        System.err.println(s);
    }
}

/*
 * $Log$
 */