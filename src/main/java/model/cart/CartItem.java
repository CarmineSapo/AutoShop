package model.cart;

import model.bean.Vehicle;

public class CartItem {

    private Vehicle vehicle;

    public CartItem(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public double getPrice() {
        return vehicle.getPrice();
    }
}