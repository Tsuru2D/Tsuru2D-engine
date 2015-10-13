package com.tsuru2d.engine.loader.exception;

public class GameLoadException extends RuntimeException {
    public GameLoadException() {
        super();
    }

    public GameLoadException(String message) {
        super(message);
    }

    public GameLoadException(String message, Throwable cause) {
        super(message, cause);
    }

    public GameLoadException(Throwable cause) {
        super(cause);
    }
}
