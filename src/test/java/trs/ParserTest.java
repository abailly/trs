/*
 * Created on Apr 28, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package trs;


import org.junit.jupiter.api.Test;

import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author bailly
 * @version $Id: ParserTest.java,v 1.1 2004/06/25 15:16:37 bailly Exp $
 */
public class ParserTest {

    private Term term;
    Command command = new Command() {
        @Override
        public void rewrite(Term t) {
            term = t;
        }

        @Override
        public void quit() {

        }

        @Override
        public void load(String file) {

        }

        @Override
        public void save(String file) {

        }

        @Override
        public void clear() {

        }

        @Override
        public void debug() {

        }

        @Override
        public void equalize(Term term, Term term2) {

        }
    };

    @Test
    public void test01() {
        String text = "sig s(1) ; z ; + " +
                "rew [ x,y ] s(x) + y -> s(x + y);" +
                "	  [ x,y ] x+ s(y) -> s(x + y);" +
                "	  [ x ] x +z -> x;" +
                "    [ x ] z + x -> x " +
                "? s(z) + s(s(z))";
        Yyparser parser = new Yyparser(true);
        RewriteSystem rs = new RewriteSystemImpl();
        parser.setRewriteSystem(rs);
        parser.setEngine(new Engine(rs));
        parser.setSignature(rs.getSignature());
        parser.setCommand(command);
        parser.parse(new StringReader((text)));
        System.out.println(parser.getSignature());
        assertEquals("s(s(s(z)))", term.toString());
    }
}
