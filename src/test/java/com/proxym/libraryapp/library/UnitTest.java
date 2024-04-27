package com.proxym.libraryapp.library;

import org.junit.jupiter.api.DisplayNameGeneration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@DisplayNameGeneration(ReplaceSnakeCase.class)
public @interface UnitTest {
}