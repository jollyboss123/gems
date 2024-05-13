## Item 8 - avoid finalizers and cleaners

- finalizers are unpredictable, often dangerous, and generally unnecessary
- cleaners are less dangerous than finalizers, but still unpredictable, slow,
and generally unnecessary
- never do anything time-critical in a finalizer or cleaner
- never depend on a finalizer or cleaner to update persistent state
- methods e.g. `System#gc` and `System#runFinalization` may increase the odds
of finalizers or cleaners getting executed, but they don't guarantee it
- an uncaught exception thrown during finalization is ignored, and finalization
of the object terminates
  - can leave other objects in a corrupt state
- there is a severe performance penalty for using finalizers and cleaners
  - use a `AutoCloseable` object, to close it using `try`-with-resources instead
  - this is because finalizers and cleaners inhibit efficient garbage
  collection
- finalizers have a serious security problem: they open our class up to
*finalizer attacks*
  - if an exception is thrown from a constructor or its serialization
  equivalents (the `readObject` and `readResolve` methods) the finalizer of a
  malicious subclass can run on the partially constructed object that should
  have "died on the vine"
  - this finalizer can record a reference to the object in the static field,
  preventing it from being garbage collected
  - it is a simple matter to invoke arbitrary methods on this object that 
  should never have been allowed to exist in the 1st place
  - final classes are immune because no one can write a malicious subclass
  of a final class
  - to protect nonfinal classes, write a `final` `finalize` method that does
  nothing
- a class should implement `AutoCloseable` whose objects encapsulate resources
that require termination e.g. files or threads, and require its clients to 
invoke the `close` method on each instance when it is no longer needed, to ensure
termination even in the face of exceptions
- use finalizers and cleaners to act as a safety net in case the owner of the
resource neglects to call its `close` method
  - it is better to free the resource late than never if the client fails to
  do so
  - must consider if such protection is worth the cost
  - e.g. `FileInputStream`, `FileOutputStream`, `ThreadPoolExecutor`, and
  `java.sql.Connection`
- use finalizers and cleaners for objects with *native peers*
  - a native peer is a native (non-Java) object to which a normal object
  delegate via native methods
  - because a native peer is not a normal object, the garbage collector doesn't
  know about it and can't reclaim it when its Java peer is reclaimed
  - assuming the performance is acceptable and the native peer holds no
  critical resources
  - else, should implement `AutoCloseable`
