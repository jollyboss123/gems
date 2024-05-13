## Item 13 - overriding clone

- in practice, a class implementing `Cloneable` is expected to provide a properly
functioning public `clone` method
- immutable classes should never provide a `clone` method because it would merely
encourage wasteful copying
- in effect, the `clone` method functions as a constructor; we must ensure that it does
no harm to the original object and that it properly establishes invariants on the clone 
- arrays are the **sole** compelling use of the `clone` facility
  - the core idea is that `clone()` provides a convenient mechanism to create a duplicate (shallow copy) of an array
  - relies on the optimized native implementation `System#arraycopy`
    - which is designed to move blocks of memory very efficiently, this is faster than manually
    copying individual elements with a step-by-step iteration in Java
- public `clone` methods should omit the `throws` clause
- when designing a class for inheritance, the class should *not* implement
`Cloneable`
  - may choose to mimic the behavior of `Object` by implementing a properly functioning
  protected `clone` method that is declared to throw `CloneNotSupportedException`
    - this gives subclasses the freedom to implement `Cloneable`
  - may choose *not* to implement a working `clone` method, and to prevent
  subclasses from implementing one by throwing `CloneNotSupportedException`
- if we write a thread-safe class that implements `Cloneable`, remember that its `clone`
method must be properly synchronized, just like any other method
- a better approach to object copying is to provide a *copy constructor* or *copy factory*
  - don't conflict with the proper use of final fields 
  - don't throw unnecessary checked exceptions
  - don't require casts
  - can take an argument whose type is an interface implemented by the class
- interface based copy constructors and factories, more properly known as *conversion constructors*
and *conversion factories*, allow the client to choose the implementation type of the copy rather
than forcing the client to accept the implementation type of the original
- given all the problems associated with `Cloneable`, new interfaces should not extend it, and new extendable
classes should not implement it
- while it's less harmful for final classes to implement `Cloneable`, this should be viewed
as a performance optimization, reserved for the rare cases where it is justified
