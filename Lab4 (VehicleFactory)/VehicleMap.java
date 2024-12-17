package org.vehicles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class VehicleMap extends VehicleCollection {
    private Map<Integer, Vehicle> vehicleMap = new HashMap<>();

    @Override
    public void addVehicle(Vehicle vehicle) {
        vehicleMap.put(vehicle.getId(), vehicle);
    }

    @Override
    public void removeVehicle(int id) {
        vehicleMap.remove(id);
    }

    @Override
    public List<Vehicle> getAllVehicles() {
        return new ArrayList<>(vehicleMap.values());
    }
}
