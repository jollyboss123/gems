package org.jolly.chapter2.item5;

import java.util.function.Supplier;

// Use the supplier interface to represent a resource factory
public class Mosaic {
    public Mosaic create(Supplier<? extends Tile> tileFactory) {
        return this;
    }
}

class Tile {}
