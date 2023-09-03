package com.koodipalvelu.airiot.fi.setupcompare.model.carselector;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CarForSelection {

    private String value;
    private String label;

}
