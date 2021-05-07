package com.example.schooltranspo;

public class Vehicle {
    private String image;
    private String brand;
    private String model;
    private String color;
    private String capacity;
    private String schoolName;
    private String schoolDestination;
    private String serviceFee;

    public Vehicle(String image, String brand, String model, String color, String capacity, String schoolName, String schoolDestination, String serviceFee) {
        this.image = image;
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.capacity = capacity;
        this.schoolName = schoolName;
        this.schoolDestination = schoolDestination;
        this.serviceFee = serviceFee;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getSchoolDestination() {
        return schoolDestination;
    }

    public void setSchoolDestination(String schoolDestination) {
        this.schoolDestination = schoolDestination;
    }

    public String getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(String serviceFee) {
        this.serviceFee = serviceFee;
    }
}
