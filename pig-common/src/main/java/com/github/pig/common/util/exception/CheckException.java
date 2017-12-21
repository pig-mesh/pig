package com.github.pig.common.util.exception;

/**
 * @author lengleng
 * @date 😴2017年12月21日20:44:38
 */
public class CheckException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CheckException() {
    }

    public CheckException(String message) {
        super(message);
    }

    public CheckException(Throwable cause) {
        super(cause);
    }

    public CheckException(String message, Throwable cause) {
        super(message, cause);
    }

    public CheckException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
