package com.koodipalvelu.airiot.fi.setupcompare.model.carselector;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TrackForCarSelection {

    private long id;

    private String trackName;

    private String trackFolderName;

}
