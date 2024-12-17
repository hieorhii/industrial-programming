package org.vehicles.utils;

import org.vehicles.Vehicle;

public class ConcreteVehicle extends Vehicle {

    public ConcreteVehicle(int id, String type, int capacity, int speed, double price) {
        super(id, type, capacity, speed, price);
    }

    @Override
    public String toString() {
        return "Vehicle ID: " + getId() + ", Type: " + getType() + ", Capacity: " + getCapacity() +
                ", Speed: " + getSpeed() + ", Price: " + getPrice();
    }
}
