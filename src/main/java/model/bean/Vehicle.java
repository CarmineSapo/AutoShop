package model.bean;

import java.util.ArrayList;
import java.util.List;

public class Vehicle {

    private int id;
    private int dealerId;
    private String brand;
    private String model;
    private int productionYear;
    private int km;
    private String fuelType;
    private String transmission;
    private double price;
    private String description;
    private String status;
    private boolean active;
    private List<String> imagePaths = new ArrayList<>();

    public Vehicle() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getDealerId() {
        return dealerId;
    }

    public void setDealerId(int dealerId) {
        this.dealerId = dealerId;
    }


    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }


    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }


    public int getProductionYear() {
        return productionYear;
    }

    public void setProductionYear(int productionYear) {
        this.productionYear = productionYear;
    }


    public int getKm() {
        return km;
    }

    public void setKm(int km) {
        this.km = km;
    }


    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }


    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }


    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<String> getImagePaths() {
        return imagePaths;
    }

    public void setImagePaths(List<String> imagePaths) {
        this.imagePaths = imagePaths;
    }
}