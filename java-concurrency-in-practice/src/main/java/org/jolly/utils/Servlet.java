package org.jolly.utils;

import java.math.BigInteger;

public interface Servlet {
    BigInteger extractFromRequest(Request request);
    BigInteger[] factor(BigInteger i);
    void encodeIntoResponse(Response response, BigInteger[] factors);
}
