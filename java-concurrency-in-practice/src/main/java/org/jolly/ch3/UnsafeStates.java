package org.jolly.ch3;

import net.jcip.annotations.NotThreadSafe;

@NotThreadSafe
public class UnsafeStates {
    private String[] states = new String[] {"ready", "not ready", "go"};

    public String[] getStates() {
        return states;
    }
}
