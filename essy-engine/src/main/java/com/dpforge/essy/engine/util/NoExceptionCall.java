package com.dpforge.essy.engine.util;

public abstract class NoExceptionCall<T> {

    public T call() {
        try {
            return callUnsafe();
        } catch (Exception e) {
            throw new IllegalStateException("Never happen", e);
        }
    }

    protected abstract T callUnsafe() throws Exception;
}
