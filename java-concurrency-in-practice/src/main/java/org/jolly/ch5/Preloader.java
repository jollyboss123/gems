package org.jolly.ch5;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class Preloader {
    private final FutureTask<ProductInfo> future = new FutureTask<>(new Callable<ProductInfo>() {
        @Override
        public ProductInfo call() throws Exception {
            return loadProductInfo();
        }
    });

    private final Thread thread = new Thread(future);

    public void start() {
        thread.start();
    }

    public ProductInfo get() throws InterruptedException, DataLoadException {
        try {
            return future.get();
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof DataLoadException dle) {
                throw dle;
            } else {
                throw launderThrowable(cause);
            }
        }
    }

    public static RuntimeException launderThrowable(Throwable t) {
        if (t instanceof RuntimeException re) {
            return re;
        } else if (t instanceof Error err) {
            throw err;
        } else {
            throw new IllegalStateException("not unchecked", t);
        }
    }

    private static ProductInfo loadProductInfo() throws DataLoadException {
        // loading information from a database
        return null;
    }

    public static class ProductInfo {}

    public static class DataLoadException extends Exception {}
}
