package com.coding.fitness.exceptions;

public class ProcessingImgException extends RuntimeException{

    public ProcessingImgException(String message) {
        super(message);
    }

    public ProcessingImgException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProcessingImgException(Throwable cause) {
        super(cause);
    }
}
