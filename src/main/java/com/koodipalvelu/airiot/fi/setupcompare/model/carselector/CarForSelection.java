package com.koodipalvelu.airiot.fi.setupcompare.model.carselector;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CarForSelection {

    private long id;
    private long carTracksWithSetup;
    private String carName;
    private String carFolderName;


}
