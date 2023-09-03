package com.koodipalvelu.airiot.fi.setupcompare.model.scan;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class Car {

    private long id;
    private String carName;
    private String carFolderName;
    private Map<String, Track> tracksWithSetup = new HashMap<>();


}
