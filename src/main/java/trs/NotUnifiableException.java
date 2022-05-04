/*
 * Created on May 15, 2003 by Arnaud Bailly - bailly@lifl.fr
 * Copyright 2003 - Arnaud Bailly
 */
package trs;

/**
 * @author bailly
 * @version $Id: NotUnifiableException.java,v 1.2 2004/02/09 20:52:43 nono Exp $
 */
public class NotUnifiableException extends TermWalkerException {

    /**
     *
     */
    public NotUnifiableException() {
        super();
    }

    /**
     * @param message
     */
    public NotUnifiableException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    public NotUnifiableException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param cause
     */
    public NotUnifiableException(Throwable cause) {
        super(cause);
    }

}
