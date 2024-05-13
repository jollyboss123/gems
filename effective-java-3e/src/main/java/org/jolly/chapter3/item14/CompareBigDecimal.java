package org.jolly.chapter3.item14;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author jolly
 */
public class CompareBigDecimal {

    // a class whose compareTo method imposes an order that is inconsistent with equals will still work,
    // but sorted collections containing elements of the class may not obey the general contract of
    // the appropriate collection interfaces (Page 68)
    public static void main(String[] args) {
        Set<BigDecimal> s = new HashSet<>();
        s.add(new BigDecimal("1.0"));
        s.add(new BigDecimal("1.00"));
        System.out.println(s);

        Set<BigDecimal> ts = new TreeSet<>();
        ts.add(new BigDecimal("1.0"));
        ts.add(new BigDecimal("1.00"));
        System.out.println(ts);
    }
}
