package com.koodipalvelu.airiot.fi.setupcompare;

import com.koodipalvelu.airiot.fi.setupcompare.model.scan.Car;
import com.koodipalvelu.airiot.fi.setupcompare.reader.SetupFilesReader;
import com.koodipalvelu.airiot.fi.setupcompare.service.SetupsService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@SpringBootTest
@Slf4j
class SetupcompareApplicationTests {

    static final String AC_SETUP_NET_DRIVE_BASE_DIR = "/Volumes/zstore/Share/Assetto Corsa/setups";
    static final String AC_SETUP_LOCAL_BASE_DIR = "/Users/petria/code/github/setupcompare/ACsetups/setups";

    ///
//    @Autowired
    SetupFilesReader reader;

    @Autowired
    SetupsService service;

  //  @Test
    void test_setup_ini_reader() throws IOException {
        List<String> lines = reader.readSetupFile("/Users/petria/code/github/setupcompare/ACsetups/1.47.6.ini");
        Assertions.assertNotNull(lines);

        Map<String, String> values = reader.parseValues(lines);
        Assertions.assertNotNull(values);
    }

//    @Test
    public void test_setups_service_read_ini_files() throws IOException {
        service.scanForSetupIniFiles(null, null);
        Map<String, Car> setupsMap = service.getSetupsMap();

        List<Car> carList = service.getCarList();

        int foo = 0;
    }
}
