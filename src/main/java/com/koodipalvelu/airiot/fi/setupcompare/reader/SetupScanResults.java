package com.koodipalvelu.airiot.fi.setupcompare.reader;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class SetupScanResults {

    String carFolder;
    String trackFolder;

    List<String> iniFilePath;

}
