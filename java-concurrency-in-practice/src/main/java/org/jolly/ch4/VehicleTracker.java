package org.jolly.ch4;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.awt.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@ThreadSafe
public class VehicleTracker {
    @GuardedBy("this")
    private final Map<String, Point> locations; // Point is not thread safe

    public VehicleTracker(Map<String, Point> locations) {
        this.locations = deepCopy(locations);
    }

    public synchronized Map<String, Point> getLocations() {
        return deepCopy(locations);
    }

    public synchronized Point getLocation(String id) {
        Point loc = locations.get(id);
        return loc == null ? null : new Point(loc);
    }

    public synchronized void setLocation(String id, int x, int y) {
        Point loc = locations.get(id);
        if (loc == null)
            throw new IllegalArgumentException("No such ID: " + id);
        loc.x = x;
        loc.y = y;
    }

    private static Map<String, Point> deepCopy(Map<String, Point> m) {
        Map<String, Point> result = new HashMap<String, Point>();
        for (String id : m.keySet())
            result.put(id, new Point(m.get(id)));
        return Collections.unmodifiableMap(result);
    }
}

class VehicleMovedEvent {
    Point getNewLocation() {
        return null;
    }

    String getVehicleId() {
        return null;
    }
}

class GUI {
    public static void main(String[] args) {
        VehicleTracker vehicles = new VehicleTracker(Collections.emptyMap());
        Thread viewThread = new Thread(new ViewThread(vehicles));
        Thread updaterThread = new Thread(new UpdaterThread(vehicles, new VehicleMovedEvent()));
        viewThread.start();
        updaterThread.start();
    }
}

class ViewThread implements Runnable {
    private final VehicleTracker vehicles;

    ViewThread(VehicleTracker vehicles) {
        this.vehicles = vehicles;
    }

    @Override
    public void run() {
        Map<String, Point> locations = vehicles.getLocations();
        for (String key : locations.keySet()) {
            renderVehicle(key, locations.get(key));
        }
    }

    private void renderVehicle(String key, Point location) {}
}

class UpdaterThread implements Runnable {
    private final VehicleTracker vehicles;
    private final VehicleMovedEvent evt;

    UpdaterThread(VehicleTracker vehicles, VehicleMovedEvent evt) {
        this.vehicles = vehicles;
        this.evt = evt;
    }

    @Override
    public void run() {
        Point loc = evt.getNewLocation();
        vehicles.setLocation(evt.getVehicleId(), loc.x, loc.y);
    }
}
