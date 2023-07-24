package com.koodipalvelu.airiot.fi.setupcompare.rest;

import com.koodipalvelu.airiot.fi.setupcompare.model.diff.Data;
import com.koodipalvelu.airiot.fi.setupcompare.service.SetupsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/setups")
public class SetupController {

    private final SetupsService service;

    public SetupController(SetupsService service) throws IOException {
        this.service = service;
        service.readIniFiles();
    }

    @GetMapping
    public ResponseEntity<?> getSetups() {
        return ResponseEntity.ok(service.getResultsMap());
    }

    @GetMapping("/carList")
    public ResponseEntity<?> getCarList() {
        return ResponseEntity.ok(service.getCarList());
    }

    private Data getData() {
        Data data = new Data();
        return data;
    }

}
