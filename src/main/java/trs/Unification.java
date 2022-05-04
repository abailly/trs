/*
 * Created on Apr 28, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package trs;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Implements an Unification algorithm inspired from Robinson's algorithm for
 * unifying two terms. A successful Unification produces a mapping from variables
 * to terms which can later be used in a Substitution.
 *
 * @author bailly
 * @version $Id: Unification.java,v 1.4 2003/05/15 08:32:46 bailly Exp $
 */
class Unification implements TermWalker, Map {

    /* current term being compared to */
    private Term cur;

    /* result from unification */
    private final Map map;

    private final Substitution subst;

    /**
     * Creates a new Unification object for the given RewriteSystem
     *
     * @param rs
     */
    Unification(RewriteSystem rs) {
        this.map = new HashMap();
        this.subst = new Substitution(rs, map);
    }

    /**
     * Creates a Unification object for the given RewriteSystem using
     * the given map object.
     *
     * @param m
     * @param rs
     */
    Unification(Map m, RewriteSystem rs) {
        if (m == null)
            throw new IllegalArgumentException("The map cannot be null");
        this.map = m;
        this.subst = new Substitution(rs, this.map);
    }

    /**
     * This method tries to unify <code>t1</code> and <code>t2</code>
     * by calling <code>walk()</code> on first term. It returns a new
     * Term which is the most general unifier for <code>t1</code> and <code>t2</code>, if
     * it exists.
     *
     * @param t1
     * @param t2
     * @throws NotUnifiableException if the two terms do not have a
     *                               common unifier
     */
    public Term unify(Term t1, Term t2) throws NotUnifiableException {
        this.cur = t2;
        try {
            return (Term) t1.walk(this);
        } catch (TermWalkerException tex) {
            throw new NotUnifiableException(tex);
        }
    }

    /*
     * @see trs.TermWalker#walk(trs.Symbol)
     */
    public Object walk(Symbol s) throws TermWalkerException {
        if (cur instanceof Symbol && cur.equals(s))
            return s;
        else if (cur instanceof Variable)
            return unify(cur, s);
        else
            throw new NotUnifiableException(
                    "Cannot unify " + cur + " and " + s);
    }

    /**
     * @throws this method may throw a RewriteException if
     *              new term cannot be constructed
     * @see trs.TermWalker#walk(trs.Term)
     */
    public Object walk(Function term) throws TermWalkerException {
        if (cur instanceof Function) {
            Function f = (Function) cur;
            /* simple case of failed unification */
            if (!f.getSymbol().equals(term.getSymbol()))
                throw new NotUnifiableException("Incompatible symbols in function");
                /* unifies sub terms */
            else {
                Term ret = null; /* last unification done */
                Term[] sub1 = term.getSubTerms();
                Term[] sub2 = f.getSubTerms();
                int l = term.getSubTerms().length;
                for (int i = 0; i < l; i++) {
                    ret = unify(
                            (Term) sub1[i].walk(subst),
                            (Term) sub2[i].walk(subst));
                }
                return ret;
            }
        } else if (cur instanceof Variable)
            return unify(cur, term);
        else
            throw new NotUnifiableException(
                    "Cannot unify " + cur + " and " + term);
    }

    /*
     * @see trs.TermWalker#walk(trs.Variable)
     */
    public Object walk(Variable v) throws TermWalkerException {
        if (v.equals(cur))
            return v;
        /* try to find type variable in map */
        Term m = (Term) map.get(v);
        if (m != null) {
			/* variable t already mapped - recursively unifies 
					m with term and check circularity */
            if (v.equals(map.get(m)))
                return v;
            else
                return unify(m, cur);
        } else {
            /* t is not mapped yet */
            if (v.equals(map.get(cur))) /* circularity check */
                return v;
            else {
                Term r = (Term) cur.walk(subst);
                if (r.getVariables().contains(v))
                    return null;
                map.put(v, r);
                return r;
            }
        }
    }

    /**
     *
     */
    public void clear() {
        map.clear();
    }

    /**
     * @param key
     * @return
     */
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    /**
     * @param value
     * @return
     */
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    /**
     * @return
     */
    public Set entrySet() {
        return map.entrySet();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        return map.equals(obj);
    }

    /**
     * @param key
     * @return
     */
    public Object get(Object key) {
        return map.get(key);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return map.hashCode();
    }

    /**
     * @return
     */
    public boolean isEmpty() {
        return map.isEmpty();
    }

    /**
     * @return
     */
    public Set keySet() {
        return map.keySet();
    }

    /**
     * @param key
     * @param value
     * @return
     */
    public Object put(Object key, Object value) {
        return map.put(key, value);
    }

    /**
     * @param t
     */
    public void putAll(Map t) {
        map.putAll(t);
    }

    /**
     * @param key
     * @return
     */
    public Object remove(Object key) {
        return map.remove(key);
    }

    /**
     * @return
     */
    public int size() {
        return map.size();
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
    public Collection values() {
        return map.values();
    }

}