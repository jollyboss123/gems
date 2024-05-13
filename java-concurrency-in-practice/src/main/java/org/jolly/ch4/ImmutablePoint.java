package org.jolly.ch4;

import net.jcip.annotations.Immutable;

@Immutable
public class ImmutablePoint {
    public final int x;
    public final int y;

    ImmutablePoint(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
