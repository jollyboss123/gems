## Item 6 - avoid creating unnecessary objects

- can often avoid creating unnecessary objects by using *static factory
methods*
  - e.g. `Boolean#valueOf(String)` is preferable to the constructor 
  `Boolean(String)`, which was deprecated in Java 9
  - the constructor *must* create a new object each time it's called,
  while the factory method is never required to do so
- some object creations are more expensive than others, it may be advisable
to cache it for reuse
  - e.g. creating a `Pattern` instance is expensive because it requires
  compiling the regex expression into a finite state machine
- if a class contains a method which was initialized but the method was never
invoked, the cached field(s) will be initialized needlessly
  - possible to eliminate this by *lazy initializing* the field(s), but this
  is *not* recommended
  - would complicate the implementation with no measurable performance
  improvement
- for the case of adapters i.e. an object that delegates to a backing object,
providing an alternative interface
  - since an adapter has no state beyond that of its backing object, there's
  no need to create more than one instance of a given adapter to a given
  object
  - e.g. `keySet` method of the `Map` interface returns a `Set` view of the
  `Map` object, consisting of all the keys in the map
    - every call to `keySet` on a given `Map` object may return the same 
    `Set` instance
    - although the returned `Set` instance is typically mutable, all the
    returned objects are functionally identical:
      - when one of the returned object changes, so do the others, because
      they're all backed by the same `Map` instance
- *autoboxing* may create unnecessary objects
  - blurs but does not erase the distinction between primitive and boxed
  primitive types
  - prefer primitives to boxed primitives
- should not be misconstrued to imply that object creation is expensive and should
be avoided
- the creation and reclamation of small objects whose constructors do little
explicit work is cheap, especially on modern JVM implementations
- conversely, avoiding object creation by maintaining our own *object pool* is a
bad idea unless the objects in the pool are extremely heavyweight
  - e.g. database connection
- note that the penalty for reusing an object when defense copying is called
for is far greater than the penalty for needlessly creating a duplicate object
  - failing to make defensive copies where required can lead to insidious bugs
  and security holes
  - creating objects unnecessarily merely affects style and
  performance
