/*
 * Created on May 15, 2003
 * Copyright 2003 Arnaud Bailly
 */
package trs;

import org.junit.jupiter.api.Test;

import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * @author bailly
 * @version $Id: LexicographicPathOrderingTest.java,v 1.1 2004/06/25 15:16:37 bailly Exp $
 */
public class LexicographicPathOrderingTest {

    @Test
    public void testSimple() {
        String text = "sig  z ;s(1); + ";
        Yyparser parser = new Yyparser();
        RewriteSystem rs = new RewriteSystemImpl();
        parser.setRewriteSystem(rs);
        parser.setSignature(rs.getSignature());
        parser.parse(new StringReader(text));
        /* check terms are equals */
        Signature sig = rs.getSignature();
        Term t1 = sig.getSymbol("z");
        Term t2 = sig.makeFunction(sig.getSymbol("s"), new Term[]{t1});
        LexicographicPathOrdering lpo = new LexicographicPathOrdering();
        assertEquals(TermOrdering.GT, lpo.compare(t2, t1));
        assertEquals(TermOrdering.LT, lpo.compare(t1, t2));
    }

    @Test
    public void testUnbaltree() {
        String text = "sig  z ;s(1); + ";
        Yyparser parser = new Yyparser();
        RewriteSystem rs = new RewriteSystemImpl();
        parser.setRewriteSystem(rs);
        parser.setSignature(rs.getSignature());
        parser.parse(new StringReader(text));
        /* check terms are equals */
        Signature sig = rs.getSignature();
        Term t1 = sig.makeFunction(sig.getSymbol("+"), new Term[]{sig.makeVariable("x"), sig.makeVariable("y")});
        t1 = sig.makeFunction(sig.getSymbol("+"), new Term[]{t1, sig.makeVariable("t")});
        Term t2 = sig.makeFunction(sig.getSymbol("+"), new Term[]{sig.makeVariable("x"), sig.makeVariable("y")});
        t2 = sig.makeFunction(sig.getSymbol("+"), new Term[]{sig.makeVariable("t"), t2});
        LexicographicPathOrdering lpo = new LexicographicPathOrdering();
        assertEquals(TermOrdering.GT, lpo.compare(t2, t1));
        assertEquals(TermOrdering.LT, lpo.compare(t1, t2));
    }

}
