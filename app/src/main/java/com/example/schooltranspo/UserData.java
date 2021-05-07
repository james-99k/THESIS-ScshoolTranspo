package com.example.schooltranspo;

import java.io.Serializable;

public class UserData implements Serializable {

   private String address, contact, email, emergencyContact, fullName, Gender, schoolAddress, userType, license, schoolName, passengerID;

    public UserData() {

    }

    public UserData(String address, String contact, String email, String emergencyContact, String fullName, String gender, String schoolAddress, String userType, String license, String schoolName, String passengerID) {
        this.address = address;
        this.contact = contact;
        this.email = email;
        this.emergencyContact = emergencyContact;
        this.fullName = fullName;
        Gender = gender;
        this.schoolAddress = schoolAddress;
        this.userType = userType;
        this.license = license;
        this.schoolName = schoolName;
        this.passengerID = passengerID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getSchoolAddress() {
        return schoolAddress;
    }

    public void setSchoolAddress(String schoolAddress) {
        this.schoolAddress = schoolAddress;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getPassengerID() {
        return passengerID;
    }

    public void setPassengerID(String passengerID) {
        this.passengerID = passengerID;
    }
}
