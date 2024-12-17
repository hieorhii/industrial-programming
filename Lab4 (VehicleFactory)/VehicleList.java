package org.vehicles;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class VehicleList extends VehicleCollection {

    public VehicleList() {
    }
    private List<Vehicle> vehicles = new ArrayList<>();

    @Override
    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
    }

    @Override
    public void removeVehicle(int id) {
        vehicles.removeIf(h -> h.getId() == id);
    }

    @Override
    public List<Vehicle> getAllVehicles() {
        return vehicles;
    }
}
