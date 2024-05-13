package org.jolly.chapter3.item11;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jolly
 */
public final class Person {
    private final String name;
    private final int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Person p)) {
            return false;
        }
        return p.name.equals(name) && p.age == age;
    }

    // Shows the recursive invocation of hashCode if the field is an object reference (page 51)
    // the object reference type here is String
    @Override
    public int hashCode() {
        int result = Integer.hashCode(age);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    public static void main(String[] args) {
        Map<Person, String> m = new HashMap<>();
        m.put(new Person("Jenny", 19), "Jenny");
        System.out.println(m.get(new Person("Jenny", 19)));
    }
}
