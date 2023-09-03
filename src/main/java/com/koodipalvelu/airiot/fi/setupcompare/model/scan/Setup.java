package com.koodipalvelu.airiot.fi.setupcompare.model.scan;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Setup {

    private long id;

    private String setupKey;

    private String setupValue;

    private String setupGroup;

}
