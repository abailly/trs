//### This file created by BYACC 1.8(/Java extension  1.1)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//### Please send bug reports to rjamison@lincom-asg.com
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";


//#line 7 "trs.y"
package trs;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

//#line 21 "Yyparser.java"


/**
 * Encapsulates yacc() parser functionality in a Java
 * class for quick code development
 */
public class Yyparser {

    boolean yydebug;        //do I want debug output?
    int yynerrs;            //number of errors so far
    int yyerrflag;          //was there an error?
    int yychar;             //the current working character

    //########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
    void debug(String msg) {
        if (yydebug)
            System.out.println(msg);
    }

    //########## STATE STACK ##########
    final static int YYSTACKSIZE = 500;  //maximum stack size
    int[] statestk;
  int stateptr;           //state stack
    int stateptrmax;                     //highest index of stackptr
    int statemax;                        //state when highest index reached

    //###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
    void state_push(int state) {
        if (stateptr >= YYSTACKSIZE)         //overflowed?
            return;
        statestk[++stateptr] = state;
        if (stateptr > statemax) {
            statemax = state;
            stateptrmax = stateptr;
        }
    }

    int state_pop() {
        if (stateptr < 0)                    //underflowed?
            return -1;
        return statestk[stateptr--];
    }

    void state_drop(int cnt) {
        int ptr;
        ptr = stateptr - cnt;
        if (ptr < 0)
            return;
        stateptr = ptr;
    }

    int state_peek(int relative) {
        int ptr;
        ptr = stateptr - relative;
        if (ptr < 0)
            return -1;
        return statestk[ptr];
    }

    //###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
    boolean init_stacks() {
        statestk = new int[YYSTACKSIZE];
        stateptr = -1;
        statemax = -1;
        stateptrmax = -1;
        val_init();
        return true;
    }

    //###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
    void dump_stacks(int count) {
        int i;
        System.out.println("=index==state====value=     s:" + stateptr + "  v:" + valptr);
        for (i = 0; i < count; i++)
            System.out.println(" " + i + "    " + statestk[i] + "      " + valstk[i]);
        System.out.println("======================");
    }


    //########## SEMANTIC VALUES ##########
//## **user defined:Object
    String yytext;//user variable to return contextual strings
    Object yyval; //used to return semantic vals from action routines
    Object yylval;//the 'lval' (result) I got from yylex()
    Object[] valstk;
    int valptr;

    //###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
    void val_init() {
        valstk = new Object[YYSTACKSIZE];
        yyval = null;
        yylval = null;
        valptr = -1;
    }

    void val_push(Object val) {
        if (valptr >= YYSTACKSIZE)
            return;
        valstk[++valptr] = val;
    }

    Object val_pop() {
        if (valptr < 0)
            return null;
        return valstk[valptr--];
    }

    void val_drop(int cnt) {
        int ptr;
        ptr = valptr - cnt;
        if (ptr < 0)
            return;
        valptr = ptr;
    }

    Object val_peek(int relative) {
        int ptr;
        ptr = valptr - relative;
        if (ptr < 0)
            return null;
        return valstk[ptr];
    }

    //#### end semantic value section ####
    public final static short ID = 257;
    public final static short INTEGER = 258;
    public final static short ERROR = 259;
    public final static short SYM = 260;
    public final static short LSYM = 261;
    public final static short RSYM = 262;
    public final static short LPAREN = 263;
    public final static short RPAREN = 264;
    public final static short LBRACKET = 265;
    public final static short RBRACKET = 266;
    public final static short REWRITE = 267;
    public final static short COMMA = 268;
    public final static short SEMICOLON = 269;
    public final static short QUESTION_MARK = 270;
    public final static short BANG = 271;
    public final static short EQ = 272;
    public final static short QUIT = 273;
    public final static short SIGNATURE = 274;
    public final static short RULES = 275;
    public final static short VARIABLES = 276;
    public final static short EQUATIONS = 277;
    public final static short CMD_QUIT = 278;
    public final static short CMD_LOAD = 279;
    public final static short CMD_SAVE = 280;
    public final static short CMD_CLEAR = 281;
    public final static short CMD_DEBUG = 282;
    public final static short YYERRCODE = 256;
    final static short[] yylhs = {-1,
            0, 0, 0, 0, 0, 1, 5, 5, 6, 6,
            6, 6, 7, 7, 7, 2, 2, 9, 9, 10,
            8, 8, 13, 11, 11, 14, 14, 12, 12, 12,
            15, 15, 15, 15, 16, 16, 3, 3, 4, 4,
            4, 4, 4,
    };
    final static short[] yylen = {2,
            2, 2, 2, 1, 0, 2, 4, 2, 4, 1,
            4, 1, 1, 1, 0, 2, 2, 3, 1, 4,
            3, 1, 4, 3, 0, 3, 1, 1, 1, 3,
            4, 4, 3, 3, 3, 1, 2, 4, 1, 1,
            1, 1, 1,
    };
    final static short[] yydefred = {0,
            39, 40, 41, 42, 43, 0, 4, 0, 0, 0,
            0, 1, 2, 3, 0, 0, 0, 0, 28, 0,
            0, 0, 0, 0, 0, 0, 22, 0, 19, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 14,
            13, 8, 27, 0, 0, 0, 0, 0, 0, 0,
            0, 30, 0, 0, 0, 0, 0, 0, 24, 0,
            21, 0, 18, 0, 31, 0, 32, 9, 11, 7,
            26, 0, 0, 0,
    };
    final static short[] yydgoto = {6,
            12, 13, 14, 7, 22, 23, 42, 25, 28, 29,
            26, 49, 27, 44, 19, 50,
    };
    final static short[] yysindex = {-210,
            0, 0, 0, 0, 0, -238, 0, -240, -236, -255,
            -255, 0, 0, 0, -251, -223, -240, -258, 0, -215,
            -206, -261, -208, -193, -214, -240, 0, -204, 0, -240,
            -240, -240, -211, -240, -240, -240, -192, -184, -236, 0,
            0, 0, 0, -235, -255, -232, -255, -256, -202, -257,
            -249, 0, -187, -187, -202, -188, -186, -208, 0, -180,
            0, -240, 0, -240, 0, -240, 0, 0, 0, 0,
            0, -202, -202, -202,
    };
    final static short[] yyrindex = {67,
            0, 0, 0, 0, 0, 0, 0, 0, 0, -216,
            -216, 0, 0, 0, 1, 0, 0, 73, 0, 34,
            43, 79, 83, 0, 89, 0, 0, 95, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, -216, 0, -216, 0, -222, 0,
            0, 0, 13, 25, 101, 0, 0, 83, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 52, 61, -219,
    };
    final static short[] yygindex = {0,
            0, 0, 0, 0, 0, 41, 23, 0, 0, 35,
            -9, -8, 39, 0, 0, 53,
    };
    final static int YYTABLESIZE = 378;
    final static short[] yytable = {18,
            29, 30, 34, 35, 34, 35, 65, 39, 33, 24,
            66, 31, 33, 36, 67, 64, 15, 46, 66, 16,
            20, 48, 17, 21, 34, 53, 54, 55, 34, 35,
            59, 8, 60, 10, 62, 9, 10, 30, 11, 32,
            25, 36, 12, 25, 35, 36, 25, 37, 35, 34,
            35, 23, 52, 72, 45, 73, 38, 74, 34, 35,
            20, 40, 41, 43, 47, 56, 5, 1, 2, 3,
            4, 5, 37, 57, 35, 68, 71, 69, 6, 58,
            70, 63, 15, 61, 51, 0, 0, 0, 16, 0,
            0, 0, 0, 0, 17, 0, 0, 0, 0, 0,
            38, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 29, 29, 0, 29, 0, 0, 29, 29, 29,
            29, 0, 29, 33, 29, 29, 33, 29, 0, 33,
            33, 33, 33, 0, 33, 34, 33, 33, 34, 33,
            0, 34, 34, 34, 34, 0, 34, 0, 34, 34,
            0, 34, 10, 10, 10, 0, 0, 10, 10, 0,
            10, 12, 12, 12, 0, 0, 12, 12, 0, 12,
            23, 23, 0, 0, 0, 23, 23, 0, 23, 20,
            20, 0, 0, 0, 20, 20, 5, 20, 0, 0,
            5, 5, 37, 5, 0, 0, 37, 37, 6, 37,
            0, 15, 6, 6, 0, 6, 15, 15, 16, 15,
            0, 0, 16, 16, 17, 16, 0, 0, 17, 17,
            38, 17, 0, 0, 38, 38, 0, 38,
    };
    final static short[] yycheck = {8,
            0, 11, 261, 262, 261, 262, 264, 269, 17, 265,
            268, 263, 0, 272, 264, 272, 257, 26, 268, 260,
            257, 30, 263, 260, 0, 34, 35, 36, 261, 262,
            266, 270, 268, 0, 267, 274, 275, 47, 277, 263,
            257, 264, 0, 260, 264, 268, 263, 263, 268, 261,
            262, 0, 264, 62, 269, 64, 263, 66, 261, 262,
            0, 270, 271, 257, 269, 258, 0, 278, 279, 280,
            281, 282, 0, 258, 262, 264, 257, 264, 0, 39,
            58, 47, 0, 45, 32, -1, -1, -1, 0, -1,
            -1, -1, -1, -1, 0, -1, -1, -1, -1, -1,
            0, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, 261, 262, -1, 264, -1, -1, 267, 268, 269,
            270, -1, 272, 261, 274, 275, 264, 277, -1, 267,
            268, 269, 270, -1, 272, 261, 274, 275, 264, 277,
            -1, 267, 268, 269, 270, -1, 272, -1, 274, 275,
            -1, 277, 269, 270, 271, -1, -1, 274, 275, -1,
            277, 269, 270, 271, -1, -1, 274, 275, -1, 277,
            269, 270, -1, -1, -1, 274, 275, -1, 277, 269,
            270, -1, -1, -1, 274, 275, 270, 277, -1, -1,
            274, 275, 270, 277, -1, -1, 274, 275, 270, 277,
            -1, 269, 274, 275, -1, 277, 274, 275, 270, 277,
            -1, -1, 274, 275, 270, 277, -1, -1, 274, 275,
            270, 277, -1, -1, 274, 275, -1, 277,
    };
    final static short YYFINAL = 6;
    final static short YYMAXTOKEN = 282;
    final static String[] yyname = {
            "end-of-file", null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, "ID", "INTEGER", "ERROR", "SYM", "LSYM", "RSYM", "LPAREN", "RPAREN",
            "LBRACKET", "RBRACKET", "REWRITE", "COMMA", "SEMICOLON", "QUESTION_MARK", "BANG", "EQ",
            "QUIT", "SIGNATURE", "RULES", "VARIABLES", "EQUATIONS", "CMD_QUIT", "CMD_LOAD",
            "CMD_SAVE", "CMD_CLEAR", "CMD_DEBUG",
    };
    final static String[] yyrule = {
            "$accept : rewrite",
            "rewrite : rewrite signature",
            "rewrite : rewrite rewrite_rules",
            "rewrite : rewrite questions",
            "rewrite : command",
            "rewrite :",
            "signature : SIGNATURE symbols",
            "symbols : symbols SEMICOLON symbol assoc",
            "symbols : symbol assoc",
            "symbol : ID LPAREN INTEGER RPAREN",
            "symbol : ID",
            "symbol : SYM LPAREN INTEGER RPAREN",
            "symbol : SYM",
            "assoc : BANG",
            "assoc : QUESTION_MARK",
            "assoc :",
            "rewrite_rules : RULES rules",
            "rewrite_rules : EQUATIONS equations",
            "equations : equations SEMICOLON equation",
            "equations : equation",
            "equation : variables term EQ term",
            "rules : rules SEMICOLON rule",
            "rules : rule",
            "rule : variables term REWRITE term",
            "variables : LBRACKET variable_list RBRACKET",
            "variables :",
            "variable_list : variable_list COMMA ID",
            "variable_list : ID",
            "term : function",
            "term : ID",
            "term : LPAREN term RPAREN",
            "function : ID LPAREN term_list RPAREN",
            "function : SYM LPAREN term_list RPAREN",
            "function : term LSYM term",
            "function : term RSYM term",
            "term_list : term_list COMMA term",
            "term_list : term",
            "questions : QUESTION_MARK term",
            "questions : QUESTION_MARK term EQ term",
            "command : CMD_QUIT",
            "command : CMD_LOAD",
            "command : CMD_SAVE",
            "command : CMD_CLEAR",
            "command : CMD_DEBUG",
    };

//#line 214 "trs.y"


    /**
     * the lexer we use
     */
    private Yylex lex;

    /**
     * the generated system
     */
    private RewriteSystem rs;

    /**
     * an equational theory
     */
    private EquationalTheory eqt;

    /**
     * current list of variables
     */
    private List varlist;

    /**
     * last result of rewriting
     */
    private Term result;

    /**
     * current engine
     */
    private Engine engine;

    /**
     * current command shell
     */
    private Command command;

    public Term getResult() {
        return result;
    }

    public Symbol getSymbol(String name) {
        return sig.getSymbol(name);
    }

    private Signature sig;

    public Signature getSignature() {
        return sig;
    }

    public void setSignature(Signature sig) {
        this.sig = sig;
    }

    /**
     * Returns the gerneate rewrite system
     */
    public RewriteSystem getRewriteSystem() {
        return rs;
    }

    public void setRewriteSystem(RewriteSystem rs) {
        this.rs = rs;
    }

    public EquationalTheory getEquationalTheory() {
        return eqt;
    }

    public void setEquationalTheory(EquationalTheory eqt) {
        this.eqt = eqt;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    /**
     * Method parse.
     *
     * @param is
     */
    public void parse(InputStream is) {
        lex = new Yylex(is);
        lex.parser = this;
        /* start parsing */
        this.yyparse();
    }

    /**
     * Method parse.
     *
     * @param rd
     */
    public void parse(Reader rd) {
        lex = new Yylex(rd);
        lex.parser = this;
        /* start parsing */
        this.yyparse();
    }

    public int yylex() {
        try {
            return lex.yylex();
        } catch (IOException ioex) {
            ioex.printStackTrace();
            return -1;
        }
    }


    public void yyerror(String msg) {
        System.err.println(msg);
    }


    //#line 447 "Yyparser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
    void yylexdebug(int state, int ch) {
        String s = null;
        if (ch < 0) ch = 0;
        if (ch <= YYMAXTOKEN) //check index bounds
            s = yyname[ch];    //now get it
        if (s == null)
            s = "illegal-symbol";
        debug("state " + state + ", reading " + ch + " (" + s + ")");
    }


    //The following are now global, to aid in error reporting
    int yyn;       //next next thing to do
    int yym;       //
    int yystate;   //current parsing state from state table
    String yys;    //current token string


    //###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
    int yyparse() {
        boolean doaction;
        init_stacks();
        yynerrs = 0;
        yyerrflag = 0;
        yychar = -1;          //impossible char forces a read
        yystate = 0;            //initial state
        state_push(yystate);  //save it
        while (true) //until parsing is done, either correctly, or w/error
        {
            doaction = true;
            if (yydebug) debug("loop");
            //#### NEXT ACTION (from reduction table)
            for (yyn = yydefred[yystate]; yyn == 0; yyn = yydefred[yystate]) {
                if (yydebug) debug("yyn:" + yyn + "  state:" + yystate + "  yychar:" + yychar);
                if (yychar < 0)      //we want a char?
                {
                    yychar = yylex();  //get next token
                    if (yydebug) debug(" next yychar:" + yychar);
                    //#### ERROR CHECK ####
                    if (yychar < 0)    //it it didn't work/error
                    {
                        yychar = 0;      //change it to default string (no -1!)
                        if (yydebug)
                            yylexdebug(yystate, yychar);
                    }
                }//yychar<0
                yyn = yysindex[yystate];  //get amount to shift by (shift index)
                if ((yyn != 0) && (yyn += yychar) >= 0 &&
                        yyn <= YYTABLESIZE && yycheck[yyn] == yychar) {
                    if (yydebug)
                        debug("state " + yystate + ", shifting to state " + yytable[yyn]);
                    //#### NEXT STATE ####
                    yystate = yytable[yyn];//we are in a new state
                    state_push(yystate);   //save it
                    val_push(yylval);      //push our lval as the input for next rule
                    yychar = -1;           //since we have 'eaten' a token, say we need another
                    if (yyerrflag > 0)     //have we recovered an error?
                        --yyerrflag;        //give ourselves credit
                    doaction = false;        //but don't process yet
                    break;   //quit the yyn=0 loop
                }

                yyn = yyrindex[yystate];  //reduce
                if ((yyn != 0) && (yyn += yychar) >= 0 &&
                        yyn <= YYTABLESIZE && yycheck[yyn] == yychar) {   //we reduced!
                    if (yydebug) debug("reduce");
                    yyn = yytable[yyn];
                    doaction = true; //get ready to execute
                    break;         //drop down to actions
                } else //ERROR RECOVERY
                {
                    if (yyerrflag == 0) {
                        yyerror("syntax error");
                        yynerrs++;
                    }
                    if (yyerrflag < 3) //low error count?
                    {
                        yyerrflag = 3;
                        while (true)   //do until break
                        {
                            if (stateptr < 0)   //check for under & overflow here
                            {
                                yyerror("stack underflow. aborting...");  //note lower case 's'
                                return 1;
                            }
                            yyn = yysindex[state_peek(0)];
                            if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE) {
                                if (yydebug)
                                    debug("state " + state_peek(0) + ", error recovery shifting to state " + yytable[yyn] + " ");
                                yystate = yytable[yyn];
                                state_push(yystate);
                                val_push(yylval);
                                doaction = false;
                                break;
                            } else {
                                if (yydebug)
                                    debug("error recovery discarding state " + state_peek(0) + " ");
                                if (stateptr < 0)   //check for under & overflow here
                                {
                                    yyerror("Stack underflow. aborting...");  //capital 'S'
                                    return 1;
                                }
                                state_pop();
                                val_pop();
                            }
                        }
                    } else            //discard this token
                    {
                        if (yychar == 0)
                            return 1; //yyabort
                        if (yydebug) {
                            yys = null;
                            if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
                            if (yys == null) yys = "illegal-symbol";
                            debug("state " + yystate + ", error recovery discards token " + yychar + " (" + yys + ")");
                        }
                        yychar = -1;  //read another
                    }
                }//end error recovery
            }//yyn=0 loop
            if (!doaction)   //any reason not to proceed?
                continue;      //skip action
            yym = yylen[yyn];          //get count of terminals on rhs
            if (yydebug)
                debug("state " + yystate + ", reducing " + yym + " by rule " + yyn + " (" + yyrule[yyn] + ")");
            if (yym > 0)                 //if count of rhs not 'nil'
                yyval = val_peek(yym - 1); //get current semantic value
            switch (yyn) {
//########## USER-SUPPLIED ACTIONS ##########
                case 6:
//#line 35 "trs.y"
                {
                    yyval = null;
                }
                break;
                case 7:
//#line 37 "trs.y"
                {
                    /* sets associativity of the symbol */
                    if (val_peek(0) != null)
                        ((Symbol) val_peek(1)).setAssociativity(((Integer) val_peek(0)).intValue());
                    yyval = val_peek(1);
                }
                break;
                case 8:
//#line 44 "trs.y"
                {
                    /* sets associativity of the symbol */
                    if (val_peek(0) != null) {
                        ((Symbol) val_peek(1)).setAssociativity(((Integer) val_peek(0)).intValue());
                    }
                    yyval = val_peek(1);
                }
                break;
                case 9:
//#line 53 "trs.y"
                {
                    Symbol sym = sig.makeSymbol((String) val_peek(3), ((Integer) val_peek(1)).intValue());
                    yyval = sym;
                }
                break;
                case 10:
//#line 58 "trs.y"
                {
                    Symbol sym = sig.makeSymbol((String) val_peek(0), 0);
                    yyval = sym;
                }
                break;
                case 11:
//#line 63 "trs.y"
                {
                    Symbol sym = sig.makeSymbol((String) val_peek(3), ((Integer) val_peek(1)).intValue());
                    yyval = sym;
                }
                break;
                case 12:
//#line 68 "trs.y"
                {
                    Symbol sym = sig.makeSymbol((String) val_peek(0), 2);
                    sym.setAssociativity(Signature.LEFT_ASSOC);
                    yyval = sym;
                }
                break;
                case 13:
//#line 76 "trs.y"
                {
                    yyval = new Integer(Signature.RIGHT_ASSOC);
                }
                break;
                case 14:
//#line 77 "trs.y"
                {
                    yyval = new Integer(Signature.LEFT_ASSOC);
                }
                break;
                case 15:
//#line 78 "trs.y"
                {
                    yyval = null;
                }
                break;
                case 16:
//#line 80 "trs.y"
                {
                    yyval = val_peek(0);
                }
                break;
                case 17:
//#line 81 "trs.y"
                {
                    yyval = val_peek(0);
                }
                break;
                case 18:
//#line 84 "trs.y"
                {
                    yyval = val_peek(2);
                }
                break;
                case 19:
//#line 88 "trs.y"
                {
                    yyval = val_peek(0);
                }
                break;
                case 20:
//#line 94 "trs.y"
                {
                    yyval = eqt.makeEquation((Term) val_peek(2), (Term) val_peek(0));
                    System.err.println("Adding equation " + yyval);
                }
                break;
                case 21:
//#line 101 "trs.y"
                {
                    yyval = val_peek(2);
                }
                break;
                case 22:
//#line 105 "trs.y"
                {
                    yyval = val_peek(0);
                }
                break;
                case 23:
//#line 110 "trs.y"
                {
                    yyval = rs.makeRule((Term) val_peek(2), (Term) val_peek(0));
                    System.err.println("Adding rule " + yyval);
                }
                break;
                case 24:
//#line 115 "trs.y"
                {
                    yyval = val_peek(2);
                }
                break;
                case 25:
//#line 116 "trs.y"
                {
                    yyval = null;
                }
                break;
                case 26:
//#line 119 "trs.y"
                {
                    try {
                        yyval = sig.makeVariable((String) val_peek(0));
                    } catch (RewriteException ex) {
                        yyerror("Variable cannot be the same as a symbol " + val_peek(2) + " (" + lex.line() + "," + lex.col() + ")");
                    }
                }
                break;
                case 27:
//#line 127 "trs.y"
                {
                    try {
                        yyval = sig.makeVariable((String) val_peek(0));
                    } catch (RewriteException ex) {
                        yyerror("Variable cannot be the same as a symbol " + val_peek(0) + " (" + lex.line() + "," + lex.col() + ")");
                    }
                }
                break;
                case 28:
//#line 136 "trs.y"
                {
                    yyval = val_peek(0);
                }
                break;
                case 29:
//#line 138 "trs.y"
                {
                    Symbol sym = sig.getSymbol((String) val_peek(0));
                    if (sym == null)  /* guess this is a variable */
                        try {
                            yyval = sig.makeVariable((String) val_peek(0));
                        } catch (RewriteException ex) {
                            yyerror("Unknown identifier " + val_peek(0) + " (" + lex.line() + "," + lex.col() + ")");
                        }
                    else if (sym.arity() != 0) {
                        yyerror("Identifier " + val_peek(0) + " (" + lex.line() + "," + lex.col() + ") is used with wrong arity");
                    } else {
                        yyval = sym;
                    }
                }
                break;
                case 30:
//#line 152 "trs.y"
                {
                    yyval = val_peek(1);
                }
                break;
                case 31:
//#line 157 "trs.y"
                {
                    yyval = sig.makeFunction(sig.getSymbol((String) val_peek(3)), (List) val_peek(1));
                }
                break;
                case 32:
//#line 162 "trs.y"
                {
                    yyval = sig.makeFunction(sig.getSymbol((String) val_peek(3)), (List) val_peek(1));
                    System.err.println("making function from symbol " + val_peek(3));
                }
                break;
                case 33:
//#line 177 "trs.y"
                {
                    Symbol sym = sig.getSymbol((String) val_peek(1));
                    sym.setInfix(true);
                    yyval = sig.makeFunction(sym, new Term[]{(Term) val_peek(2), (Term) val_peek(0)});
                    System.err.println("making function from symbol " + val_peek(1));
                }
                break;
                case 34:
//#line 185 "trs.y"
                {
                    Symbol sym = sig.getSymbol((String) val_peek(1));
                    sym.setInfix(true);
                    yyval = sig.makeFunction(sym, new Term[]{(Term) val_peek(2), (Term) val_peek(0)});
                    System.err.println("making function from symbol " + val_peek(1));
                }
                break;
                case 35:
//#line 200 "trs.y"
                {
                    ((List) val_peek(2)).add(val_peek(0));
                    yyval = val_peek(2);
                }
                break;
                case 36:
//#line 201 "trs.y"
                {
                    List l = new ArrayList();
                    l.add(val_peek(0));
                    yyval = l;
                }
                break;
                case 37:
//#line 203 "trs.y"
                {
                    this.command.rewrite((Term) val_peek(0));
                }
                break;
                case 38:
//#line 204 "trs.y"
                {
                    this.command.equalize((Term) val_peek(2), (Term) val_peek(0));
                }
                break;
                case 39:
//#line 206 "trs.y"
                {
                    this.command.quit();
                }
                break;
                case 40:
//#line 207 "trs.y"
                {
                    this.command.load(val_peek(0).toString());
                }
                break;
                case 41:
//#line 208 "trs.y"
                {
                    this.command.save(val_peek(0).toString());
                }
                break;
                case 42:
//#line 209 "trs.y"
                {
                    this.command.clear();
                }
                break;
                case 43:
//#line 210 "trs.y"
                {
                    this.command.debug();
                }
                break;
//#line 824 "Yyparser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
            }//switch
            //#### Now let's reduce... ####
            if (yydebug) debug("reduce");
            state_drop(yym);             //we just reduced yylen states
            yystate = state_peek(0);     //get new state
            val_drop(yym);               //corresponding value drop
            yym = yylhs[yyn];            //select next TERMINAL(on lhs)
            if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
            {
                debug("After reduction, shifting from state 0 to state " + YYFINAL + "");
                yystate = YYFINAL;         //explicitly say we're done
                state_push(YYFINAL);       //and save it
                val_push(yyval);           //also save the semantic value of parsing
                if (yychar < 0)            //we want another character?
                {
                    yychar = yylex();        //get next character
                    if (yychar < 0) yychar = 0;  //clean, if necessary
                    if (yydebug)
                        yylexdebug(yystate, yychar);
                }
                if (yychar == 0)          //Good exit (if lex returns 0 ;-)
                    break;                 //quit the loop--all DONE
            }//if yystate
            else                        //else not done yet
            {                         //get next state and push, for next yydefred[]
                yyn = yygindex[yym];      //find out where to go
                if ((yyn != 0) && (yyn += yystate) >= 0 &&
                        yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
                    yystate = yytable[yyn]; //get new state
                else
                    yystate = yydgoto[yym]; //else go to new defred
                debug("after reduction, shifting from state " + state_peek(0) + " to state " + yystate + "");
                state_push(yystate);     //going again, so push state & val...
                val_push(yyval);         //for next action
            }
        }//main loop
        return 0;//yyaccept!!
    }
//## end of method parse() ######################################


//## run() --- for Thread #######################################

    /**
     * A default run method, used for operating this parser
     * object in the background.  It is intended for extending Thread
     * or implementing Runnable.  Turn off with -Jnorun .
     */
    public void run() {
        yyparse();
    }
//## end of method run() ########################################


//## Constructors ###############################################

    /**
     * Default constructor.  Turn off with -Jnoconstruct .
     */
    public Yyparser() {
        //nothing to do
    }


    /**
     * Create a parser, setting the debug to true or false.
     *
     * @param debugMe true for debugging, false for no debug.
     */
    public Yyparser(boolean debugMe) {
        yydebug = debugMe;
    }
//###############################################################


}
//################### END OF CLASS ##############################
