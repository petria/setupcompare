package com.koodipalvelu.airiot.fi.setupcompare.model.scan;


import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Builder
@Data
public class SetupScanResults {

    String carFolder;
    String trackFolder;

    List<String> iniFilePath;
    private Map<String, String> iniFilesMap;

}
