/*
 * Created on Apr 28, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package trs;

/**
 * An exception thrown by Rule instances during rewrite process.
 *
 * @author bailly
 * @version $Id: RewriteException.java,v 1.1 2003/04/28 13:46:59 bailly Exp $
 */
public class RewriteException extends RuntimeException {

    /**
     *
     */
    public RewriteException() {
        super();
    }

    /**
     * @param message
     */
    public RewriteException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    public RewriteException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param cause
     */
    public RewriteException(Throwable cause) {
        super(cause);
    }

}
