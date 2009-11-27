package org.codehaus.einstein.cuoco;

public class WalkException extends RuntimeException {

    public WalkException() {
        super();
    }

    public WalkException(String message) {
        super(message);
    }

    public WalkException(String message, Throwable cause) {
        super(message, cause);
    }

    public WalkException(Throwable cause) {
        super(cause);
    }
}
