package com.proxym.libraryapp.library;

import com.proxym.libraryapp.Application;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@DisplayNameGeneration(ReplaceSnakeCase.class)
@SpringBootTest(classes = { Application.class})
public @interface IntegrationTest {
    @AliasFor(annotation = SpringBootTest.class)
    String[] properties() default {};
}
