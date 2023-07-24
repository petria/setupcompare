package com.koodipalvelu.airiot.fi.setupcompare.model.scan;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class Track {

    private String trackName;

    private String trackFolderName;

    private Map<String, String> iniFilesMap = new HashMap<>();


}
