package org.jolly.chapter3.item13;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

interface ToppingContainer {
    List<String> getToppings();
}

class Salad implements ToppingContainer {
    private List<String> toppings;

    public Salad(List<String> toppings) {
        this.toppings = toppings;
    }

    @Override
    public List<String> getToppings() {
        return toppings;
    }
}

class Pizza implements ToppingContainer {
    private int size;
    private List<String> toppings;

    public int getSize() {
        return size;
    }

    @Override
    public List<String> getToppings() {
        return toppings;
    }

    public Pizza(int size, List<String> toppings) {
        this.size = size;
        this.toppings = new ArrayList<>(toppings); // defensive copy
    }

    // copy constructor (Page 65)
    public Pizza(Pizza otherPizza) {
        this.size = otherPizza.getSize();
        this.toppings = new ArrayList<>(otherPizza.getToppings()); // defensive copy
    }

    // copy factory (Page 65)
    public static Pizza newInstance(Pizza otherPizza) {
        return new Pizza(otherPizza.getSize(), new ArrayList<>(otherPizza.getToppings()));
    }

    // conversion constructor (Page 65)
    public Pizza(ToppingContainer container) {
        this.size = 16; // default in this conversion
        this.toppings = new ArrayList<>(container.getToppings());
    }

    // conversion factory (Page 65)
    public static ToppingContainer from(ToppingContainer container) {
        return new Pizza(container);
    }

    public static void main(String[] args) {
//        Pizza originalPizza = new Pizza(16, Arrays.asList("Cheese", "Pepperoni"));
//        Pizza copiedPizza = new Pizza(originalPizza);

        Pizza originalPizza = new Pizza(16, Arrays.asList("Cheese", "Pepperoni"));
        Pizza copiedPizza = Pizza.newInstance(originalPizza);

        Salad salad = new Salad(Arrays.asList("Lettuce", "Tomato"));
        Pizza saladPizza = new Pizza(salad);
        Pizza anotherSaladPizza = (Pizza) Pizza.from(salad);
    }
}
