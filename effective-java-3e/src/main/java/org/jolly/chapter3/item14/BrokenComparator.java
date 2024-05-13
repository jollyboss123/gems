package org.jolly.chapter3.item14;

import java.util.Comparator;

/**
 * @author jolly
 */
public class BrokenComparator {
    // BROKEN difference-based comparator - violates transitivity! (Page 71)
    static Comparator<Object> hashCodeOrder = new Comparator<Object>() {
        @Override
        public int compare(Object o1, Object o2) {
            return o1.hashCode() - o2.hashCode();
        }
    };

    // Comparator based on static compare method (Page 71)
//    static Comparator<Object> hashCodeOrder = new Comparator<Object>() {
//        @Override
//        public int compare(Object o1, Object o2) {
//            return Integer.compare(o1.hashCode(), o2.hashCode());
//        }
//    };

    // Comparator based on Comparator construction method
//    static Comparator<Object> hashCodeOrder = Comparator.comparingInt(o -> o.hashCode());
}
