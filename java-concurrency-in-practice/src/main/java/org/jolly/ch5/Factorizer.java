package org.jolly.ch5;

import net.jcip.annotations.ThreadSafe;
import org.jolly.utils.Request;
import org.jolly.utils.Response;
import org.jolly.utils.Servlet;

import java.math.BigInteger;

@ThreadSafe
public class Factorizer implements Servlet {
    private final Computable<BigInteger, BigInteger[]> c = new Computable<BigInteger, BigInteger[]>() {
        @Override
        public BigInteger[] compute(BigInteger arg) throws InterruptedException {
            return factor(arg);
        }
    };
    private final Computable<BigInteger, BigInteger[]> cache = new Memorizer<>(c);

    public void service(Request request, Response response) {
        try {
            BigInteger i = extractFromRequest(request);
            encodeIntoResponse(response, cache.compute(i));
        } catch (InterruptedException e) {
            encodedError(response, "factorization interrupted");
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

    private void encodedError(Response response, String message) {}
}
