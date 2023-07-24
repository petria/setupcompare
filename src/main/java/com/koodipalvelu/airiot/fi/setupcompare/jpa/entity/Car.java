package com.koodipalvelu.airiot.fi.setupcompare.jpa.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "car")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "acFolderName")
    private String acFolderName;

    @Column(name = "acceleration")
    private String acceleration;

    @Column(name = "top_speed")
    private String topSpeed;

    @Column(name = "torque")
    private String torque;

    @Column(name = "weight")
    private String weight;

    @Column(name = "power_ratio")
    private String powerRatio;

    @Column(name = "brake_horse_power")
    private String brakeHorsePower;

    @Column(name = "brand")
    private String brand;

    @Column(name = "car_class")
    private String carClass;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAcFolderName() {
        return acFolderName;
    }

    public void setAcFolderName(String acFolderName) {
        this.acFolderName = acFolderName;
    }

    public String getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(String acceleration) {
        this.acceleration = acceleration;
    }

    public String getTopSpeed() {
        return topSpeed;
    }

    public void setTopSpeed(String topSpeed) {
        this.topSpeed = topSpeed;
    }

    public String getTorque() {
        return torque;
    }

    public void setTorque(String torque) {
        this.torque = torque;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getPowerRatio() {
        return powerRatio;
    }

    public void setPowerRatio(String powerRatio) {
        this.powerRatio = powerRatio;
    }

    public String getBrakeHorsePower() {
        return brakeHorsePower;
    }

    public void setBrakeHorsePower(String brakeHorsePower) {
        this.brakeHorsePower = brakeHorsePower;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCarClass() {
        return carClass;
    }

    public void setCarClass(String carClass) {
        this.carClass = carClass;
    }
}
