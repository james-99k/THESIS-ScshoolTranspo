package com.example.schooltranspo;

public class Operator {

    String DriverName;
    String Vehicles;
    String OperatorID;
    String vehicleID;

    public Operator(){
    }

    public Operator(String driverName, String vehicles, String operatorID, String vehicleID) {
        DriverName = driverName;
        Vehicles = vehicles;
        OperatorID = operatorID;
        this.vehicleID = vehicleID;
    }

    public String getDriverName() {
        return DriverName;
    }

    public void setDriverName(String driverName) {
        DriverName = driverName;
    }

    public String getVehicles() {
        return Vehicles;
    }

    public void setVehicles(String vehicles) {
        Vehicles = vehicles;
    }

    public String getOperatorID() {
        return OperatorID;
    }

    public void setOperatorID(String operatorID) {
        OperatorID = operatorID;
    }

    public String getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(String vehicleID) {
        this.vehicleID = vehicleID;
    }
}
