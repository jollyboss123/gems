package org.jolly.ch2;

import net.jcip.annotations.NotThreadSafe;
import org.jolly.utils.Request;
import org.jolly.utils.Response;
import org.jolly.utils.Servlet;

import java.math.BigInteger;

@NotThreadSafe
public class UnsafeCountingFactorizer implements Servlet {

    private long count = 0;

    public long getCount() {
        return count;
    }

    public void service(Request req, Response resp) {
        BigInteger i = extractFromRequest(req);
        BigInteger[] factors = factor(i);
        ++count;
        encodeIntoResponse(resp, factors);
    }

    public BigInteger extractFromRequest(Request request) {
        return BigInteger.TEN;
    }

    public BigInteger[] factor(BigInteger i) {
        BigInteger[] dummy = new BigInteger[1];
        dummy[0] = i;
        return dummy;
    }

    @Override
    public void encodeIntoResponse(Response response, BigInteger[] factors) {

    }
}
