package com.koodipalvelu.airiot.fi.setupcompare.model.carselector;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class TrackListForCarResponse {

    private List<TrackForCarSelection> trackList;

}
