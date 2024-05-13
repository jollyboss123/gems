package org.jolly.ch2;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import org.jolly.utils.Request;
import org.jolly.utils.Response;
import org.jolly.utils.Servlet;

import java.math.BigInteger;

@ThreadSafe
public class SynchronizedFactorizer implements Servlet {
    @GuardedBy("this") private BigInteger lastNumber;
    @GuardedBy("this") private BigInteger[] lastFactors;

    public synchronized void service(Request request, Response response) {
        BigInteger i = extractFromRequest(request);
        if (i.equals(lastNumber)) {
            encodeIntoResponse(response, lastFactors);
        } else {
            BigInteger[] factors = factor(i);
            lastNumber = i;
            lastFactors = factors;
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
