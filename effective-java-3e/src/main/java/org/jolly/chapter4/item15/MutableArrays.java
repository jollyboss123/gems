package org.jolly.chapter4.item15;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author jolly
 */
class MutableArrays {
    // Potential security hole! (page 76)
    public static final Integer[] VALUES = {1,2,3};

    // to fix, there are 2 alternatives (page 76)
    // 1.
    // throws UnsupportedOperationException when modify
    private static final Integer[] PRIVATE_VALUES = {1,2,3};
    // can just be replaced by List.of since List.of creates a
    // ImmutableCollections with the input array
    public static final List<Integer> PUBLIC_VALUES = Collections.unmodifiableList(Arrays.asList(PRIVATE_VALUES));

    // 2.
    // will not throw exception and unmodifiable
    // but provides client an option to create a new variable and override some of the values without
    // modifying the private values since it is cloned
    public static final Integer[] values() {
        return PRIVATE_VALUES.clone();
    }

    // throws UnsupportedOperationException
    // because List.of creates a ImmutableCollections
    public static final List<Integer> VALUES_LIST = List.of(1,2,3);

    // modifiable and unsafe
    // Arrays.asList creates an ArrayList
    public static final List<Integer> VALUES_ARRAY_LIST = Arrays.asList(1,2,3);

    // throws UnsupportedOperationException
    // the Set.of static factory method creates a ImmutableCollections
    public static final Set<Integer> VALUES_SET = Set.of(1,2,3);
}
