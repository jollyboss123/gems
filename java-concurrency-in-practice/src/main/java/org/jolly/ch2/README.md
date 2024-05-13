# thread safety
1. writing thread-safe code is, at its core, about managing access to state, and in particular to shared, mutable state
    1. shared
        1. a variable that could be accessed by multiple threads
    2. mutable
        1. the variable’s value could change during its lifetime
2. protect data from uncontrolled concurrent access
    1. 3 ways to do this:
        1. don’t share the state variable across threads
        2. make the state variable immutable
        3. use synchronization whenever accessing the state variable
3. the primary mechanism for synchronization in Java is the `synchronized` keyword, which provides exclusive locking
4. the term “synchronization” also includes the use of `volatile` variables, explicit locks, and atomic variables
5. when designing thread-safe classes, good object-oriented techniques - encapsulation, immutability, and clear specification of invariants - are our best friends
6. pursue optimization only if our performance measurements and requirements tell us that we must, and if those same measurements tell us that our optimizations actually made a difference under realistic conditions
    1. because concurrency bugs are so difficult to reproduce and debug
    2. the benefit of a small performance gain on some infrequently used code path may well be dwarfed by the risk that the program will fail in the field

---

## what is thread safety?

1. a class is thread-safe if it behaves correctly when accessed from multiple threads
    1. regardless of the scheduling or interleaving of the execution of those threads by the runtime environment
    2. and with no additional synchronization or other coordination on the part of the calling code
2. thread-safe classes encapsulate any needed synchronization so that clients need not provide their own

```java
@ThreadSafe
public class StatelessFactorizer implements Servlet {
    public void service(ServletRequest req, ServletResponse resp) {
        BigInteger i = extractFromRequest(req);
        BigInteger[] factors = factor(i);
        encodeIntoResponse(resp, factors);
	} 
}
```

1. stateless objects are always thread-safe
2. the transient state for a particular computation exists solely in local variables that are stored on the thread’s stack and are accessible only to the executing thread

---

## atomicity

```java
@NotThreadSafe
public class UnsafeCountingFactorizer implements Servlet {
    private long count = 0;
    public long getCount() { return count; }
    public void service(ServletRequest req, ServletResponse resp) {
        BigInteger i = extractFromRequest(req);
        BigInteger[] factors = factor(i);
        ++count;
        encodeIntoResponse(resp, factors);
    }
}
```

1. this is susceptible to lost updates
2. while the increment operation `++count`, may look like a single action because of its compact syntax, it is not atomic, which means that it does not execute as a single, indivisible operation
3. this is an example of a read-modify-write operation
    1. the resulting state is derived from the previous state
4. this could cause data integrity problems i.e. possibility of stale data

### race conditions

1. often confused with the related term data race
    1. data race arises when synchronization is not used to coordinate all access to a shared non-final field
    2. we risk a data race whenever a thread writes a variable that might next be read by another thread or reads a variable that might have been written by another thread if both threads do not use synchronization
    3. not all race conditions are data races, and not all data races are race conditions
2. the most common type → check-then-act
    1. a potentially stale observation is used to make a decision on what to do next
    2. a common idiom → lazy initialization

    ```java
    @NotThreadSafe
    public class LazyInitRace {
        private ExpensiveObject instance = null;
        public ExpensiveObject getInstance() {
            if (instance == null) 
    			    instance = new ExpensiveObject();
    				return instance;
    		}
    }
    ```


### compound actions

1. operations A and B are atomic with respect to each other if, from the perspective of a thread executing A, when another thread executes B, either all of B has executed or none of it has
2. an atomic operation is one that is atomic with respect to all operations, including itself, that operate on the same state
3. refer collectively to check-then-act and read-modify-write sequences as compound action
    1. sequences of operation that must be executed atomically in order to remain thread-safe

```java
@ThreadSafe
public class CountingFactorizer implements Servlet {
    private final AtomicLong count = new AtomicLong(0);
    public long getCount() { return count.get(); }
    public void service(ServletRequest req, ServletResponse resp) {
        BigInteger i = extractFromRequest(req);
        BigInteger[] factors = factor(i);
        count.incrementAndGet();
        encodeIntoResponse(resp, factors);
    }
}
```

1. the `java.util.concurrent.atomic` package contains atomic variable classes for effecting atomic state transitions on numbers and object references
2. when a single element of state is added to a stateless class, the resulting class will be thread-safe if the state is entirely managed by a thread-safe object
3. where practical, use existing thread-safe objects to manage our class’s state
    1. it is simpler to reason about the possible states and state transitions for existing thread-safe objects than it is for arbitrary state variables
    2. this makes it easier to maintain and verify thread-safety

---

## locking

1. what if we want to add more state to our class, can we just add more thread-safe state variables?

```java
@NotThreadSafe
public class UnsafeCachingFactorizer implements Servlet {
     private final AtomicReference<BigInteger> lastNumber
         = new AtomicReference<BigInteger>();
     private final AtomicReference<BigInteger[]>  lastFactors
         = new AtomicReference<BigInteger[]>();
     public void service(ServletRequest req, ServletResponse resp) {
         BigInteger i = extractFromRequest(req);
         if (i.equals(lastNumber.get()))
             encodeIntoResponse(resp,  lastFactors.get() );
         else {
		        BigInteger[] factors = factor(i);
						lastNumber.set(i);
						lastFactors.set(factors);
						encodeIntoResponse(resp, factors);
					}
			}
}
```

1. even though the atomic references are individually thread-safe, this class has race conditions that could make it produce the wrong answer
2. the definition of thread safety requires that invariants be preserved regardless of timing or interleaving of operations in multiple threads
    1. 1 invariant of this class is that the product of the factors cached in `lastFactors` equal the value cached in `lastNumber`, the class is only correct if this invariant always hold
3. when multiple variables participate in an invariant, they are not independent
    1. the value of 1 constrains the allowed value(s) of the others
    2. when updating 1, must update the others in the same atomic operation
4. to preserve state consistency, update related state variables in a single atomic operation

### intrinsic locks

```java
synchronized (lock) {
    // Access or modify shared state guarded by lock
}
```

1. Java built-in locking mechanism for enforcing atomicity
2. every Java object can implicitly act as a lock for purposes of synchronization i.e. intrinsic/monitor locks
    1. the lock is automatically acquired by the executing thread before entering a `synchronized` block and automatically released when control exits the `synchronized` block
3. intrinsic locks in Java act as mutexes (mutual exclusive locks)
    1. i.e. at most only 1 thread may own the lock
4. in the context of concurrency, atomicity means the same thing as it does in transactional applications
    1. a group of statements appear to execute as a single, indivisible unit
5. this approach is fairly extreme, since it inhibits multiple clients from using the class simultaneously at all → poor responsiveness → performance problem

```java
@ThreadSafe
public class SynchronizedFactorizer implements Servlet {
    @GuardedBy("this") private BigInteger lastNumber;
    @GuardedBy("this") private BigInteger[] lastFactors;
    public synchronized void service(ServletRequest req,
                                     ServletResponse resp) {
        BigInteger i = extractFromRequest(req);
        if (i.equals(lastNumber))
            encodeIntoResponse(resp, lastFactors);
        else {
	        BigInteger[] factors = factor(i);
					lastNumber = i;
					lastFactors = factors;
					encodeIntoResponse(resp, factors);
        } 
    }
}
```

### reentrancy

1. when a thread requests a lock that is already held by another thread, the requesting thread blocks
2. but because intrinsic locks are reentrant
    1. if a thread tries to acquire a lock that it already holds, the request succeeds
3. reentrancy means that locks are acquired on a per-thread rather than per-invocation basis
    1. this differs from the default locking behavior for pthreads (POSIX threads) mutexes, which are granted on a per-invocation basis
4. reentrancy is implemented by associating with each lock an acquisition count and an owning thread
5. reentrancy facilitates encapsulation of locking behavior, and thus simplifies the development of object-oriented concurrent code

```java
public class Widget {
    public synchronized void doSomething() {
... }
}
public class LoggingWidget extends Widget {
    public synchronized void doSomething() {
        System.out.println(toString() + ": calling doSomething");
        super.doSomething();
    }
}
```

---

## guarding state with locks

1. just wrapping the compound action with a `synchronized` block is not sufficient
    1. if synchronization is used to coordinate access to a variable, it is needed everywhere that variable is accessed
    2. the same lock must be used wherever that variable is accessed
2. for each mutable state variable that may be accessed by more than 1 thread, all accesses to that variable must be performed with the same lock held
    1. that variable is guarded by that lock
3. acquiring the lock associated with an object does not prevent other threads from accessing that object
    1. the only thing acquiring a lock prevents any other thread from doing is acquiring that same lock
    2. the fact that every object has a built-in lock is just a convenience so that we need not explicitly create lock objects
4. every shared, mutable variable should be guarded by exactly 1 lock
    1. make it clear to maintainers which lock that is
5. a common locking convention is to encapsulate all mutable state within an object and to protect it from concurrent access by synchronizing any code path that accesses mutable state using the object’s intrinsic lock
    1. all the variables in an object’s state are guarded by the object’s intrinsic lock
    2. it is easy to subvert this locking protocol accidentally by adding a new method or code path and forgetting to use synchronization
6. for every invariant that involves more than 1 variable, all the variables involved in that invariant must be guarded by the same lock
7. while synchronized methods can make individual operations atomic, additional locking is required → when multiple operations are combined into a compound action
8. synchronizing every method can lead to liveness or performance problems

---

## liveness and performance

1. can improve the concurrency of the class while maintaining thread safety by narrowing the scope of the `synchronized` block
    1. would not want to divide an operation that should be atomic into more than 1 `synchronized` block
    2. reasonable to try to exclude from `synchronized` blocks long-running operations that do not affect shared state

```java
@ThreadSafe
public class CachedFactorizer implements Servlet {
    @GuardedBy("this") private BigInteger lastNumber;
    @GuardedBy("this") private BigInteger[] lastFactors;
    @GuardedBy("this") private long hits;
    @GuardedBy("this") private long cacheHits;
    public synchronized long getHits() { return hits; }
    public synchronized double getCacheHitRatio() {
        return (double) cacheHits / (double) hits;
    }
    public void service(ServletRequest req, ServletResponse resp) {
        BigInteger i = extractFromRequest(req);
        BigInteger[] factors = null;
        synchronized (this) {
            ++hits;
            if (i.equals(lastNumber)) {
                ++cacheHits;
                factors = lastFactors.clone();
            }
        }
        if (factors == null) {
            factors = factor(i);
            synchronized (this)  {
                lastNumber = i;
                lastFactors = factors.clone();
            }
				}
        encodeIntoResponse(resp, factors);
    }
}
```

1. restructures the class to use 2 separate `synchronized` blocks
    1. 1 guards the check-then-act sequence that tests whether we can just return the cached result
    2. the other guards updating both the cached number and the cached factors
2. the portions of code that are outside the `synchronized` blocks operate exclusively on local (stack-based) variables, which are not shared across threads and therefore do not require synchronization
3. acquiring and releasing a lock has some overhead, so it is undesirable to break down `synchronized` blocks too far, even if this would not compromise atomicity
    1. e.g. factoring `++hits` into its own `synchronized` block
4. there is frequently a tension between simplicity and performance
    1. when implementing a synchronization policy, resist the temptation to prematurely sacrifice simplicity (potentially compromising safety) for the sake of performance
5. avoid holding locks during lengthy computations or operations at risk of not completing quickly such as network or console I/O
