package com.koodipalvelu.airiot.fi.setupcompare.jpa.service;

import com.koodipalvelu.airiot.fi.setupcompare.jpa.entity.Car;
import com.koodipalvelu.airiot.fi.setupcompare.jpa.repository.CarRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class CarService {
    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public Map<String, Car> getCarFolderToNameMap() {
        Map<String, Car> map = new HashMap<>();
        carRepository.findAll().forEach(car -> {
            map.put(car.getAcFolderName(), car);
        });
        return map;
    }


}
