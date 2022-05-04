package trs;

/**
 * A callback interface for parser to execute commands given
 * in trs
 *
 * @author bailly
 * @version $Id: Command.java,v 1.4 2003/05/15 08:40:52 bailly Exp $
 */
public interface Command {

    /**
     * Called to notify that a term has been rewritten
     * to the result given.
     *
     * @param t a Term object - maybe null
     */
	void rewrite(Term t);

    /**
     * Called to notify that system should be quitted
     */
	void quit();

    /**
     * Called to notify that a request to load a file has been
     * done. The content of the file should be parsed and added
     * to current RewriteSystem.
     *
     * @param file the name of file to load
     */
	void load(String file);

    /**
     * Called to request that thet content of the current
     * RewriteSystem be saved in the given file
     *
     * @param file the path to file
     */
	void save(String file);

    /**
     * Called to request that the content of the Rewrite System
     * be cleared.
     */
	void clear();

    /**
     * Called to request that debug flag be turned on or off
     */
	void debug();

    /**
     * This method asks the system to try asserting the equality
     * of the two given terms with respect to the current RS.
     *
     * @param term
     * @param term2
     */
	void equalize(Term term, Term term2);

}