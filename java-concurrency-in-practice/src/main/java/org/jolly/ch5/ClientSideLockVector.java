package org.jolly.ch5;

import java.util.Vector;

public class ClientSideLockVector {
    public static Object getLast(Vector list) {
//        int lastIndex = list.size() - 1;
//        return list.get(lastIndex);
        synchronized (list) {
            int lastIndex = list.size() - 1;
            return list.get(lastIndex);
        }
    }

    public static Object deleteLast(Vector list) {
//        int lastIndex = list.size() - 1;
//        return list.remove(lastIndex);
        synchronized (list) {
            int lastIndex = list.size() - 1;
            return list.remove(lastIndex);
        }
    }
}
