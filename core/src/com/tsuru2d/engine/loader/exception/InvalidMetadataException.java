package com.tsuru2d.engine.loader.exception;

public class InvalidMetadataException extends RuntimeException {
    public InvalidMetadataException() {
        super();
    }

    public InvalidMetadataException(String message) {
        super(message);
    }

    public InvalidMetadataException(Throwable cause) {
        super(cause);
    }

    public InvalidMetadataException(String message, Throwable cause) {
        super(message, cause);
    }
}
