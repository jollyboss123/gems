## Item 5 - dependency injection

- static utility classes and singletons are inappropriate for classes whose
behavior is parameterized by an underlying resource
- pass the resource into the constructor when creating a new instance
  - preserves immutability
  - multiple clients can share dependency objects
  - equally applicable to constructors, static factories, and builders
- pass a *resource factory* to the constructor
  - the `Supplier<T>` interface is perfect for representing factories
