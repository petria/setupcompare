package com.koodipalvelu.airiot.fi.setupcompare.model.scan;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SetupIniFileScanStats {

    private String scanDir;

    private String configKeyMapFile;

    private int uniqueSetupFiles;
    private int carDirs;
    private int trackDirs;
    private long scanTime;

}
