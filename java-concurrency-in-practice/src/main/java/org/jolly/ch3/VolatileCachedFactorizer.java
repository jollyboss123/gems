package org.jolly.ch3;

import org.jolly.utils.Request;
import org.jolly.utils.Response;
import org.jolly.utils.Servlet;

import java.math.BigInteger;

public class VolatileCachedFactorizer implements Servlet {
    private volatile OneValueCache cache = new OneValueCache(null, null);

    public void service(Request request, Response response) {
        BigInteger i = extractFromRequest(request);
        BigInteger[] factors = cache.getFactors(i);
        if (factors == null) {
            factors = factor(i);
            cache = new OneValueCache(i, factors);
        }
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
