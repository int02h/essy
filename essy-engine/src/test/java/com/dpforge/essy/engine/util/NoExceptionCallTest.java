package com.dpforge.essy.engine.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class NoExceptionCallTest {
    @Test
    public void call() throws Exception {
        final String actual = new NoExceptionCall<String>() {
            @Override
            protected String callUnsafe() throws Exception {
                return "Hello World";
            }
        }.call();
        assertEquals("Hello World", actual);
    }

    @Test(expected = IllegalStateException.class)
    public void callWithException() throws Exception {
        new NoExceptionCall<String>() {
            @Override
            protected String callUnsafe() throws Exception {
                String[] array = new String[]{"test"};
                return array[1];
            }
        }.call();
    }

}