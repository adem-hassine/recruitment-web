package com.proxym.libraryapp.infrastructure.error.assertions;

import com.proxym.libraryapp.infrastructure.error.assertions.AssertionErrorType;
import com.proxym.libraryapp.infrastructure.error.assertions.AssertionException;

public class NullElementInCollectionException extends AssertionException {

  public NullElementInCollectionException(String field) {
    super(field, message(field));
  }

  private static String message(String field) {
    return new StringBuilder().append("The field \"").append(field).append("\" contains a null element").toString();
  }

  @Override
  public AssertionErrorType type() {
    return AssertionErrorType.NULL_ELEMENT_IN_COLLECTION;
  }
}
