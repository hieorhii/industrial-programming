package org.vehicles;

public class InsuranceDecorator extends VehicleDecorator {
    private double insuranceCost;

    public InsuranceDecorator(Vehicle decoratedVehicle, double insuranceCost) {
        super(decoratedVehicle);
        this.insuranceCost = insuranceCost;
    }

    @Override
    public double getPrice() {
        return decoratedVehicle.getPrice() + insuranceCost;
    }

    @Override
    public String toString() {
        return super.toString() + ", Insurance Cost=" + insuranceCost;
    }
}
