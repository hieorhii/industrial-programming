package org.vehicles;

public class WarrantyDecorator extends VehicleDecorator {
    private double warrantyCost;

    public WarrantyDecorator(Vehicle decoratedVehicle, double warrantyCost) {
        super(decoratedVehicle);
        this.warrantyCost = warrantyCost;
    }

    @Override
    public double getPrice() {
        return decoratedVehicle.getPrice() + warrantyCost;
    }

    @Override
    public String toString() {
        return super.toString() + ", Warranty Cost=" + warrantyCost;
    }
}
