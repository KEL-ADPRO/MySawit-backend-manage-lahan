package com.mysawit.mysawit_kebun.exception;

public abstract class KebunException extends RuntimeException {
    public KebunException(String message) {
        super(message);
    }

    public KebunException(String message, Throwable cause) {
        super(message, cause);
    }
}

