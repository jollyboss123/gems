## Item 11 - overriding hashCode

- the contract, adapted from the `Object` specification:
  - when the `hashCode` method is invoked on an object repeatedly
  during an execution of an application, it must consistently return the same
  value, provided no information used in `equals` comparisons is modified
  - if 2 objects are equal according to the `equals(Object)` method, then
  calling `hashCode` on the 2 objects must produce the same integer result
  - if 2 objects are unequal according to the `equals(Object)` method, it is *not*
  required that calling `hashCode` on each of the objects must produce distinct results
    - however, producing distinct results for unequal objects may improve
    the performance of hash tables
- when fail to override `hashCode`, the 2nd provision of contract is violated
  - because failure to override `hashCode` causes 2 equal instances to have
  unequal hash codes
  - thus, methods such as `HashMap#get` is likely to look for the object in a different hash
  bucket from the 1 in which it was stored by the `HashMap#put` method
  - even if the 2 instances happen to hash to the same bucket, the `HashMap#get` method
  will return `null`, because `HashMap` has an optimization that caches the hash code
  associated with each entry and doesn't bother checking for object equality if the
  hash codes don't match
- returning an arbitrary value as hash code is atrocious because it ensures that *every* object
  has the exact same hash code
  - every object hashes to the same bucket, and hash tables degenerate to linked lists
    - this is because when multiple keys with the same hash code == collision
    - since each bucket in the hash table is actually a linked list, all the elements are
    *chained* together in 1 bucket, making the hash table structure equivalent to a linked list
  - causing the lookup function which should run in linear time to instead run in quadratic time
    - this is because for each lookup:
      - we have a linked list operation with O(N) complexity
      - likely performing this lookup multiple times inside a loop as we process elements
      in the hash table
- a good hash function tends to produce unequal hash codes for unequal instances
  - to distribute any reasonable collection of unequal instances uniformly
  across all `int` values
- recipe for computing hash code:
  - declare an `int` variable named `result`, and initialize it to the hash code `c` 
  for the 1st significant field in our object, as computed in the next step
  - compute an `int` hash code `c` for the field:
    - if the field is of primitive type, compute `Type#hashCode(f)`, where `Type`
    is the boxed primitive type
    - if the field is an object reference and this class's `equals` method
    compares the field by recursively invoking `equals`, recursively invoke `hashCode`
    on the field
      - if the value of the field is `null`, use 0
    - if the field is an array, treat it as each significant element were a separate field
      - use `Arrays#hashCode`
      - if the array has no significant elements, use a constant, preferably not 0
  - `result = 31 * result + c`
- *must* exclude any fields that are not used in `equals` comparisons, or risk
  violating the 2nd provision of the `hashCode` contract
- the value 31 was chosen because it is an odd prime
  - the advantage of using a prime is less clear, but it is traditional
  - a nice property of 31 is that the multiplication can be replaced by a shift and
  a subtraction for better performance on some architectures: `31 * i == (i<<5) - i`
  - modern VMs do this sort of optimization automatically
- static `Objects#hash` run more slowly because they entail array creation to pass a
  variable number of arguments, as well as boxing and unboxing if any of the arguments are
  of primitive type
- if a class is immutable and the cost of computing the hash code is significant, might consider
  caching the hash code in the object rather than recalculating it each time it is requested
- do *not* be tempted to exclude significant fields from the hash code computation to
  improve performance
- do *not* provide a detailed specification for the value returned by `hashCode`, so clients
  cannot reasonably depend on it; this gives us the flexibility to change it
  - `String` and `Integer` specify the exact value returned by their `hashCode` method as a function
  of the instance value
  - impedes the ability to improve the hash function in future releases
