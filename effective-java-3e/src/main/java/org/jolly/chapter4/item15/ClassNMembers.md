## Item 15 - minimize accessibility of classes and members

- while information hiding does not itself cause good performance, it enables effective performance tuning
  - i.e. once a system is complete and profiling has determined which components are causing performance problems, those components can be optimized without affecting the correctness of others
- **make each class or member as inaccessible as possible**
- it is acceptable to make a private member of a public class package-private in order to test it, but it is not acceptable to raise the accessibility any higher
  - tests can be made to run as part of the package being tested, thus gaining access to its package-private elements
- instance fields of public classes should _rarely_ be public 
  - if an instance field is nonfinal or is a reference to a mutable object, then by making it public, we give up the ability to enforce invariants involving the field
  - we also give up the ability to take any action when the field is modified
    - so classes with public mutual fields are generally _not_ thread-safe
- note that even a *static* field containing a reference to a mutable object has all the disadvantages of a nonfinal field
  - nonzero-length array is always mutable, so it is **wrong** for a class to have a `public static final` array field, or an accessor that returns such a field
  - ensure that objects referenced by `public static final` fields are immutable
