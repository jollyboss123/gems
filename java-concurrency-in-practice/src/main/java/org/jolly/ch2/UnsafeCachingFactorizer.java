package org.jolly.ch2;

import net.jcip.annotations.NotThreadSafe;
import org.jolly.utils.Request;
import org.jolly.utils.Response;
import org.jolly.utils.Servlet;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicReference;

@NotThreadSafe
public class UnsafeCachingFactorizer implements Servlet {
    private final AtomicReference<BigInteger> lastNumber = new AtomicReference<>();
    private final AtomicReference<BigInteger[]> lastFactors = new AtomicReference<>();

    public void service(Request request, Response response) {
        BigInteger i = extractFromRequest(request);
        if (i.equals(lastNumber.get())) {
            encodeIntoResponse(response, lastFactors.get());
        } else {
            BigInteger[] factors = factor(i);
            lastNumber.set(i);
            lastFactors.set(factors);
            encodeIntoResponse(response, factors);
        }
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
