/*
 * Created on Apr 28, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package trs;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * DEfault implemenation for Signature interface
 *
 * @author bailly
 * @version $Id: SignatureImpl.java,v 1.7 2003/05/15 20:13:30 bailly Exp $
 */
class SignatureImpl implements Signature {

    private int symId;

    private final Map syms;

    private SymbolImpl[] symTable;

    /**
     * Construct a SignatureImpl from a Set of strings
     */
    SignatureImpl() {
        this.syms = new HashMap();
        this.symId = 0;
        this.symTable = new SymbolImpl[8];
    }

    /* (non-Javadoc)
     * @see trs.Signature#iterator()
     */
    public Iterator iterator() {
        return syms.values().iterator();
    }

    /* (non-Javadoc)
     * @see trs.Signature#size()
     */
    public int size() {
        return syms.size();
    }

    /* (non-Javadoc)
     * @see trs.Signature#contains(trs.Symbol)
     */
    public boolean contains(Symbol sym) {
        return syms.containsValue(sym);
    }

    /* (non-Javadoc)
     * @see trs.Signature#getSymbol(java.lang.String)
     */
    public Symbol getSymbol(String str) {
        return (Symbol) syms.get(str);
    }


    Symbol getSymbol(int id) {
        return symTable[id];
    }

    /* (non-Javadoc)
     * @see trs.Signature#addSymbol(java.lang.String, int)
     */
    public Symbol makeSymbol(String str, int arity) throws RewriteException {
        if (syms.get(str) != null)
            throw new RewriteException(
                    "Symbol " + str + " is already part of this signature");
        SymbolImpl s = new SymbolImpl(str, arity, symId++);
        syms.put(str, s);
        /* add sym to symtable */
        addToTable(s);
        System.out.println("Adding symbol " + str + " (" + arity + ")");
        return s;
    }

    /**
     * @param s
     */
    private void addToTable(SymbolImpl s) {
        int pos = s.getId();
        if (pos >= symTable.length) {
            /* resize arrayt */
            SymbolImpl[] ntbl = new SymbolImpl[symTable.length * 2];
            System.arraycopy(symTable, 0, ntbl, 0, symTable.length);
            symTable = ntbl;
        }
        symTable[pos] = s;
    }

    /* (non-Javadoc)
     * @see trs.RewriteSystem#makeTerm(trs.Symbol, java.util.List)
     */
    public Term makeFunction(Symbol sym, List terms) throws RewriteException {
        return makeFunction(sym, (Term[]) terms.toArray(new Term[0]));
    }

    /* (non-Javadoc)
     * @see trs.Signature#makeVariable(java.lang.String)
     */
    public Variable makeVariable(String str) throws RewriteException {
        if (syms.containsKey(str))
            throw new RewriteException("Symbol " + str + " exists in this signature, cannot create variable with same name");
        return new VariableImpl(str);
    }

    /**
     * @see trs.Signature#makeFunction(Symbol, Term[])
     */
    public Term makeFunction(Symbol sym, Term[] terms)
            throws RewriteException {
        if (!syms.containsValue(sym))
            throw new RewriteException("Symbol " + sym + " not in signature");
        if (sym.arity() != terms.length)
            throw new RewriteException(
                    "Symbol "
                            + sym
                            + " as arity "
                            + sym.arity()
                            + " which is not the same as arguments list : "
                            + terms.length);
        Function f = new FunctionImpl(sym, terms);
        return f;
    }

}
