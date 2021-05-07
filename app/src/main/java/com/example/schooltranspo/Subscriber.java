package com.example.schooltranspo;

public class Subscriber {
    private String operatorID;
    private String vehicleID;
    private String driverID;
    private String driverName;
    private String passengerID;
    private String Vehicles;
    private String Status;
    private String passengerName;


    public Subscriber() {
    }

    public Subscriber(String operatorID, String vehicleID, String driverID, String driverName, String passengerID, String vehicles, String status, String passengerName) {
        this.operatorID = operatorID;
        this.vehicleID = vehicleID;
        this.driverID = driverID;
        this.driverName = driverName;
        this.passengerID = passengerID;
        Vehicles = vehicles;
        Status = status;
        this.passengerName = passengerName;
    }

    public String getOperatorID() {
        return operatorID;
    }

    public void setOperatorID(String operatorID) {
        this.operatorID = operatorID;
    }

    public String getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(String vehicleID) {
        this.vehicleID = vehicleID;
    }

    public String getDriverID() {
        return driverID;
    }

    public void setDriverID(String driverID) {
        this.driverID = driverID;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getPassengerID() {
        return passengerID;
    }

    public void setPassengerID(String passengerID) {
        this.passengerID = passengerID;
    }

    public String getVehicles() {
        return Vehicles;
    }

    public void setVehicles(String vehicles) {
        Vehicles = vehicles;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }
}
