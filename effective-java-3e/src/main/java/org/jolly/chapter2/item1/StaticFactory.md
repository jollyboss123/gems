## Item 1 - static factory method

- a class can provide a public *static factory* method, which is simply a 
static method that returns an instance of the class
- unlike constructors, they have specific method names
  - user of an API will never have to remember which constructor is which and
  end up calling the wrong one by mistake
- not required to create a new object each time they're invoked
  - similar to *Flyweight pattern*
  - can greatly improve performance if equivalent objects are requested often, 
  especially if they are expensive to create
  - classes that do this are said to be *instance controlled*
    - allows a class to guarantee that it is a singleton or noninstantiable
- can return an object of any subtype of their return type
  - leads itself to *interface-based frameworks*
  - prior to Java 8, interfaces couldn't have static methods
    - by convention, static factory methods for an interface named *Type* were
    put in a *noninstantiable companion class* named *Types*
      - e.g. Java Collections Framework has 45 utility implementations of
      its interfaces 
    - the Collections Framework API would have been much smaller had it exported
    45 separate public classes, 1 for each convenience implementation
  - as of Java 8, the restriction that interfaces cannot contain static 
  methods was eliminated
  - refer to `EnumSet#noneOf` that return instances of *non-public* subclasses
    (`RegularEnumSet` or `JumboEnumSet`)
- class of returned object can vary from call to call as function of the input parameters
  - refer to `EnumSet#noneOf`
- main limitation is that classes without public or protected constructors
cannot be subclassed
  - in return encourage programmers to use composition instead of inheritance
  and is required for immutable types
