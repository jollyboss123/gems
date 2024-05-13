package org.jolly.ch3;

import net.jcip.annotations.NotThreadSafe;
import net.jcip.annotations.ThreadSafe;

import static org.jolly.ch3.ThisEscape.*;

@NotThreadSafe
public class ThisEscape {
    public ThisEscape(EventSource source) {
        source.registerListener(new EventListener() {
            @Override
            public void onEvent(Event event) {
                doSomething(event);
            }
        });
    }

    public interface EventSource {
        void registerListener(EventListener listener);
    }

    public interface EventListener {
        void onEvent(Event event);
    }

    public interface Event {}

    private void doSomething(Event event) {}
}

@ThreadSafe
class SafeListener {
    private final EventListener listener;

    private SafeListener() {
        listener = new EventListener() {
            @Override
            public void onEvent(Event event) {
                doSomething(event);
            }
        };
    }

    public static SafeListener newInstance(EventSource source) {
        SafeListener safe = new SafeListener();
        source.registerListener(safe.listener);
        return safe;
    }

    private void doSomething(Event event) {}
}
