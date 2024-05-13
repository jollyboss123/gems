package org.jolly.chapter3.item10;

import java.util.Objects;

/**
 * @author jolly
 */
public class Dog {
    private String name;

    public Dog(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Dog d)) {
            return false;
        }
        return Objects.equals(d.name, name);
    }

    public static void main(String[] args) {
        Dog dog1 = new Dog("Jack");
        Dog dog2 = dog1;
        Dog dog3 = new Dog("Jack");
        Dog dog4 = new Dog("Buzz");

        System.out.println(dog1 == dog2);
        System.out.println(dog1.equals(dog2));
        System.out.println(dog1 == dog3);
        System.out.println(dog1.equals(dog3));
        System.out.println(dog1 == dog4);
        System.out.println(dog1.equals(dog4));
    }
}
