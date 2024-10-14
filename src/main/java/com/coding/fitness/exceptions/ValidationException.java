package com.coding.fitness.exceptions;

public class ValidationException extends  RuntimeException{

    private String message;

    public ValidationException(String message){
        super();
        this.message = message;
    }

}
