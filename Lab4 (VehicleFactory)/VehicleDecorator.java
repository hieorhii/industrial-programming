package org.vehicles;

public abstract class VehicleDecorator extends Vehicle {
    protected Vehicle decoratedVehicle;

    public VehicleDecorator(Vehicle decoratedVehicle) {
        super(decoratedVehicle.getId(), decoratedVehicle.getType(),
                decoratedVehicle.getCapacity(), decoratedVehicle.getSpeed(),
                decoratedVehicle.getPrice());
        this.decoratedVehicle = decoratedVehicle;
    }

    @Override
    public String toString() {
        return decoratedVehicle.toString();
    }
}
