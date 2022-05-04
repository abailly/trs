package trs;

import java.io.*;

/**
 * @author bailly
 * @version $Id: Shell.java,v 1.10 2004/04/12 07:02:49 bailly Exp $
 */
public class Shell implements Command {

    final static String[] banner =
            {
                    "Term Rewrite System Shell v0.1",
                    "(c) Arnaud Bailly - 2003",
                    "type !q at prompt to exit"};

    final static String outro = "Thank you for using TRS !";

    private final Reader input;

    private final PrintStream output = System.out;

    private final Yyparser parser;

    private boolean debug;

    private Engine engine;

    /**
     * Build a new Shell with given InputStream to read command
     * from.
     *
     * @param input
     */
    public Shell(InputStream input, boolean debug) {
        this.input = new InputStreamReader(input);
        this.parser = new Yyparser(debug);
        parser.setCommand(this);
        /* init rewrite system */
        RewriteSystem rs = new RewriteSystemImpl();
        parser.setRewriteSystem(rs);
        engine = new Engine(rs, debug);
    }

    /**
     * Constructor Shell.
     */
    public Shell(boolean debug) {
        this(System.in, debug);
    }

    /**
     * Main function of Shell : this starts the command
     * line interpreter infinite loop.
     */
    public static void main(String[] argv) {
        /* create shell */
        Shell shell = null;
        boolean interactive = false;
        File file = null;
        boolean debug = false;
        InputStream is = null;
        /* parse command line */
        for (int i = 0; i < argv.length; i++)
            switch (argv[i].charAt(0)) {
                case '-': /* an option */
                    switch (argv[i].charAt(1)) {
                        case 'f': /* a file argument to load */
                            i++;

                            try {
                                file = new File(argv[i]);
                            } catch (Exception ex) {
                                System.err.println("Invalid file argument ");
                                System.exit(1);
                            }
                            break;
                        case 'd':
                            debug = true;
                            break;
                        case 'i': /* shell is interactive */
                            interactive = true;
                            break;
                        default:
                            usage();
                            System.exit(1);
                    }
                    break;
                default: /* argument is a file name to parse */
                    try {
                        file = (new File(argv[i]));
                    } catch (Exception ioex) {
                        shell.output.println(ioex.getMessage());
                    }

            }
        /* create reader */
        if (file == null)
            is = System.in;
        else
            try {
                is = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.exit(2);
            }
        /* done */
        shell = new Shell(is, debug);
        if (interactive)
            shell.start();
        else
            shell.interpret();
    }

    /**
     * Method setDebug.
     *
     * @param b
     */
    public void setDebug(boolean b) {
        this.debug = b;
        this.engine.setDebug(b);
    }

    /**
     * Method parse.
     */
    private void parse(Reader input) {
        try {
            parser.parse(input);
        } catch (Exception ex) {
            ex.printStackTrace();
            output.print(ex.getMessage());
        }
    }

    /**
     * Single run  of non interactive shell
     */
    private void interpret() {
        try {
            parser.parse(input);
        } catch (Exception ex) {
            output.print(ex.getMessage());
        }
    }


    /**
     * This method is the main loop of the term rewrite system Shell.
     * It allows user to enter rules, signatures, queries and commands.
     */
    private void start() {
        /* print banner */
        for (int i = 0; i < banner.length; i++)
            output.println(banner[i]);
        /* start shell */
        for (; ; ) {
            output.print("> ");
            /* parse command */
            try {
                parser.parse(input);
            } catch (Exception ex) {
                output.print(ex.getMessage());
            }
        }
    }

    /**
     * Method usage.
     */
    private static void usage() {
        System.err.println("java trs.Shell [-f file]");
    }

    /**
     * @see trs.Command#quit()
     */
    public void quit() {
        // TODO : save current context
        output.println(outro);
        System.exit(0);
    }

    /**
     * @see trs.Command#rewrite(Term)
     */
    public void rewrite(Term t) {
        output.println(engine.rewrite(t));
        output.println("Number of loop :" + engine.getLoops());
        output.println("Number of rewrites :" + engine.getNumberOfRewrites());
        output.println("Number of rules applied :" + engine.getNumberOfRules());
        output.println("Duration :" + engine.getDuration());
    }

    /**
     * @see trs.Command#clear()
     */
    public void clear() {
        RewriteSystem rs = new RewriteSystemImpl();
        parser.setRewriteSystem(rs);
        engine = new Engine(rs, debug);
    }

    /**
     * @see trs.Command#load(String)
     */
    public void load(String file) {
        try {
            Reader rd = new FileReader(file);
            parse(rd);
        } catch (IOException ioex) {
            output.println(ioex.getMessage());
        }
    }

    /**
     * @see trs.Command#save(String)
     */
    public void save(String file) {
        try {
            PrintStream ps = new PrintStream(new FileOutputStream(file));
            ps.println(parser.getRewriteSystem().toString());
        } catch (IOException ioex) {
            output.println(ioex.getMessage());
        }
    }

    /**
     * Returns the debug.
     *
     * @return boolean
     */
    public boolean isDebug() {
        return debug;
    }

    /* (non-Javadoc)
     * @see trs.Command#debug()
     */
    public void debug() {
        this.setDebug(!debug);
    }

    /* (non-Javadoc)
     * @see trs.Command#equalize(trs.Term, trs.Term)
     */
    public void equalize(Term term, Term term2) {
        output.println(engine.equalize(term, term2));
        output.println("Number of loop :" + engine.getLoops());
        output.println("Number of rewrites :" + engine.getNumberOfRewrites());
        output.println("Number of rules applied :" + engine.getNumberOfRules());
        output.println("Duration :" + engine.getDuration());
    }

}


