## Item 14 - implementing Comparable

- by implementing `Comparable`, a class indicates that its instances
have a *natural ordering*
- by implementing `Comparable`, we allow our class to interoperate with all of
the many generic algorithms and collection implementations that depend on this interface
  - e.g. sorted collections `TreeSet` and `TreeMap` and the utility classes
  `Collections` and `Arrays`
- if we are writing a value class with an obvious natural ordering, such as alphabetical
order, numerical order, or chronological order, we should implement the `Comparable` interface
- `Comparable` contract:
  - must ensure that `sgn(x.compareTo(y)) == -sgn(y.compareTo(x))` for all `x` and `y`
  - must ensure that the relation is transitive 
    - `x.compareTo(y) > 0 && y.compareTo(z) > 0` implies that `x.compareTo(z) > 0`
  - must ensure that `x.compareTo(y) == 0` implies that `sgn(x.compareTo(z)) == sgn(y.compareTo(z))`
  for all `z`
  - strongly recommended, but *not* required, that `(x.compareTo(y) == 0) == (x.equals(y))`
    - if violated, should clearly indicate this fact
    - recommended language: "Note: This class has a natural ordering that is inconsistent with equals."
- `compareTo` doesn't have to work across objects of different types: when confronted with objects of
different types, `compareTo` is permitted to throw `ClassCastException`
- the contract does *permit* intertype comparisons, which are typically defined
in an interface implemented by the objects being compared
- has the same caveat as `equals` contract:
  - no way to extend an instantiable class with a new value component while
  preserving the `compareTo` contract
  - if we want to add a value component to a class that implements `Comparable`, don't extend it
    - write an unrelated class containing an instance of the 1st class
    - provide a "view" method that returns the contained instance
- a class whose `compareTo` method imposes an order that is inconsistent with `equals` will still
work, but sorted collections containing elements of the class may not obey the general contract of 
the appropriate collection interfaces (`Collection`, `Set`, or `Map`)
  - this is because general contracts for these interfaces are defined in terms of the `equals`
  method, but sorted collections use the equality test imposed by `compareTo` in place of `equals`
- because the `Comparable` interface is parameterized, the `compareTo` method is statically
typed, so we don't need to type check or cast its argument
- to compare object reference fields, invoke the `compareTo` method recursively
- if a field does not implement `Comparable` or we need a nonstandard ordering, use
a `Comparator` instead
- use of relational operators `<` and `>` in `compareTo` methods is verbose and not recommended,
use static `compare` methods instead
- if a class has multiple significant fields, the order in which we compare them is critical
  - start with the most significant field and work our way down
- the `Comparator` interface enables fluent construction of comparators, but does come at a 
modest performance cost
  - when using this approach, consider using Java's *static import* facility, so we
  can refer to static comparator construction methods by their simple names for clarity and brevity
  - this implementation builds a comparator at class initialization time, using 2 comparator
  construction methods
- might see `compareTo` or `compare` methods that rely on the difference between 2 values
  - do not use this technique
  - it is fraught with danger from integer overflow and IEEE 754 floating point arithmetic
  artifacts
  - use either a static `compare` method or a comparator construction method
