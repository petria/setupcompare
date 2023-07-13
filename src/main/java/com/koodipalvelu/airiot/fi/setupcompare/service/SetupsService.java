package com.koodipalvelu.airiot.fi.setupcompare.service;

import com.koodipalvelu.airiot.fi.setupcompare.compare.SetupIniComparator;
import com.koodipalvelu.airiot.fi.setupcompare.reader.SetupFilesReader;
import com.koodipalvelu.airiot.fi.setupcompare.reader.SetupScanResults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class SetupsService {

    private static final String AC_SETUP_LOCAL_BASE_DIR = "/Users/petria/code/github/setupcompare/ACsetups/setups";
    private static final String AC_CONFIG_KEYS_MAP_FILE = "/Users/petria/code/github/setupcompare/ACsetups/config_keys_mapping.ini";


    private final SetupFilesReader reader;

    private Map<String, String> configKeyMapping;
    private Map<String, SetupScanResults> resultsMap = new HashMap<>();

    public SetupsService(SetupFilesReader reader) throws IOException {
        this.reader = reader;
        readIniFiles();
    }


    private void readIniFiles() throws IOException {
        long start = System.currentTimeMillis();
        configKeyMapping = reader.readConfigKeysMappingIniFile(AC_CONFIG_KEYS_MAP_FILE);

        String scanDir = AC_SETUP_LOCAL_BASE_DIR;
        log.debug("Scan dir   : {}", scanDir);

        List<File> dirNames = reader.scanForFolders(scanDir);
//        Assertions.assertNotNull(dirNames);

        Set<String> carDirNames = new HashSet<>();
        Set<String> trackDirNames = new HashSet<>();
        Set<String> uniqueSetupFiles = new HashSet<>();

        for (File carFile : dirNames) {
            List<File> files = reader.scanForFolders(carFile.getAbsolutePath());
            for (File file : files) {
                String path = file.getAbsolutePath();
                String[] split = path.split("/");

                String car = split[split.length - 2];
                String track = split[split.length - 1];

                carDirNames.add(car);
                trackDirNames.add(track);
                String key = String.format("%s__%s", car, track);

                List<File> iniFiles = reader.scanForIniFiles(path);
                SetupScanResults results
                        = SetupScanResults.builder()
                        .carFolder(car)
                        .trackFolder(track)
                        .iniFilePath(new ArrayList<>())
                        .build();
                for (File iniFile : iniFiles) {
                    results.getIniFilePath().add(iniFile.getAbsolutePath());
                    uniqueSetupFiles.add(iniFile.getAbsolutePath());
                }
/*                if (results.getIniFilePath().size() > 2) {
                    log.debug("{} - {}", results.getIniFilePath().size(), key);
                }*/
                resultsMap.put(key, results);

            }

        }

//
/*        Set<String> uniqueConfigKeys = new TreeSet<>();
        for (String iniFilePath : uniqueSetupFiles) {
            List<String> lines = reader.readSetupFile(iniFilePath);
            Map<String, String> parsed = reader.parseValues(lines);
            uniqueConfigKeys.addAll(parsed.keySet());
        }*/
/*        for (String uniqueKey : uniqueConfigKeys) {
            System.out.printf("%s\n", uniqueKey);
        }*/

        log.debug("Setup INIs : {}", uniqueSetupFiles.size());
        log.debug("Car dirs   : {}", carDirNames.size());
        log.debug("Track dirs : {}", trackDirNames.size());
        log.debug("Scan time  : {} ms", System.currentTimeMillis() - start);


    }


    private void compare() throws IOException {
        SetupScanResults scanResults = resultsMap.get("ks_porsche_911_gt1__monza");
        log.debug("Compare    : {}", scanResults);
        int idx = 0;
        for (String iniFile : scanResults.getIniFilePath()) {
            log.debug("ini        : {} = {}", idx, iniFile);
            idx++;
        }
        String iniBase = scanResults.getIniFilePath().get(0);
        String iniCompare = scanResults.getIniFilePath().get(1);
        Map<String, String> baseValues = reader.parseValues(reader.readSetupFile(iniBase));
        Map<String, String> otherValues = reader.parseValues(reader.readSetupFile(iniCompare));
        SetupIniComparator comparator = new SetupIniComparator(configKeyMapping);
        comparator.compare(baseValues, otherValues);

    }


    public Map<String, String> getConfigKeyMapping() {
        return configKeyMapping;
    }

    public Map<String, SetupScanResults> getResultsMap() {
        return resultsMap;
    }
}
