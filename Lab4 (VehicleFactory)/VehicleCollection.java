package org.vehicles;

import java.util.ArrayList;
import java.util.List;

public class VehicleCollection {
    private List<Vehicle> vehicles = new ArrayList<>();

    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
    }

    public void removeVehicle(int id) {
        vehicles.removeIf(h -> h.getId() == id);
    }

    public List<Vehicle> getAllVehicles() {
        return vehicles;
    }

    public Vehicle getVehicleById(int id) {
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getId() == id) {
                return vehicle;
            }
        }
        return null;
    }
}
