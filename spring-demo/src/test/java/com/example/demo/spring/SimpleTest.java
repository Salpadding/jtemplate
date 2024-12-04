package com.example.demo.spring;

import org.junit.jupiter.api.Test;
import org.springframework.core.annotation.MergedAnnotations;

import java.lang.annotation.*;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@interface Example1s {
    Example1[] value();
}


// import org.springframework.core.annotation.
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(Example1s.class)
@interface Example1 {
    String[] value() default "";
    String[] alias() default "";
}

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Example1("aaaa")
@Example1("aaaa")
@interface Example2 {
    String[] value() default "";
    String[] alias() default "";
}


@Example2
public class SimpleTest {
    @Test
    public void test0() {
        MergedAnnotations merged = MergedAnnotations.from(SimpleTest.class);
        Object obj = merged.get(Example1.class).getValue("value").get();
        System.out.println(obj);
    }
}
