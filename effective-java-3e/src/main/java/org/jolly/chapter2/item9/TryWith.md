## Item 9 - try-with-resources

- a resource must implement `AutoCloseable` and use `try`-with-resources instead
of a `try`-`finally`
- because even if implemented `AutoCloseable` but used `try`-`finally`, the
exception thrown from the `try` block would be suppressed by the subsequent
exception thrown by the invisible `close` method
  - there will be no record of the exception thrown from the `try` block
  - complicate debugging
- with `try`-with-resources, the exceptions in the `try` block will be preserved
  - suppressed exceptions will not be discarded but printed in the stack trace
  with the notation suppressed
  - can also access these exceptions programmatically with the `getSuppressed`
  method, added to `Throwable` in Java 7
