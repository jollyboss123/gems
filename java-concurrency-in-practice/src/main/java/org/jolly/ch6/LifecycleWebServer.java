package org.jolly.ch6;

import org.jolly.utils.Request;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.logging.Logger;

public class LifecycleWebServer {
    private static final Logger log = Logger.getLogger(LifecycleWebServer.class.getName());
    private final ExecutorService exec = Executors.newFixedThreadPool(100);

    public void start() throws IOException {
        ServerSocket socket = new ServerSocket(80);
        while (!exec.isShutdown()) {
            try {
                final Socket conn = socket.accept();
                exec.submit(() -> handleRequest(conn));
            } catch (RejectedExecutionException e) {
                if (!exec.isShutdown()) {
                    log.info("task submission rejected" + e);
                }
            }
        }
    }

    public void stop() {
        exec.shutdown();
    }

    private void handleRequest(Socket conn) {
        Request request = readRequest(conn);
        if (isShutdownRequest(request)) {
            stop();
        } else {
            dispatchRequest(request);
        }
    }

    private Request readRequest(Socket conn) {
        return null;
    }

    private boolean isShutdownRequest(Request request) {
        return false;
    }

    private void dispatchRequest(Request request) {}
}
