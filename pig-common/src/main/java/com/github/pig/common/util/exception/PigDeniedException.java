package com.github.pig.common.util.exception;

/**
 * @author lengleng
 * @date 2017-12-29 17:05:10
 * 403 授权拒绝
 */
public class PigDeniedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public PigDeniedException() {
    }

    public PigDeniedException(String message) {
        super(message);
    }

    public PigDeniedException(Throwable cause) {
        super(cause);
    }

    public PigDeniedException(String message, Throwable cause) {
        super(message, cause);
    }

    public PigDeniedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
