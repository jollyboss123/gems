# intro
1. threads are sometimes called lightweight processes, and most modern operating systems treat threads, not processes, as the basic unit of scheduling
2. since threads share the memory address space of their owning process, all threads within a process have access to the same variables and allocate objects from the same heap
    1. but without explicit synchronisation to coordinate access to shared data, a thread may modify variables that another thread is in the middle of using, with unpredictable results
3. programs with multiple active threads can execute simultaneously on multiple processors
4. a complicated, asynchronous workflow can be decomposed into a number of simpler, synchronous workflows each running in a separate thread, interacting only with each other at specific synchronization points
    1. this benefit is often exploited by frameworks such as servlets or RMI (Remote Method Invocation)
    2. the framework handles the details of request management, thread creation, and load balancing, dispatching portions of the request handling to the appropriate application component at the appropriate point in the work-flow
5. to avoid thread blocking in a single-threaded server, applications are forced to use non-blocking I/O, which is far more complicated and error-prone than synchronous I/O
    1. if each request has its own thread, then blocking does not affect the processing of other requests
    2. operating system support for larger number of threads has improved significantly, making the thread-per-client model practical even for large numbers of clients on some platforms
    3. thus, better OS support for threads means that there are fewer situations for which non-blocking I/O is essential

# risk of threads

## safety hazards

```java
@NotThreadSafe
public class UnsafeSequence {
	private int value;
	
	public int getNext() {
		return value++;
	}
}
```

1. allowing multiple threads to access and modify the same variables introduces an element of non-sequentiality into an otherwise sequential programming model

```java
@ThreadSafe
public class Sequence {
    @GuardedBy("this") private int nextValue;
    public synchronized int getNext() {
        return nextValue++;
	} 
}
```

1. in the absence of synchronisation, the compiler, hardware, and runtime are allowed to take substantial liberties with the timing and order of actions
    1. e.g. caching variables in registers or processor-local caches where they are temporarily (or even permanently) invisible to other threads

## liveness hazards

1. occurs when an activity gets into a state such that it is permanently unable to make forward progress
2. 1 form that can occur in sequential program is an inadvertent infinite loop, where the code that follows the loop never gets executed
3. the use of threads introduces additional liveness risks
    1. e.g. if thread A is waiting for a resource that thread B holds exclusively, and B never releases it, A will wait forever
4. bugs that cause liveness failure can be elusive because they depend on the relative timing of events in different threads, and therefore do not always manifest themselves in development or testing

## performance hazards

1. threads carry some degree of runtime overhead
2. context switches
    1. when the scheduler suspends the active thread temporarily so another thread can run
    2. more frequent in applications with many threads
    3. have significant costs:
        1. saving and restoring execution context
        2. loss of locality
        3. CPU time spent scheduling threads instead of running them
3. when threads share data, they must use synchronization mechanisms:
    1. can inhibit compiler optimizations
    2. flush or invalidate memory caches
    3. create synchronization traffic on the shared memory bus
