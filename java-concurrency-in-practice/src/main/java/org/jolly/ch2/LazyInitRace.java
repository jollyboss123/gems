package org.jolly.ch2;

import net.jcip.annotations.NotThreadSafe;
import org.jolly.utils.ExpensiveObject;

@NotThreadSafe
public class LazyInitRace {
    private ExpensiveObject instance = null;

    public ExpensiveObject getInstance() {
        if (instance == null) {
            instance = new ExpensiveObject();
        }
        return instance;
    }
}

