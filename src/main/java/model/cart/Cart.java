package model.cart;

import model.bean.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class Cart {

    private List<CartItem> items = new ArrayList<>();

    public void addVehicle(Vehicle vehicle) {
        if (!containsVehicle(vehicle.getId())) {
            items.add(new CartItem(vehicle));
        }
    }

    public void removeVehicle(int vehicleId) {
        items.removeIf(item -> item.getVehicle().getId() == vehicleId);
    }

    public boolean containsVehicle(int vehicleId) {
        for (CartItem item : items) {
            if (item.getVehicle().getId() == vehicleId) {
                return true;
            }
        }
        return false;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public double getTotal() {
        double total = 0;

        for (CartItem item : items) {
            total += item.getPrice();
        }

        return total;
    }

    public boolean hasItems() {
        return items.isEmpty();
    }
}