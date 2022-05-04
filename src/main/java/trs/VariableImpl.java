/*
 * Created on Apr 28, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package trs;

import java.util.HashSet;
import java.util.Set;

/**
 * Default implementation for a Variable
 *
 * @author bailly
 * @version $Id: VariableImpl.java,v 1.3 2003/05/15 08:32:46 bailly Exp $
 */
class VariableImpl extends TermImpl implements Variable {

    private final String name;

    private static final Set usedNames = new HashSet();

    private static int usedIndex = 0;

    private static final String prefix = "v";

    /**
     * @param rs
     */
    VariableImpl(String name) {
        this.name = name;
        usedNames.add(name);
    }

    /**
     * Allocate a fresh variable different from any
     * other variable
     */
    VariableImpl(Signature sig) {
        String n = null;
        do {
            n = prefix + usedIndex;
        } while ((sig.getSymbol(n) != null) || usedNames.contains(n));
        this.name = n;
        usedNames.add(n);
        usedIndex++;
    }

    /**
     * @see trs.Term#walk(trs.TermWalker)
     */
    public Object walk(TermWalker tw) throws TermWalkerException {
        return tw.walk(this);
    }

    /* (non-Javadoc)
     * @see trs.Term#getVariables()
     */
    public Set getVariables() {
        Set s = new HashSet();
        s.add(this);
        return s;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return name;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof VariableImpl))
            return false;
        VariableImpl v = (VariableImpl) obj;
        return name.equals(v.name);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return name.hashCode();
    }

}
