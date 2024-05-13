## Item 10 - overriding equals

- when `equals` is not override, each instance of the class is equal to itself
- only don't override `equals` when:
  - each instance of the class is inherently unique
    - true for classes such as `Thread` that represent active entities
      rather than values
  - there is no need for the class to provide a "logical equality" test
    - e.g. `regex.Pattern` could have overridden `equals` to check whether
    2 `Pattern` instances represented exactly the same regex, but there is
    no need for this functionality
  - a superclass has already overridden `equals` and the superclass behavior
    is appropriate for this class
    - e.g. most `Set` implementations inherit their `equals` implementation
    from `AbstractSet`, `List` implementations from `AbstractList`, `Map`
    implementations from `AbstractMap`
  - the class is private or package-private, and we are certain that its `equals`
    method will never be invoked
- appropriate to override `equals` when a class has a notion of *logical equality*
  that differs from mere object identity, i.e. *value classes*
  - compares references to value objects using the `equals` method expects to
    find out whether they are logically equivalent, not whether they refer
    to the same object
- enables instances to serve as map keys or set elements with predictable,
  desirable behavior
- 1 kind of value class that does *not* require the `equals` method to be
    overridden is a class that uses instance control to ensure that at most
    1 object exists with each value e.g. `Enum`
- the `equals` method implements an *equivalence relation*, it has to be:
  - reflexive:
    - for any non-null reference value `x`, `x.equals(x)` must return `true`
    - if violated and added an instance of `x` to a collection, the `contains` method
    will return `false`
  - symmetric:
    - for any non-null reference values `x` and `y`, `x.equals(y)` must return true
    and only if `y.equals(x)` return `true`
  - transitive:
    - for any non-null reference values `x`, `y` and `z`, if `x.equals(y)` returns
    true and `y.equals(z)` returns `true`, then `x.equals(z)` must return `true`
    - beware when using inheritance in object-oriented languages, there is no way to 
    extend an instantiable class and add a value component while preserving the `equals` contract
    - using a `getClass` test in place of a `instanceof` test does not solve this but violates
    the *Liskov substitution principle*, that a subclass should work equally well as its
    superclass
    - a fine workaround is favoring composition over inheritance, by having a private
    superclass field and a public *view* method
    - note that there is _no_ issue with adding a value component to a subclass of an
    `abstract` class without violating the `equals` contract, i.e. prefer class hierarchies
    to tagged classes
  - consistent:
    - for any non-null reference values `x` and `y`, multiple invocations of `x.equals(y)`
    must consistently return `true` or consistently return `false`
    - think hard whether a class should be immutable
      - if it should, make sure that the `equals` method enforces the restriction
      that equal objects remain equal and unequal objects remain unequal for all time
    - do not write an `equals` method that depends on unreliable resources
      - e.g. `java.net.URL`'s `equal` method relies on the comparison of the IP
      addresses of the hosts associated with the URLs
      - translating a host name to an IP address can require network access, and it
      isn't guaranteed to yield the same results over time
  - for any non-null reference value `x`, `x.equals(null)` must return `false`
- for a high quality `equals` method:
  - use the `==` operator to check if the argument is a reference to this object
  - use the `instanceof` operator to check if the argument has the correct type
    - if some interface is implemented by a class, use the interface to permit comparisons
    across classes that implement the interface
    - e.g. `Set`, `List`, `Map` and `Map.Entry`
  - cast the argument to the correct type
  - for each "significant" field in the class, check if that field of the argument
  matches the corresponding field of this object
    - for primitive fields, use the `==` operator for comparisons
      - for `float` fields, use the static `Float.compare(float, float)`
      - for `double` fields, use the static `Double.compare(double, double)`
      - the special treatment of `float` and `double` fields is made necessary by
      the existence of `Float.NaN`, `-0.0f` and the analogous `double` values
    - for array fields, if every element in an array field is significant, use
    1 of the `Array.equals` methods
    - for some object reference fields which may legitimately contain `null`, use 
    the static `Objects.equals(object, object)`
    - refer to `CaseInsensitiveString`, where field comparisons are more complex than
    simple equality tests, store a *canonical form* of the field so that the `equals`
    method can do a cheap exact comparison on canonical forms rather than on a more costly
    nonstandard comparison
      - most appropriate for immutable classes
  - for best performance, should compare fields that are more likely to differ, 
  less expensive to compare, or both
    - need not compare *derived fields*, which can be calculated from "significant
    fields", but doing so may improve the performance of the `equals` method
    - if a derived field amounts to a summary description of the entire object, comparing this
    field will save us the expense of comparing the actual data if the comparison fails
- always override `hashCode` when override `equals`
