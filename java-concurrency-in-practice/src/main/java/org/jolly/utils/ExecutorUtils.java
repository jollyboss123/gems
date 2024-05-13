package org.jolly.utils;

public class ExecutorUtils {
    private ExecutorUtils() {}

    public static RuntimeException launderThrowable(Throwable t) {
        if (t instanceof RuntimeException re) {
            return re;
        } else if (t instanceof Error err) {
            throw err;
        } else {
            throw new IllegalStateException("not unchecked", t);
        }
    }
}
