/*
 * Created on Apr 28, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package trs;

import java.util.*;

/**
 * This class implements a basic Rewrite engine.
 *
 * @author bailly
 * @version $Id: Engine.java,v 1.11 2003/05/15 20:13:30 bailly Exp $
 */
public class Engine {

    /* the rewrite system this engine should implement */
    private final RewriteSystem rs;

    /* current substitution */
    private Substitution subst;

    private int countRewrite;

    private int countRules;

    private long startTime;

    private long endTime;

    private int loops;

    class Rewriter implements TermWalker {

        private int indent = 0;

        private final Map map = new HashMap();

        /*
         * (non-Javadoc)
         *
         * @see trs.TermWalker#walk(trs.Function)
         */
        public Object walk(Function t) throws TermWalkerException {
            Term res;
            /* first rewrite sub terms */
            indent++;
            countRewrite++;
            Term[] ts = t.getSubTerms();
            Term[] subs = new Term[t.getSymbol().arity()];
            int l = ts.length;
            for (int i = 0; i < l; i++)
                subs[i] = (Term) ts[i].walk(this);
            /* rewrite function itself */
            FunctionImpl cur = (FunctionImpl) rs.getSignature().makeFunction(
                    t.getSymbol(), subs);
            res = cur;
            Set set = rs.getRulesFor(cur.getSymbol());
            if (set != null) {
                for (Object o : set) {
                    Rule r = (Rule) o;
                    var m = new HashMap();
                    Term uni = fresh(m, r.getLeft());
                    try { /* apply mapping to conclusion */
                        new Unification(map, rs).unify(uni, cur);
                        res = substitute(substitute(r.getRight(), m), map);
                        countRules++;
                        if (debug)
                            System.err.println("Engine => " + indent() + cur + " -> " + res);
                        break;
                    } catch (NotUnifiableException e) {
                        if (debug)
                            System.err.println("Engine => Failed to unify " + cur + " with "
                                    + r.getLeft());
                    }
                }
            } else if (debug)
                System.err.println("Engine => No rules for symbol " + cur.getSymbol()
                        + " in " + cur);

            indent--;
            return res;
        }

        /**
         * @return
         */
        private String indent() {
            char[] points = new char[indent];
            Arrays.fill(points, '.');
            return new String(points);
        }

        /*
         * (non-Javadoc)
         *
         * @see trs.TermWalker#walk(trs.Symbol)
         */
        public Object walk(Symbol s) {
            indent++;
            countRewrite++;
            Term res = s;
            Set set = rs.getRulesFor(s);
            if (set != null) {
                Iterator it = set.iterator();
                while (it.hasNext()) {
                    Rule r = (Rule) it.next();
                    Map m = new HashMap();
                    Term uni = fresh(m, r.getLeft());
                    try {
                        uni = new Unification(m, rs).unify(uni, s);
                        res = substitute(substitute(r.getRight(), m), map);
                        countRules++;
                        if (debug)
                            System.err.println("Engine => " + indent() + s + " -> " + res);
                    } catch (NotUnifiableException e) { /* nop */
                    }
                }
            }
            indent--;
            return res;
        }

        /*
         * (non-Javadoc)
         *
         * @see trs.TermWalker#walk(trs.Variable)
         */
        public Object walk(Variable v) {
            countRewrite++;
            Set set = rs.getRulesFor(null);
            if (set == null)
                return v;
            Iterator it = set.iterator();
            while (it.hasNext()) {
                if (it.hasNext()) {
                    Rule r = (Rule) it.next();
                    if (r != null) {
                        countRules++;
                        return r.getRight();
                    }
                }
            }
            return v;
        }

        /**
         * @return
         */
        public Map getMap() {
            return map;
        }

    }

    /* tracing flag */
    private boolean debug;

    /**
     * Construct ?an Engine instance from given RewriteSystem object.
     *
     * @param rs a RewriteSystem object
     */
    public Engine(RewriteSystem rs) {
        this(rs, false);
    }

    /**
     * Constructor Engine.
     *
     * @param rs
     * @param debug
     */
    public Engine(RewriteSystem rs, boolean debug) {
        this.rs = rs;
        this.debug = debug;
    }

    /**
     * Applies this Engine rewrite rules to the given Term object and returns the
     * constructed Term
     *
     * @param t a Term object which must be member of the Algebra generated by the
     *          Signature
     * @return a new Term produced by application of rules to the given Term
     * @throws RewriteException if the production process cannot be pursued.
     */
    public Term rewrite(Term t) throws RewriteException {
        Map fv = new HashMap();
        Term cur = fresh(fv, t);
        Term old = cur;
        Rewriter rew = new Rewriter();
        startTime = new Date().getTime();
        countRewrite = 0;
        countRules = 0;
        do {
            loops++;
            old = cur;
            try {
                cur = (Term) cur.walk(rew);
            } catch (TermWalkerException ex) {
                System.err.println(ex.getMessage());
                ex.printStackTrace();
            }
            if (debug)
                System.err.println("Engine => " + old + " -> " + cur);
        } while (!cur.equals(old));
        Map inv = reverse(fv);
        /* associate initial variables with their values in map */
        Iterator it = fv.entrySet().iterator();
        Map rewmap = rew.getMap();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            entry.setValue(substitute((Term) entry.getValue(), rewmap));
        }
        endTime = new Date().getTime();
        if (debug)
            System.err.println("Engine => mapping " + fv);
        return substitute(cur, inv);
    }

    /*
     * rewrite the given term using the given map as initial substitution and
     * storing further substitutions in m
     */
    private Term rewriteAndMap(Map fv, Term t) {
        Map fr = new HashMap();
        /* apply substitution to t */
        Term cur = fresh(fr, substitute(t, fv));
        Term old = cur;
        Rewriter rew = new Rewriter();
        startTime = new Date().getTime();
        countRewrite = 0;
        countRules = 0;
        do {
            loops++;
            old = cur;
            try {

                cur = (Term) cur.walk(rew);
            } catch (TermWalkerException tex) {
                tex.printStackTrace();
            }
            if (debug)
                System.err.println("Engine => " + old + " -> " + cur);
        } while (!cur.equals(old));
        /* associate initial variables with their values in map */
        /* first reverse initial fresh variables map */
        Map inv = reverse(fr);
        Iterator it = fr.entrySet().iterator();
        /* compute transitive (??) closure of mapping in rewrite engine */
        Map rewmap = rew.getMap();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            entry.setValue(substitute((Term) entry.getValue(), rewmap));
        }
        endTime = new Date().getTime();
        if (debug)
            System.err.println("Engine => mapping " + fv);
        return substitute(cur, inv);
    }

    /**
     * Returns the inverse map from the given map. This method checks that fv is
     * bijective.
     *
     * @param fv
     * @return
     */
    private Map reverse(Map fv) {
        Map m = new HashMap();
        if (fv == null)
            return m; /* empty map */
        Iterator it = fv.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            if (m.get(entry.getValue()) != null)
                return null;
            m.put(entry.getValue(), entry.getKey());
        }
        return m;
    }

    /**
     * @param t
     * @return
     */
    private Term fresh(Map m, Term t) {
        Set vars = t.getVariables();
        if (vars.isEmpty())
            return t;
        /* create substituion map */
        Iterator it = vars.iterator();
        while (it.hasNext())
            m.put(it.next(), new VariableImpl(rs.getSignature()));
        /* apply substitution */
        return substitute(t, m);
    }

    /**
     * Applies given substitution to the given Term. A new Term is constructed
     * with all variables from <code>m</code> replaced by their image.
     *
     * @param term the Term to apply substitution to
     * @param m    a - possibly empty but not null - Map object
     * @return a new Term
     */
    private Term substitute(Term term, Map m) {
        try {
            return (Term) term.walk(new Substitution(this.rs, m));
        } catch (TermWalkerException tex) {
            return null;
        }
    }

    /**
     * Method setDebug.
     *
     * @param b
     */
    public void setDebug(boolean b) {
        this.debug = b;
    }

    /**
     * Return the number of rewriting calls
     *
     * @return an int
     */
    public int getNumberOfRewrites() {
        return countRewrite;
    }

    public int getNumberOfRules() {
        return countRules;
    }

    /**
     * return the duration of the last rewrite in seconds
     *
     * @return a double value
     */
    public double getDuration() {
        return ((double) (endTime - startTime)) / 1000.0;
    }

    /**
     * @return
     */
    public int getLoops() {
        return loops;
    }

    /**
     * This method computes the common normal form of two terms, if it exists. If
     * it does not exists, this method returns null.
     *
     * @param term
     * @param term2
     */
    public Map equalize(Term term, Term term2) {
        /* reduce first term */
        Map m1 = new HashMap();
        Term t1 = rewriteAndMap(m1, term);
        if (debug)
            System.err.println("Engine => first term map =" + m1);
        /* reduce second term */
        Map m2 = new HashMap();
        Term t2 = rewriteAndMap(m2, term2);
        if (debug)
            System.err.println("Engine => second term map =" + m2);
        /* unify maps */
        Map m3 = unifyMaps(m1, m2);
        /* unify terms */
        try {
            new Unification(m3, rs).unify(t1, t2);
            if (debug)
                System.err.println("Engine => unify map =" + m3);
            return m3;
        } catch (NotUnifiableException e) {
            return null;
        }
    }

    /**
     * This method tries to compute the unification of two maps by unifying their
     * common substitutions.
     *
     * @param m1
     * @param m2
     * @return
     */
    private Map unifyMaps(Map m1, Map m2) {
        Map res = new HashMap();
        Iterator it = m1.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            /* is the variable common ? */
            Term t2 = (Term) m2.get(entry.getKey());
            if (t2 != null) {
                /* try to unify the two terms */
                Term s3 = ((Term) entry.getValue());
                try {
                    s3 = new Unification(res, rs).unify(s3, t2);
                    /* store new substitution in res */
                    res.put(entry.getKey(), s3);
                } catch (NotUnifiableException e) {
                    return null;
                }
            } else
                res.put(entry.getKey(), entry.getValue());
        }
        /* add second map entries */
        it = m2.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            /* is the variable common ? */
            Term t2 = (Term) m1.get(entry.getKey());
            if (t2 == null)
                res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}