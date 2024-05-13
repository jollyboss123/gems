package org.jolly.ch5;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.NotThreadSafe;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@NotThreadSafe
public class HiddenIterator {
    @GuardedBy("this") private final Set<Integer> set = new HashSet<>();

    public synchronized void add(Integer i) {
        set.add(i);
    }

    public synchronized void remove(Integer i) {
        set.remove(i);
    }

    public void addTenThings() {
        Random r = new Random();
        for (int i = 0; i < 10; i++) {
            add(r.nextInt());
        }
        System.out.println("debug: added 10 elements to " + set);
    }
}
