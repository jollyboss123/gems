package org.jolly.ch4;

import net.jcip.annotations.NotThreadSafe;

import java.util.concurrent.atomic.AtomicInteger;

@NotThreadSafe
public class NumberRange {
    // invariant: lower <= upper
    private final AtomicInteger lower = new AtomicInteger(0);
    private final AtomicInteger upper = new AtomicInteger(0);

    public void setLower(int i) {
        // warning - unsafe check-then-act
        if (i > upper.get()) {
            throw new IllegalArgumentException("cannot set lower to %d > upper".formatted(i));
        }
        lower.set(i);
    }
}
