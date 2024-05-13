package org.jolly.ch4;

import net.jcip.annotations.ThreadSafe;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@ThreadSafe
public class DelegatingVehicleTracker {
    private final ConcurrentMap<String, ImmutablePoint> locations;
    private final Map<String, ImmutablePoint> unmodifiableMap;

    public DelegatingVehicleTracker(Map<String, ImmutablePoint> points) {
        this.locations = new ConcurrentHashMap<>(points);
        this.unmodifiableMap = Collections.unmodifiableMap(points);
    }

    // live view of vehicle locations
    public Map<String, ImmutablePoint> getLocations() {
        return unmodifiableMap;
    }

    // shallow copy, static copy of locations instead of live
//    public Map<String, ImmutablePoint> getLocations() {
//        return Collections.unmodifiableMap(new HashMap<>(locations));
//    }

    public ImmutablePoint getLocation(String id) {
        return locations.get(id);
    }

    public void setLocation(String id, int x, int y) {
        if (locations.replace(id, new ImmutablePoint(x, y)) == null) {
            throw new IllegalArgumentException("invalid vehicle name: " + id);
        }
    }
}

