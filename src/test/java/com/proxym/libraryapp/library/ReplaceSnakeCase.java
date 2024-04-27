package com.proxym.libraryapp.library;

import org.junit.jupiter.api.DisplayNameGenerator;

import java.lang.reflect.Method;

public class ReplaceSnakeCase extends DisplayNameGenerator.Standard {

  @Override
  public String generateDisplayNameForMethod(Class<?> testClass, Method testMethod) {
    return this.replaceCapitals(testMethod.getName());
  }

  private String replaceCapitals(String name) {
    name = name.replaceAll("_", " ");
    name = name.toLowerCase();
    return name;
  }
}
