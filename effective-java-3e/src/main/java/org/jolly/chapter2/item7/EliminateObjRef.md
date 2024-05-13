## Item 7 - eliminate obsolete object references

- an obsolete reference is simply a reference that will never be dereferenced
again
- null out references once they become obsolete
- nulling out object references should be the exception not the norm
  - the best way to eliminate an obsolete reference is to let the variable
  that contained the reference fall out of scope
  - this occurs naturally if we define each variable in the narrowest 
  possible scope
- a class is susceptible to memory leaks if it *manages its own memory*
  - the garbage collector has no way of knowing which object references
  are inactive and *free*
  - all the object references are treated equally
  valid within the defined scope
  - only the programmer knows that the inactive portion is unimportant and
  should be freed
- another common source of memory leaks is caches
  - cache should occasionally be cleansed of entries that have fallen into
  disuse
  - this can be done by a background thread (perhaps a `ScheduledThreadPoolExecutor`)
  or as a side effect of adding new entries to the cache
    - e.g. `LinkedHashMap#removeEldestEntry`
  - if implemented a cache for which an entry is relevant exactly so long there
  are references to its key outside the cache, represent the cache as a
  `WeakHashMap`
    - entries will be automatically removed after they become obsolete
- another common source of memory leaks is callbacks and listeners
  - to ensure callbacks are garbage collected promptly is to store only
  *weak references* to them
    - e.g. storing them only as keys in a `WeakHashMap`
- memory leaks typically do not manifest themselves as obvious failures
  - typically discovered only as a result of careful code inspection or
  with the aid of a *heap profiler*
