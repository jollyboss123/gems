package org.jolly.chapter3.item11;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jolly
 */
public final class Course {
    private final String name;
    private final int[] grades;
    private final List<Integer> credits;

    public Course(String name, int[] grades, List<Integer> credits) {
        this.name = name;
        this.grades = grades;
        this.credits = credits;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Course c)) {
            return false;
        }
        return c.name.equals(name) && Arrays.equals(c.grades, grades) && c.credits.equals(credits);
    }

    // Shows the recursive invocation of hashCode if the field is an array (page 51)
    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + Arrays.hashCode(grades);
        // because list have their own implementation of hashCode and equals in AbstractList
        // same goes for Set and Map
        result = 31 * result + (credits != null ? credits.hashCode() : 0);
        return result;
    }

    public static void main(String[] args) {
        Map<Course, String> m = new HashMap<>();
        m.put(new Course("CS101", new int[]{1,2,3}, List.of(4,5,6)), "Jenny");
        System.out.println(m.get(new Course("CS101", new int[]{1,2,3}, List.of(4,5,6))));
    }
}
