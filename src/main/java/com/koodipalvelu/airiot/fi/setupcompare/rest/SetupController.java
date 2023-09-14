package com.koodipalvelu.airiot.fi.setupcompare.rest;

import com.koodipalvelu.airiot.fi.setupcompare.model.carselector.SetupListForCarAndTrackRequest;
import com.koodipalvelu.airiot.fi.setupcompare.model.carselector.TrackListForCarRequest;
import com.koodipalvelu.airiot.fi.setupcompare.service.SetupsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static com.koodipalvelu.airiot.fi.setupcompare.config.StaticConfig.AC_CONFIG_KEYS_MAP_FILE;
import static com.koodipalvelu.airiot.fi.setupcompare.config.StaticConfig.AC_SETUP_LOCAL_BASE_DIR;

@RestController
@RequestMapping("/api/setups")
public class SetupController {


    private final SetupsService service;

    public SetupController(SetupsService service) throws IOException {
        super();
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> getSetups() {
        return ResponseEntity.ok(service.getResultsMap());
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/carList")
    public ResponseEntity<?> getCarList() {
        return ResponseEntity.ok(service.getCarList());
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/car-list-for-selection")
    public ResponseEntity<?> getCarListForSelection() {
        return ResponseEntity.ok(service.getCarListForSelection());
    }


    @CrossOrigin(origins = "*")
    @PostMapping("/track-list-for-car")
    public ResponseEntity<?> getTrackListForCar(@RequestBody TrackListForCarRequest request) {
        return ResponseEntity.ok(service.getTrackListForCar(request.getCarFolderName()));
    }


    @CrossOrigin(origins = "*")
    @PostMapping("/setup-list-for-car-and-track")
    public ResponseEntity<?> getSetupListForCarAndTrack(@RequestBody SetupListForCarAndTrackRequest request) {
        return ResponseEntity.ok(service.getSetupListForCarAndTrack(request.getCarFolderName(), request.getTrackFolderName()));
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/scan-for-setup-ini-files")
    public ResponseEntity<?> scanForSetupIniFiles() throws IOException {
        return ResponseEntity.ok(service.scanForSetupIniFiles(AC_CONFIG_KEYS_MAP_FILE, AC_SETUP_LOCAL_BASE_DIR));
    }


}
