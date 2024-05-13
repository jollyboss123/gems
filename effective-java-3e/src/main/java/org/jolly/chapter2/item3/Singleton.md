## Item 3 - singleton

- making a class singleton can make it difficult to test its clients
- by making the constructor private and export a public static member to
provide access to the sole instance
  - a privileged client can invoke the private constructor reflectively with
  the aid of the `AccessibleObject#setAccessible` method
  - modify the constructor to make it throw an exception if it's asked to create
  a 2nd instance
- public member is a static factory method
  - makes it clear that the class is a singleton
  - public static field is final, will always contain the same object reference
  - flexibility to change one's mind about whether the class is a singleton
  without changing its API
  - can write a *generic singleton factory* if application requires it
  - a *method reference* can be used as a supplier 
    - e.g. `Elvis::instance` is a `Supplier<Elvis>`
- to make a singleton that uses either of these approaches *serializable*,
it is not sufficient merely to add *implements Serializable* to its declaration
  - need to declare all instance fields `transient` and provide `readResolve` method
  - else, each time a serialized instance is deserialized, a new instance will
  be created
- declare a single element enum
  - more concise
  - provides the serializable machinery for free, and provides an ironclad
  guarantee against multiple instantiation, even in the face of sophisticated
  serialization or reflection attacks
  - can't use this approach if singleton must extend a superclass other than
  `Enum` (though can declare an enum to implement interfaces)
