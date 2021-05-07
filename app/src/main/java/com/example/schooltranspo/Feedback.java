package com.example.schooltranspo;

public class Feedback {

    String operatorID, passengerID, vehivleID, driverID, comment;
    public Feedback() {
    }

    public Feedback(String operatorID, String passengerID, String vehivleID, String driverID, String comment) {
        this.operatorID = operatorID;
        this.passengerID = passengerID;
        this.vehivleID = vehivleID;
        this.driverID = driverID;
        this.comment = comment;
    }

    public String getOperatorID() {
        return operatorID;
    }

    public void setOperatorID(String operatorID) {
        this.operatorID = operatorID;
    }

    public String getPassengerID() {
        return passengerID;
    }

    public void setPassengerID(String passengerID) {
        this.passengerID = passengerID;
    }

    public String getVehivleID() {
        return vehivleID;
    }

    public void setVehivleID(String vehivleID) {
        this.vehivleID = vehivleID;
    }

    public String getDriverID() {
        return driverID;
    }

    public void setDriverID(String driverID) {
        this.driverID = driverID;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
