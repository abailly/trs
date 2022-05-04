/*
 * Created on May 15, 2003
 * Copyright 2003 Arnaud Bailly
 */
package trs;

/**
 * A class implementing a <em>lexicographic path ordering</em> on terms.
 * <p>
 * A lexicographic path ordering is an ordering between terms that, given an
 * ordered signature S, gives s &gt; t iff :
 * <ol>
 * <li>(1) t is in Var(s) and t &neq; s</li>
 * <li>or (2) s = f(s1,s2,...,sn), t = g(t1,t2,...,tm) and either
 * <ol>
 * <li>(2.1) there exists i such that si &gt; t or</li>
 * <li>(2.2) f &gt; g and s &gt; ti for all i or</li>
 * <li>(2.3) f = g, s &gt; ti for all i &lt; n and there exists j such that
 * s1=t1,s2=t2,...sj-1 = tj-1 and sj &gt; tj</li>
 * </ol>
 * </li>
 * </ol>
 * <p>
 * A lpo is a reduction order on terms and as such can be used in completion
 * algorithms and termination proof.
 *
 * @author bailly
 * @version $Id: LexicographicPathOrdering.java,v 1.2 2003/05/17 16:14:53 bailly
 * Exp $
 */
public class LexicographicPathOrdering implements TermOrdering, TermWalker {

    class GreaterThan extends RuntimeException {
    }

    class LowerThan extends RuntimeException {
    }

    private Term cur;

    /*
     * (non-Javadoc)
     *
     * @see trs.TermOrdering#compare(trs.Term, trs.Term)
     */
    public int compare(Term t1, Term t2) {
        Term old = cur;
        this.cur = t2;
        try {
            if (t1.equals(t2))
                return TermOrdering.EQ;
            else
                t1.walk(this);
            return TermOrdering.EQ;
        } catch (GreaterThan gt) {
            return TermOrdering.GT;
        } catch (LowerThan lt) {
            return TermOrdering.LT;
        } catch (TermWalkerException tex) {
            return TermOrdering.NE;
        } finally {
            cur = old;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see trs.TermWalker#walk(trs.Function)
     */
    public Object walk(Function t) throws TermWalkerException {
        /* first, compare symbols */
        Symbol sym = t.getSymbol();
        Term[] subs1 = t.getSubTerms();
        if (cur instanceof Function) {
            Function of = (Function) cur;
            Symbol osym = ((Function) cur).getSymbol();
            Term[] subs2 = of.getSubTerms();
            switch (compare(sym, osym)) {
                case TermOrdering.EQ: /* case 2.3 */
                    /* same function, we must compare sub terms */
                    int current = TermOrdering.EQ;
                    for (int i = 0; i < subs1.length; i++)
                        /* s > ti for all i */
                        if (compare(t, subs2[i]) != TermOrdering.GT)
                            throw new TermWalkerException();
                    for (int i = 0; i < subs1.length; i++) {
                        switch (compare(subs1[i], subs2[i])) {
                            case TermOrdering.EQ: /* si = ti */
                                continue;
                            case TermOrdering.GT: /* si > ti */
                                throw new GreaterThan();
                            case TermOrdering.LT: /* si < ti */
                                throw new LowerThan();
                            default: /* uncomparable */
                                throw new TermWalkerException();
                        }
                    }
                    return new Object();
                case TermOrdering.GT: /* case 2.2 */
                    for (int i = 0; i < subs2.length; i++)
                        /* s > ti for all i */
                        if (compare(t, subs2[i]) != TermOrdering.GT)
                            throw new TermWalkerException();
                    throw new GreaterThan();
                case TermOrdering.LT:
                    /* case 2.2 with s and t swapped */
                    for (int i = 0; i < subs1.length; i++)
                        /* t > si for all i */
                        if (compare(cur, subs1[i]) != TermOrdering.GT)
                            throw new TermWalkerException();
                    throw new LowerThan();
                default: /* case 2.1 */
                    int i = 0;
                    for (; i < subs1.length && i < subs2.length; i++)
                        /* si > t */
                        if (compare(subs1[i], of) == TermOrdering.GT)
                            throw new GreaterThan();
                        else if (compare(subs2[i], t) == TermOrdering.GT)
                            throw new LowerThan();
                    /* if subs1.length != subs2.length, we finish the job */
                    if (i == subs1.length) {
                        for (; i < subs2.length; i++)
                            /* ti > s */
                            if (compare(subs2[i], t) == TermOrdering.GT)
                                throw new LowerThan();
                    } else if (i == subs2.length) {
                        for (; i < subs1.length; i++)
                            /* ti > s */
                            if (compare(subs1[i], of) == TermOrdering.GT)
                                throw new GreaterThan();
                    }
                    throw new TermWalkerException(); /* not comparable */
            }

        } else if (cur instanceof Variable) { /* case 1 */
            throw new GreaterThan();
        } else if (cur instanceof Symbol) { /* cur is a symbol */
            switch (compare(t.getSymbol(), cur)) {
                case TermOrdering.GT:
                    throw new GreaterThan();
                case TermOrdering.LT: /* case 2.2 */
                    for (int i = 0; i < subs1.length; i++)
                        /* s > ti for all i */
                        if (compare(cur, subs1[i]) != TermOrdering.GT)
                            throw new TermWalkerException();
                    throw new LowerThan();
                default:
                    throw new TermWalkerException();
            }

        }
        throw new TermWalkerException();

    }

    /*
     * (non-Javadoc)
     *
     * @see trs.TermWalker#walk(trs.Symbol)
     */
    public Object walk(Symbol s) throws TermWalkerException {
        if (cur instanceof Symbol) {
            switch (s.compareTo(cur)) {
                case -1:
                    throw new LowerThan();
                case 1:
                    throw new GreaterThan();
                case 0: // ???
                    return new Object();
            }
        } else if (cur instanceof Function) { /* case 2 */
            Function f = (Function) cur;
            Term[] subs1 = f.getSubTerms();
            switch (compare(s, f.getSymbol())) {
                case TermOrdering.GT:
                    for (int i = 0; i < subs1.length; i++)
                        /* s > ti for all i */
                        if (compare(s, subs1[i]) != TermOrdering.GT)
                            throw new TermWalkerException();
                    throw new GreaterThan();
                case TermOrdering.LT: /* case 2.2 */
                    throw new LowerThan();
                default:
                    throw new TermWalkerException();
            }
        }
        throw new GreaterThan();
    }

    /*
     * (non-Javadoc)
     *
     * @see trs.TermWalker#walk(trs.Variable)
     */
    public Object walk(Variable v) throws TermWalkerException {
        if (cur instanceof Variable)
            throw new TermWalkerException();
        else
            throw new LowerThan();
    }

}