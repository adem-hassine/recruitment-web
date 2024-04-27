package com.proxym.libraryapp.domain.library;

/**
 * This exception is thrown when a member who owns late books tries to borrow another book
 */
public class HasLateBooksException extends RuntimeException {
}