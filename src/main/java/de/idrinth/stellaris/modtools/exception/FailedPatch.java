package de.idrinth.stellaris.modtools.exception;

public class FailedPatch extends RuntimeException {

    public FailedPatch() {
    }

    public FailedPatch(String message) {
        super(message);
    }

    public FailedPatch(String message, Throwable cause) {
        super(message, cause);
    }

    public FailedPatch(Throwable cause) {
        super(cause);
    }

    protected FailedPatch(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
