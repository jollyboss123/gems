package org.jolly.ch4;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

@ThreadSafe
public class PrivateLock {
    private final Object lock = new Object();
    @GuardedBy("this") Widget widget;

    void someMethod() {
        synchronized (lock) {
            // access or modify the state of the widget
        }
    }

    public interface Widget {}
}
