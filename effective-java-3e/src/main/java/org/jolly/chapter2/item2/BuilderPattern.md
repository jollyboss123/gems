## Item 2 - builder pattern

- static factories and constructors do not scale well to large numbers of
optional parameters
- telescoping constructor pattern works, but it is hard to write client code
when there are many parameters, and harder still to read it
- javabeans pattern may result in an inconsistent state partway through its
construction and precludes the possibility of making a class immutable
- builder pattern simulates named optional parameters as found in Python and Scala
- check invariants involving multiple parameters in the constructor invoked
by the `build` method
