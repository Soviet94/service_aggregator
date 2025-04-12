package com.aa.service_aggregator.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "service_records")
public class ServiceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 17)
    private String vin;

    @Column(name = "service_date", nullable = false)
    private LocalDate serviceDate;

    private int mileage;

    @Column(name = "service_type")
    private String serviceType;

    @Column(name = "service_cost")
    private double serviceCost;

    // Constructors
    public ServiceRecord() {
    }

    public ServiceRecord(String vin, LocalDate serviceDate, int mileage, String serviceType, double serviceCost) {
        this.vin = vin;
        this.serviceDate = serviceDate;
        this.mileage = mileage;
        this.serviceType = serviceType;
        this.serviceCost = serviceCost;
    }

    public Long getId() {
        return id;
    }

    public String getVin() {
        return vin;
    }

    public LocalDate getServiceDate() {
        return serviceDate;
    }

    public int getMileage() {
        return mileage;
    }

    public String getServiceType() {
        return serviceType;
    }

    public double getServiceCost() {
        return serviceCost;
    }
}