package org.jolly.ch2;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import org.jolly.utils.Request;
import org.jolly.utils.Response;
import org.jolly.utils.Servlet;

import java.math.BigInteger;

@ThreadSafe
public class CachedFactorizer implements Servlet {
    @GuardedBy("this") private BigInteger lastNumber;
    @GuardedBy("this") private BigInteger[] lastFactors;
    @GuardedBy("this") private long hits;
    @GuardedBy("this") private long cacheHits;

    public synchronized long getHits() {
        return hits;
    }

    public synchronized double getCacheHitRatio() {
        return (double) cacheHits / (double) hits;
    }

    public void service(Request request, Response response) {
        BigInteger i = extractFromRequest(request);
        BigInteger[] factors = null;
        synchronized (this) {
            ++hits;
            if (i.equals(lastNumber)) {
                ++cacheHits;
                factors = lastFactors.clone();
            }
        }
        if (factors == null) {
            factors = factor(i);
            synchronized (this) {
                lastNumber = i;
                lastFactors = factors.clone();
            }
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
