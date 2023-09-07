package com.koodipalvelu.airiot.fi.setupcompare.model.carselector;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrackListForCarRequest {

    private String carFolderName;

}
