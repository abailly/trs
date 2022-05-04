/*
 * Created on Apr 28, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package trs;

import java.util.Map;

/**
 * Implements a Substitution algorithm for replacing variables occurences in
 * terms with their mappings.
 *
 * @author bailly
 * @version $Id: Substitution.java,v 1.4 2003/05/15 08:32:46 bailly Exp $
 */
class Substitution implements TermWalker {

    private final RewriteSystem rs;

    private final Map map;

    Substitution(RewriteSystem rs, Map m) {
        this.map = m;
        this.rs = rs;
    }

    /*
     * @see trs.TermWalker#walk(trs.Symbol)
     */
    public Object walk(Symbol s) {
        return s;
    }

    /**
     * @throws this method may throw a RewriteException if
     *              new term cannot be constructed
     * @see trs.TermWalker#walk(trs.Term)
     */
    public Object walk(Function term) throws TermWalkerException {
        Term[] ts = term.getSubTerms();
        Symbol s = term.getSymbol();
        Term[] t = new Term[s.arity()];
        int l = ts.length;
        for (int i = 0; i < l; i++) {
            t[i] = (Term) ts[i].walk(this);
        }
        return rs.getSignature().makeFunction(s, t);
    }

    /*
     * @see trs.TermWalker#walk(trs.Variable)
     */
    public Object walk(Variable v) throws TermWalkerException {
        Term t = (Term) map.get(v);
        if (t == null)
            return v;
        else
            return t.walk(this);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return map.toString();
    }

    /**
     * @return
     */
    public boolean isEmpty() {
        return map.isEmpty();
    }
}