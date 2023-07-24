package com.koodipalvelu.airiot.fi.setupcompare.jpa.repository;


import com.koodipalvelu.airiot.fi.setupcompare.jpa.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<Driver, Long> {

    Driver findOneByName(String name);

}
