package com.example.appointment.data;


import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "appointment")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "doctor_id", nullable = false)
    private int doctorId;

    @Column(name = "patient_id", nullable = false)
    private int patientId;

    @Column(name = "app_date", nullable = false)
    private String appDate;

    @Column(name = "app_time", nullable = false)
    private String appTime;


    @Column(name = "app_number", nullable = false, unique = false)
    private String appNumber;

    @Column(name = "app_fee", nullable = false)
    private double appFee;

    @Column(name = "status")
    private String status = "PENDING";

    // --- Getters and Setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }



    public String getAppDate() {
        return appDate;
    }

    public void setAppDate(String appDate) {
        this.appDate = appDate;
    }

    public String getAppTime() {
        return appTime;
    }

    public void setAppTime(String appTime) {
        this.appTime = appTime;
    }

    public String getAppNumber() {
        return appNumber;
    }

    public void setAppNumber(String appNumber) {
        this.appNumber = appNumber;
    }

    public double getAppFee() {
        return appFee;
    }

    public void setAppFee(double appFee) {
        this.appFee = appFee;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
