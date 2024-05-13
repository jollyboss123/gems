package org.jolly.chapter2.item1;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jolly
 */
public class MyMuchBetterList<E> extends AbstractList<E> implements List<E> {
    @Override
    public E get(int index) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }
}

class Test {
    public static void main(String[] args) {
        List<String> l = getList();
        System.out.println(l.getClass());
    }

    // can return an object of any subtype of their return type
    public static List<String> getList() {
        return new ArrayList<>();
//        return new MyMuchBetterList<>();
    }
}
