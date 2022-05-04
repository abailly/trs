/************************************************************************
 * Lexical analyzer for Term Rewrite System grammar
 * Author : Arnaud Bailly
 * Version : $Id: Yylex,v 1.8 2003/04/25 15:22:06 bailly Exp $^
 **************************************************************************/
package trs;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


class Yylex {
    private final int YY_BUFFER_SIZE = 512;
    private final int YY_F = -1;
    private final int YY_NO_STATE = -1;
    private final int YY_NOT_ACCEPT = 0;
    private final int YY_START = 1;
    private final int YY_END = 2;
    private final int YY_NO_ANCHOR = 4;
    private final int YY_BOL = 128;
    private final int YY_EOF = 129;
    public final int YYEOF = -1;

    /* the parser object we use */
    Yyparser parser;

    public int line() {
        return yyline;
    }

    public int col() {
        return yychar;
    }

    private java.io.BufferedReader yy_reader;
    private int yy_buffer_index;
    private int yy_buffer_read;
    private int yy_buffer_start;
    private int yy_buffer_end;
    private char[] yy_buffer;
    private int yychar;
    private int yyline;
    private boolean yy_at_bol;
    private int yy_lexical_state;

    Yylex(java.io.Reader reader) {
        this();
        if (null == reader) {
            throw (new Error("Error: Bad input stream initializer."));
        }
        yy_reader = new java.io.BufferedReader(reader);
    }

    Yylex(java.io.InputStream instream) {
        this();
        if (null == instream) {
            throw (new Error("Error: Bad input stream initializer."));
        }
        yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
    }

    private Yylex() {
        yy_buffer = new char[YY_BUFFER_SIZE];
        yy_buffer_read = 0;
        yy_buffer_index = 0;
        yy_buffer_start = 0;
        yy_buffer_end = 0;
        yychar = 0;
        yyline = 0;
        yy_at_bol = true;
        yy_lexical_state = YYINITIAL;
    }

    private final boolean yy_eof_done = false;
    private final int INSIG = 2;
    private final int YYINITIAL = 0;
    private final int INCMD = 1;
    private final int[] yy_state_dtrans = {
            0,
            0,
            0
    };

    private void yybegin(int state) {
        yy_lexical_state = state;
    }

    private int yy_advance()
            throws java.io.IOException {
        int next_read;
        int i;
        int j;

        if (yy_buffer_index < yy_buffer_read) {
            return yy_buffer[yy_buffer_index++];
        }

        if (0 != yy_buffer_start) {
            i = yy_buffer_start;
            j = 0;
            while (i < yy_buffer_read) {
                yy_buffer[j] = yy_buffer[i];
                ++i;
                ++j;
            }
            yy_buffer_end = yy_buffer_end - yy_buffer_start;
            yy_buffer_start = 0;
            yy_buffer_read = j;
            yy_buffer_index = j;
            next_read = yy_reader.read(yy_buffer,
                    yy_buffer_read,
                    yy_buffer.length - yy_buffer_read);
            if (-1 == next_read) {
                return YY_EOF;
            }
            yy_buffer_read = yy_buffer_read + next_read;
        }

        while (yy_buffer_index >= yy_buffer_read) {
            if (yy_buffer_index >= yy_buffer.length) {
                yy_buffer = yy_double(yy_buffer);
            }
            next_read = yy_reader.read(yy_buffer,
                    yy_buffer_read,
                    yy_buffer.length - yy_buffer_read);
            if (-1 == next_read) {
                return YY_EOF;
            }
            yy_buffer_read = yy_buffer_read + next_read;
        }
        return yy_buffer[yy_buffer_index++];
    }

    private void yy_move_end() {
        if (yy_buffer_end > yy_buffer_start &&
                '\n' == yy_buffer[yy_buffer_end - 1])
            yy_buffer_end--;
        if (yy_buffer_end > yy_buffer_start &&
                '\r' == yy_buffer[yy_buffer_end - 1])
            yy_buffer_end--;
    }

    private boolean yy_last_was_cr = false;

    private void yy_mark_start() {
        int i;
        for (i = yy_buffer_start; i < yy_buffer_index; ++i) {
            if ('\n' == yy_buffer[i] && !yy_last_was_cr) {
                ++yyline;
            }
            if ('\r' == yy_buffer[i]) {
                ++yyline;
                yy_last_was_cr = true;
            } else yy_last_was_cr = false;
        }
        yychar = yychar
                + yy_buffer_index - yy_buffer_start;
        yy_buffer_start = yy_buffer_index;
    }

    private void yy_mark_end() {
        yy_buffer_end = yy_buffer_index;
    }

    private void yy_to_mark() {
        yy_buffer_index = yy_buffer_end;
        yy_at_bol = (yy_buffer_end > yy_buffer_start) &&
                ('\r' == yy_buffer[yy_buffer_end - 1] ||
                        '\n' == yy_buffer[yy_buffer_end - 1] ||
                        2028/*LS*/ == yy_buffer[yy_buffer_end - 1] ||
                        2029/*PS*/ == yy_buffer[yy_buffer_end - 1]);
    }

    private java.lang.String yytext() {
        return (new java.lang.String(yy_buffer,
                yy_buffer_start,
                yy_buffer_end - yy_buffer_start));
    }

    private int yylength() {
        return yy_buffer_end - yy_buffer_start;
    }

    private char[] yy_double(char[] buf) {
        int i;
        char[] newbuf;
        newbuf = new char[2 * buf.length];
        for (i = 0; i < buf.length; ++i) {
            newbuf[i] = buf[i];
        }
        return newbuf;
    }

    private final int YY_E_INTERNAL = 0;
    private final int YY_E_MATCH = 1;
    private final java.lang.String[] yy_error_string = {
            "Error: Internal error.\n",
            "Error: Unmatched input.\n"
    };

    private void yy_error(int code, boolean fatal) {
        java.lang.System.out.print(yy_error_string[code]);
        java.lang.System.out.flush();
        if (fatal) {
            throw new Error("Fatal Error.\n");
        }
    }

    private int[][] unpackFromString(int size1, int size2, String st) {
        int colonIndex = -1;
        String lengthString;
        int sequenceLength = 0;
        int sequenceInteger = 0;

        int commaIndex;
        String workString;

        int[][] res = new int[size1][size2];
        for (int i = 0; i < size1; i++) {
            for (int j = 0; j < size2; j++) {
                if (sequenceLength != 0) {
                    res[i][j] = sequenceInteger;
                    sequenceLength--;
                    continue;
                }
                commaIndex = st.indexOf(',');
                workString = (commaIndex == -1) ? st :
                        st.substring(0, commaIndex);
                st = st.substring(commaIndex + 1);
                colonIndex = workString.indexOf(':');
                if (colonIndex == -1) {
                    res[i][j] = Integer.parseInt(workString);
                    continue;
                }
                lengthString =
                        workString.substring(colonIndex + 1);
                sequenceLength = Integer.parseInt(lengthString);
                workString = workString.substring(0, colonIndex);
                sequenceInteger = Integer.parseInt(workString);
                res[i][j] = sequenceInteger;
                sequenceLength--;
            }
        }
        return res;
    }

    private final int[] yy_acpt = {
            /* 0 */ YY_NOT_ACCEPT,
            /* 1 */ YY_NO_ANCHOR,
            /* 2 */ YY_NO_ANCHOR,
            /* 3 */ YY_NO_ANCHOR,
            /* 4 */ YY_NO_ANCHOR,
            /* 5 */ YY_NO_ANCHOR,
            /* 6 */ YY_NO_ANCHOR,
            /* 7 */ YY_NO_ANCHOR,
            /* 8 */ YY_NO_ANCHOR,
            /* 9 */ YY_NO_ANCHOR,
            /* 10 */ YY_NO_ANCHOR,
            /* 11 */ YY_NO_ANCHOR,
            /* 12 */ YY_NO_ANCHOR,
            /* 13 */ YY_NO_ANCHOR,
            /* 14 */ YY_NO_ANCHOR,
            /* 15 */ YY_NO_ANCHOR,
            /* 16 */ YY_NO_ANCHOR,
            /* 17 */ YY_NO_ANCHOR,
            /* 18 */ YY_NO_ANCHOR,
            /* 19 */ YY_NO_ANCHOR,
            /* 20 */ YY_NO_ANCHOR,
            /* 21 */ YY_NO_ANCHOR,
            /* 22 */ YY_NO_ANCHOR,
            /* 23 */ YY_NO_ANCHOR,
            /* 24 */ YY_NO_ANCHOR,
            /* 25 */ YY_NO_ANCHOR,
            /* 26 */ YY_NOT_ACCEPT,
            /* 27 */ YY_NO_ANCHOR,
            /* 28 */ YY_NO_ANCHOR,
            /* 29 */ YY_NO_ANCHOR,
            /* 30 */ YY_NO_ANCHOR,
            /* 31 */ YY_NO_ANCHOR,
            /* 32 */ YY_NO_ANCHOR,
            /* 33 */ YY_NO_ANCHOR,
            /* 34 */ YY_NOT_ACCEPT,
            /* 35 */ YY_NO_ANCHOR,
            /* 36 */ YY_NO_ANCHOR,
            /* 37 */ YY_NOT_ACCEPT,
            /* 38 */ YY_NO_ANCHOR,
            /* 39 */ YY_NOT_ACCEPT,
            /* 40 */ YY_NO_ANCHOR,
            /* 41 */ YY_NO_ANCHOR,
            /* 42 */ YY_NO_ANCHOR,
            /* 43 */ YY_NO_ANCHOR,
            /* 44 */ YY_NO_ANCHOR,
            /* 45 */ YY_NO_ANCHOR
    };
    private final int[] yy_cmap = unpackFromString(1, 130,
            "39:8,40:2,20,39,40,21,39:18,28,22,29,38,29:3,27,15,16,29:2,13,10,19,29,30,3" +
                    "3:7,26:2,29,12,29,23,11,14,29,36:6,25:8,32,25:8,35,25:2,17,29,18,29,39:2,9," +
                    "37:3,5,37,3,24,2,24:5,31,24,7,4,1,24:2,8,6,34,24:2,39,29,39,29,39,0:2")[0];

    private final int[] yy_rmap = unpackFromString(1, 46,
            "0,1,2,3,1:2,4,1:4,5,6,7,1,8,4,1,9,1,10:4,1,11,12,13,4,1,14,15,16,17,11,10,1" +
                    "8,19,20,16,21,22,10,23,24,25")[0];

    private final int[][] yy_nxt = unpackFromString(26, 41,
            "1,2,42:2,43,44,42:2,45,42,3,28,4,5,6,7,8,9,10,11,12:2,13,14,42:2,15,29,12,2" +
                    "8,30,42:2,15,42:4,36,29,12,-1:42,42,27,42:7,-1:14,42,35:3,-1:2,35,42,35:2,4" +
                    "2,35:2,42,-1:13,28,16,-1:2,28,-1:14,28,-1:8,28,-1:12,28:2,-1:2,28,-1:14,28," +
                    "-1:8,28,-1:22,17,31,-1:39,12:2,-1:6,12,-1:11,12,-1,18:9,-1:14,18:4,-1:2,18:" +
                    "8,-1:29,15,-1:3,15,-1:2,15,-1:35,39,-1:13,42:9,-1:14,42,35:3,-1:2,35,42,35:" +
                    "2,42,35:2,42,-1:8,25,-1:3,25,-1:16,25,-1:3,25,-1:2,25,-1:2,25:2,-1:33,24,-1" +
                    ":2,24,-1:8,42:2,20,42:6,-1:14,42,35:3,-1:2,35,42,35:2,42,35:2,42,-1:29,15,-" +
                    "1:3,15,26:2,15,34:2,-1:25,17,-1:21,32:16,-1:2,32,-1:2,32:6,39,32:10,-1:22,1" +
                    "9,-1:21,37:9,36:2,37:2,36,37:5,19,33,37:7,36,37:8,36,37:2,-1,37:19,19,33,37" +
                    ":19,-1,42:5,21,42:3,-1:14,42,35:3,-1:2,35,42,35:2,42,35:2,42,-1:4,22,42:8,-" +
                    "1:14,42,35:3,-1:2,35,42,35:2,42,35:2,42,-1:4,42:3,23,42:5,-1:14,42,35:3,-1:" +
                    "2,35,42,35:2,42,35:2,42,-1:4,42:4,38,42:4,-1:14,42,35:3,-1:2,35,42,35:2,42," +
                    "35:2,42,-1:4,42:6,40,42:2,-1:14,42,35:3,-1:2,35,42,35:2,42,35:2,42,-1:4,42:" +
                    "8,41,-1:14,42,35:3,-1:2,35,42,35:2,42,35:2,42,-1:3");

    public int yylex()
            throws java.io.IOException {
        int yy_lookahead;
        int yy_anchor = YY_NO_ANCHOR;
        int yy_state = yy_state_dtrans[yy_lexical_state];
        int yy_next_state = YY_NO_STATE;
        int yy_last_accept_state = YY_NO_STATE;
        boolean yy_initial = true;
        int yy_this_accept;

        yy_mark_start();
        yy_this_accept = yy_acpt[yy_state];
        if (YY_NOT_ACCEPT != yy_this_accept) {
            yy_last_accept_state = yy_state;
            yy_mark_end();
        }
        while (true) {
            if (yy_initial && yy_at_bol) yy_lookahead = YY_BOL;
            else yy_lookahead = yy_advance();
            yy_next_state = YY_F;
            yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
            if (YY_EOF == yy_lookahead && true == yy_initial) {
                return YYEOF;
            }
            if (YY_F != yy_next_state) {
                yy_state = yy_next_state;
                yy_initial = false;
                yy_this_accept = yy_acpt[yy_state];
                if (YY_NOT_ACCEPT != yy_this_accept) {
                    yy_last_accept_state = yy_state;
                    yy_mark_end();
                }
            } else {
                if (YY_NO_STATE == yy_last_accept_state) {
                    throw (new Error("Lexical Error: Unmatched Input."));
                } else {
                    yy_anchor = yy_acpt[yy_last_accept_state];
                    if (0 != (YY_END & yy_anchor)) {
                        yy_move_end();
                    }
                    yy_to_mark();
                    switch (yy_last_accept_state) {
                        case 1:

                        case -2:
                            break;
                        case 2: {
                            parser.yylval = yytext();
                            return Yyparser.ID;
                        }
                        case -3:
                            break;
                        case 3: {
                            /* try to find assoc of symbol in parser */
                            Symbol sym = parser.getSymbol(yytext());
                            System.err.println("Reading symbol " + yytext());
                            parser.yylval = yytext();
                            if (sym != null) { /* symbol registered in signature, return its associativity */
                                switch (sym.getAssociativity()) {
                                    case Signature.RIGHT_ASSOC:
                                        return Yyparser.RSYM;
                                    case Signature.LEFT_ASSOC:
                                        return Yyparser.LSYM;
                                    default:
                                        return Yyparser.SYM;
                                }
                            } else return Yyparser.SYM; /* non associative operator */
                        }
                        case -4:
                            break;
                        case 4: {
                            return Yyparser.SEMICOLON;
                        }
                        case -5:
                            break;
                        case 5: {
                            return Yyparser.COMMA;
                        }
                        case -6:
                            break;
                        case 6: {
                            return Yyparser.QUESTION_MARK;
                        }
                        case -7:
                            break;
                        case 7: {
                            return Yyparser.LPAREN;
                        }
                        case -8:
                            break;
                        case 8: {
                            return Yyparser.RPAREN;
                        }
                        case -9:
                            break;
                        case 9: {
                            return Yyparser.LBRACKET;
                        }
                        case -10:
                            break;
                        case 10: {
                            return Yyparser.RBRACKET;
                        }
                        case -11:
                            break;
                        case 11: {
                            System.err.println("Illegal input to Yylex, exiting");
                            yy_error(0, true);
                        }
                        case -12:
                            break;
                        case 12: {
                            return yylex();
                        }
                        case -13:
                            break;
                        case 13: {
                            return Yyparser.BANG;
                        }
                        case -14:
                            break;
                        case 14: {
                            return Yyparser.EQ;
                        }
                        case -15:
                            break;
                        case 15: {
                            parser.yylval = new Integer(Integer.parseInt(yytext()));
                            return Yyparser.INTEGER;
                        }
                        case -16:
                            break;
                        case 16: {
                            return Yyparser.REWRITE;
                        }
                        case -17:
                            break;
                        case 17: {
                            return YYEOF;
                        }
                        case -18:
                            break;
                        case 18: {
                            char cmd = yytext().charAt(1);
                            /* parse arguments and create array */
                            String[] args = null;
                            if (yytext().length() > 2) {
                                List l = new ArrayList();
                                StringTokenizer st = new StringTokenizer(yytext().substring(2), " ");
                                while (st.hasMoreTokens())
                                    l.add(st.nextToken());
                                System.err.println(l);
                                args = (String[]) l.toArray(new String[0]);
                            }
                            /* return command */
                            switch (cmd) {
                                case 'q':
                                    return Yyparser.CMD_QUIT;
                                case 'l':
                                    if (args == null)
                                        return -1;
                                    parser.yylval = args[0];
                                    return Yyparser.CMD_LOAD;
                                case 's':
                                    if (args == null)
                                        return -1;
                                    parser.yylval = args[0];
                                    return Yyparser.CMD_SAVE;
                                case 'c':
                                    return Yyparser.CMD_CLEAR;
                                case 'd':
                                    return Yyparser.CMD_DEBUG;
                                default:
                                    return -1;
                            }
                        }
                        case -19:
                            break;
                        case 19: {
                            return yylex();
                        }
                        case -20:
                            break;
                        case 20: {
                            return Yyparser.SIGNATURE;
                        }
                        case -21:
                            break;
                        case 21: {
                            return Yyparser.RULES;
                        }
                        case -22:
                            break;
                        case 22: {
                            return Yyparser.EQUATIONS;
                        }
                        case -23:
                            break;
                        case 23: {
                            return Yyparser.VARIABLES;
                        }
                        case -24:
                            break;
                        case 24: {
                            parser.yylval = new Integer(Integer.parseInt(yytext(), 8));
                            return Yyparser.INTEGER;
                        }
                        case -25:
                            break;
                        case 25: {
                            parser.yylval = new Integer(Integer.parseInt(yytext().substring(2), 16));
                            return Yyparser.INTEGER;
                        }
                        case -26:
                            break;
                        case 27: {
                            parser.yylval = yytext();
                            return Yyparser.ID;
                        }
                        case -27:
                            break;
                        case 28: {
                            /* try to find assoc of symbol in parser */
                            Symbol sym = parser.getSymbol(yytext());
                            System.err.println("Reading symbol " + yytext());
                            parser.yylval = yytext();
                            if (sym != null) { /* symbol registered in signature, return its associativity */
                                switch (sym.getAssociativity()) {
                                    case Signature.RIGHT_ASSOC:
                                        return Yyparser.RSYM;
                                    case Signature.LEFT_ASSOC:
                                        return Yyparser.LSYM;
                                    default:
                                        return Yyparser.SYM;
                                }
                            } else return Yyparser.SYM; /* non associative operator */
                        }
                        case -28:
                            break;
                        case 29: {
                            System.err.println("Illegal input to Yylex, exiting");
                            yy_error(0, true);
                        }
                        case -29:
                            break;
                        case 30: {
                            parser.yylval = new Integer(Integer.parseInt(yytext()));
                            return Yyparser.INTEGER;
                        }
                        case -30:
                            break;
                        case 31: {
                            return YYEOF;
                        }
                        case -31:
                            break;
                        case 32: {
                            char cmd = yytext().charAt(1);
                            /* parse arguments and create array */
                            String[] args = null;
                            if (yytext().length() > 2) {
                                List l = new ArrayList();
                                StringTokenizer st = new StringTokenizer(yytext().substring(2), " ");
                                while (st.hasMoreTokens())
                                    l.add(st.nextToken());
                                System.err.println(l);
                                args = (String[]) l.toArray(new String[0]);
                            }
                            /* return command */
                            switch (cmd) {
                                case 'q':
                                    return Yyparser.CMD_QUIT;
                                case 'l':
                                    if (args == null)
                                        return -1;
                                    parser.yylval = args[0];
                                    return Yyparser.CMD_LOAD;
                                case 's':
                                    if (args == null)
                                        return -1;
                                    parser.yylval = args[0];
                                    return Yyparser.CMD_SAVE;
                                case 'c':
                                    return Yyparser.CMD_CLEAR;
                                case 'd':
                                    return Yyparser.CMD_DEBUG;
                                default:
                                    return -1;
                            }
                        }
                        case -32:
                            break;
                        case 33: {
                            return yylex();
                        }
                        case -33:
                            break;
                        case 35: {
                            parser.yylval = yytext();
                            return Yyparser.ID;
                        }
                        case -34:
                            break;
                        case 36: {
                            /* try to find assoc of symbol in parser */
                            Symbol sym = parser.getSymbol(yytext());
                            System.err.println("Reading symbol " + yytext());
                            parser.yylval = yytext();
                            if (sym != null) { /* symbol registered in signature, return its associativity */
                                switch (sym.getAssociativity()) {
                                    case Signature.RIGHT_ASSOC:
                                        return Yyparser.RSYM;
                                    case Signature.LEFT_ASSOC:
                                        return Yyparser.LSYM;
                                    default:
                                        return Yyparser.SYM;
                                }
                            } else return Yyparser.SYM; /* non associative operator */
                        }
                        case -35:
                            break;
                        case 38: {
                            parser.yylval = yytext();
                            return Yyparser.ID;
                        }
                        case -36:
                            break;
                        case 40: {
                            parser.yylval = yytext();
                            return Yyparser.ID;
                        }
                        case -37:
                            break;
                        case 41: {
                            parser.yylval = yytext();
                            return Yyparser.ID;
                        }
                        case -38:
                            break;
                        case 42: {
                            parser.yylval = yytext();
                            return Yyparser.ID;
                        }
                        case -39:
                            break;
                        case 43: {
                            parser.yylval = yytext();
                            return Yyparser.ID;
                        }
                        case -40:
                            break;
                        case 44: {
                            parser.yylval = yytext();
                            return Yyparser.ID;
                        }
                        case -41:
                            break;
                        case 45: {
                            parser.yylval = yytext();
                            return Yyparser.ID;
                        }
                        case -42:
                            break;
                        default:
                            yy_error(YY_E_INTERNAL, false);
                        case -1:
                    }
                    yy_initial = true;
                    yy_state = yy_state_dtrans[yy_lexical_state];
                    yy_next_state = YY_NO_STATE;
                    yy_last_accept_state = YY_NO_STATE;
                    yy_mark_start();
                    yy_this_accept = yy_acpt[yy_state];
                    if (YY_NOT_ACCEPT != yy_this_accept) {
                        yy_last_accept_state = yy_state;
                        yy_mark_end();
                    }
                }
            }
        }
    }
}
