package org.jolly.chapter2.item5;

import java.util.function.Supplier;

/**
 * @author jolly
 */
public class Person {
    private String name;
    private Address address;

    public Person(String name, Address address) {
        this.name = name;
        this.address = address;
    }

    public Person(String name, Supplier<Address> addressFactory) {
        this.name = name;
        this.address = addressFactory.get();
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", address=" + address +
                '}';
    }
}

class Address {
    private int streetNumber;

    Address() {
        this.streetNumber = 0;
    }

    Address(int streetNumber) {
        this.streetNumber = streetNumber;
    }

    public void setStreetNumber(int streetNumber) {
        this.streetNumber = streetNumber;
    }

    public int getStreetNumber() {
        return streetNumber;
    }

    @Override
    public String toString() {
        return "Address{" +
                "streetNumber=" + streetNumber +
                '}';
    }
}

class PersonTest {
    public static void main(String[] args) {
        var addr = new Address(212);
        var p1 = new Person("Bob", addr);
        System.out.println(p1);

        // because pass by reference, the updated street number
        // will be reflected in p1
        addr.setStreetNumber(312);
        System.out.println(p1);

        // use temporary reference
        // with normal dependency injection, the address is eagerly initialized
        var p2 = new Person("Bob", new Address(52));
        System.out.println(p2);

        // use method reference as supplier
        // with lambda expression, the address is lazily initialized
        var p3 = new Person("Bob", () -> new Address(20));
        System.out.println(p3);

        // default constructor
        var p4 = new Person("Bob", Address::new);
        System.out.println(p4);
    }
}
