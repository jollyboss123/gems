package org.jolly.chapter4.item15;

import java.util.Arrays;
import java.util.List;

/**
 * @author jolly
 */
public class Test {
    public static void main(String[] args) {
        MutableArrays.VALUES[0] = 2;
        System.out.println(Arrays.toString(MutableArrays.VALUES));

        // throws UnsupportedOperationException when modify
        MutableArrays.PUBLIC_VALUES.set(0, 2);
        System.out.println(MutableArrays.PUBLIC_VALUES);

        // will not throw exception and not modifiable
        MutableArrays.values()[0] = 2;
        System.out.println(Arrays.toString(MutableArrays.values()));

        List<Integer> arr1 = MutableArrays.PUBLIC_VALUES;
        arr1.set(0, 2);
        System.out.println(MutableArrays.PUBLIC_VALUES);
        System.out.println(arr1);

        Integer[] arr2 = MutableArrays.values();
        arr2[0] = 2;
        System.out.println(Arrays.toString(MutableArrays.values()));
        System.out.println(Arrays.toString(arr2));

        MutableArrays.VALUES_LIST.set(0, 2);
        System.out.println(MutableArrays.VALUES_LIST);

        // throws UnsupportedOperationException upon mutation
        MutableArrays.VALUES_ARRAY_LIST.set(0, 2);
        System.out.println(MutableArrays.VALUES_ARRAY_LIST);

        MutableArrays.VALUES_SET.remove(1);
        System.out.println(MutableArrays.VALUES_SET);
    }
}
