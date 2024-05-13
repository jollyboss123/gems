## Item 12 - override toString

- providing a good `toString` implementation makes a class more pleasant
    to use and makes systems using the class easier to debug
- when practical, the `toString` method should return *all* the interesting
    information contained in the object
- whether we decide to specify the format of the return value, we should clearly
    document our intentions
- provide programmatic access to the information contained in the value returned
    by `toString`
- it makes no sense to write a `toString` method in a static utility class and most
    `Enum` types, because Java provides a perfectly good one for us
- should write a `toString` method in any abstract class whose subclasses share a common
    string representation
  - e.g. the `toString` methods on most collection implementations are inherited from
  the abstract collection classes
