package org.jolly.ch2;

import net.jcip.annotations.ThreadSafe;
import org.jolly.utils.Request;
import org.jolly.utils.Response;
import org.jolly.utils.Servlet;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicLong;

@ThreadSafe
public class CountingFactorizer implements Servlet {
    private final AtomicLong count = new AtomicLong(0);

    public Long getCount() {
        return count.get();
    }

    public void service(Request request, Response response) {
        BigInteger i = extractFromRequest(request);
        BigInteger[] factors = factor(i);
        count.incrementAndGet();
        encodeIntoResponse(response, factors);
    }

    @Override
    public BigInteger extractFromRequest(Request request) {
        return null;
    }

    @Override
    public BigInteger[] factor(BigInteger i) {
        return new BigInteger[0];
    }

    @Override
    public void encodeIntoResponse(Response response, BigInteger[] factors) {

    }
}
