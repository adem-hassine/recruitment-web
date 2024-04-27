package com.proxym.libraryapp.domain.library;

public class InsufficientBooksException extends RuntimeException{
    public InsufficientBooksException(String message){
        super(message);
    }
}
