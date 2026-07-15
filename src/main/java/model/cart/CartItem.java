package model.cart;

import model.bean.Vehicle;

public class CartItem {

    private Vehicle vehicle;

    private boolean selected = true;

    public CartItem(Vehicle vehicle) {
        this.vehicle = vehicle;
        this.selected= true;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle){
        this.vehicle = vehicle;
    }

    public double getPrice() {
        return vehicle.getPrice();
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}