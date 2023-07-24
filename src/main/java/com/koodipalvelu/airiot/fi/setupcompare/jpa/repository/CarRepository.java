package com.koodipalvelu.airiot.fi.setupcompare.jpa.repository;


import com.koodipalvelu.airiot.fi.setupcompare.jpa.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {

    Car findOneByName(String name);


    List<Car> findAllByName(String name);

}
