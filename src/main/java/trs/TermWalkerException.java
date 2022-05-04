/*
 * Created on May 15, 2003 by Arnaud Bailly - bailly@lifl.fr
 * Copyright 2003 - Arnaud Bailly
 */
package trs;

/**
 * @author bailly
 * @version $Id: TermWalkerException.java,v 1.2 2004/02/09 20:52:43 nono Exp $
 */
public class TermWalkerException extends Exception {

    /**
     *
     */
    public TermWalkerException() {
        super();
    }

    /**
     * @param message
     */
    public TermWalkerException(String message) {
        super(message);

    }

    /**
     * @param message
     * @param cause
     */
    public TermWalkerException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param cause
     */
    public TermWalkerException(Throwable cause) {
        super(cause);
    }

}
