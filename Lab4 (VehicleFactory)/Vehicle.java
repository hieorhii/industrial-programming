package org.vehicles;

import java.util.Objects;

public abstract class Vehicle {
    protected int id;
    protected String type;
    protected int capacity;
    protected int speed;
    protected double price;

    public Vehicle(int id, String type, int capacity, int speed, double price) {
        this.id = id;
        this.type = type;
        this.capacity = capacity;
        this.speed = speed;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", capacity=" + capacity +
                ", speed=" + speed +
                ", price=" + price +
                '}';
    }
}
