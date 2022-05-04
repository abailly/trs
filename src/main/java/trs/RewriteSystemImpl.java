/*
 * Created on Apr 28, 2003
 *

 */
package trs;

import java.util.*;

/**
 * @author bailly
 * @version $Id: RewriteSystemImpl.java,v 1.7 2004/02/09 20:52:43 nono Exp $
 */
class RewriteSystemImpl implements RewriteSystem {

    private SignatureImpl sig;

    private final Map rules;

    private int rulesId;

    /**
     *
     */
    RewriteSystemImpl() {
        this.rules = new HashMap();
        this.sig = new SignatureImpl();
        this.rulesId = 0;
    }

    /*
     * (non-Javadoc)
     *
     * @see trs.RewriteSystem#getSignature()
     */
    public Signature getSignature() {
        return sig;
    }

    /*
     * (non-Javadoc)
     *
     * @see trs.RewriteSystem#getRules()
     */
    public Set getRules() {
        Set s = new HashSet();
        Iterator it = rules.values().iterator();
        while (it.hasNext())
            s.addAll((Set) it.next());

        return s;
    }

    /*
     * (non-Javadoc)
     *
     * @see trs.RewriteSystem#makeRule(trs.Term, trs.Term)
     */
    public Rule makeRule(Term left, Term right) throws RewriteException {
        Set s1 = left.getVariables();
        Set s2 = right.getVariables();
        if (!left.getVariables().containsAll(right.getVariables()))
            throw new RewriteException(
                    "There are free variables in consequence of rule : " + s2 + "!in"
                            + s1);
        /* newly created rule */
        RuleImpl r = new RuleImpl(left, right);
        r.setId(rulesId++);
        /* symbol for this rule */
        Symbol sym = null;
        if (left instanceof Function) {
            sym = ((Function) left).getSymbol();
        } else if (left instanceof Symbol) {
            sym = (Symbol) left;
        }
        /* add rule to correct set */
        Set set = (Set) rules.get(sym);
        if (set == null) {
            set = new HashSet();
            rules.put(sym, set);
        }
        set.add(r);
        return r;
    }

    /**
     * @param signature
     */
    public void setSignature(Signature signature) {
        if (!(signature instanceof SignatureImpl))
            throw new RewriteException(
                    "Cannot set signature with incompatible implementation");
        sig = (SignatureImpl) signature;
    }

    /*
     * (non-Javadoc)
     *
     * @see trs.RewriteSystem#getRulesFor(trs.Symbol)
     */
    public Set getRulesFor(Symbol sym) {
        return (Set) rules.get(sym);
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        /* first append signature */
        sb.append(sig);
        sb.append("rew ");
        Iterator it = rules.values().iterator();
        while (it.hasNext()) {
            Iterator rit = ((Set) it.next()).iterator();
            while (rit.hasNext()) {
                sb.append(rit.next()).append('\n');
                if (rit.hasNext() || it.hasNext())
                    sb.append(";");
            }
        }

        return sb.toString();
    }

    /**
     * @param ri
     */
    public void removeRule(Rule ri) {
        Term left = ri.getLeft();
        /* symbol for this rule */
        Symbol sym = null;
        if (left instanceof Function) {
            sym = ((Function) left).getSymbol();
        } else if (left instanceof Symbol) {
            sym = (Symbol) left;
        }
        /* add rule to correct set */
        Set set = (Set) rules.get(sym);
        set.remove(ri);
    }

    /**
     * Compute the set of critical pairs between rules in this rewrite system and
     * a given rule. A critical pair between rule <code>s -> t</code> and
     * <code>u -> v</code> is formed if <code>s</code> or one of its sub-terms
     * s1 is unifiable with <code>u</code> on <span style="font-family:
     * symbol">t </span>. The pairof terms (x,y) such that <code>x=t</code>
     * <span style="font-family: symbol">t </span> and <code>y=s[s1/v]</code>
     * <span style="font-family: symbol">t </span> is a critical pair.
     *
     * @param r
     * @param rule
     * @return
     */
    public Set makeCriticalPairs(Rule rule) {
        Term rl = rule.getLeft();
        Set cps = new HashSet();
        Iterator it = getRules().iterator();
        while (it.hasNext()) {
            Rule r = (Rule) it.next();
            /* left expr */
            Term g = r.getLeft();
            /*
             * try unification between left of rule and non variable subterms of g
             */
            SubtermIterator sit = new SubtermIterator(g, getSignature());
            while (sit.hasNext()) {
                Term sub = sit.next();
                /* skip variable sub terms - they always unify */
                if (sub instanceof Variable)
                    continue;
                try {
                    Unification uni = new Unification(this);
                    System.out.println("Try unifying " + rl + " with " + sub);
                    uni.unify(rl, sub);
                    /* found a critical pair - reduce */
                    Substitution subst = new Substitution(this, uni);
                    System.out.println("Found unification with subst :" + subst);
                    /* skip lexically identical terms */
                    if (subst.isEmpty())
                        continue;
                    Term delta = (Term) r.getRight().walk(subst);
                    Term redex = (Term) sit.makeContextualReplacement(rl).walk(subst);
                    System.out.println("Adding critical pair (" + redex + "," + delta + ")");
                    cps.add(new EquationImpl(redex, delta));
                } catch (NotUnifiableException e) {
                    /* continue */
                } catch (TermWalkerException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return cps;
    }

}