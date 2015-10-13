package com.tsuru2d.engine.loader.exception;

public class AssetTypeMismatchException extends RuntimeException {
    public AssetTypeMismatchException() {
        super();
    }

    public AssetTypeMismatchException(String message) {
        super(message);
    }

    public AssetTypeMismatchException(Throwable cause) {
        super(cause);
    }

    public AssetTypeMismatchException(String message, Throwable cause) {
        super(message, cause);
    }
}
