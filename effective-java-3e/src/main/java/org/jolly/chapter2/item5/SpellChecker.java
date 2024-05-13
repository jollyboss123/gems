package org.jolly.chapter2.item5;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

// Dependency injection provides flexibility and testability
public class SpellChecker {
    private final Lexicon dictionary;

    // Class behavior dependent on 1 or more underlying resources
    public SpellChecker(Lexicon dictionary) {
        this.dictionary = Objects.requireNonNull(dictionary);
    }

    public boolean isValid(String word) {
        return false;
    }

    public List<String> suggestions(String typo) {
        return Collections.emptyList();
    }
}

class Lexicon {}
