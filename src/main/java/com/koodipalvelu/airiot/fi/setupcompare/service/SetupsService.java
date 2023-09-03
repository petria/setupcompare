package com.koodipalvelu.airiot.fi.setupcompare.service;

import com.koodipalvelu.airiot.fi.setupcompare.compare.SetupIniComparator;
import com.koodipalvelu.airiot.fi.setupcompare.model.carselector.CarForSelection;
import com.koodipalvelu.airiot.fi.setupcompare.model.scan.Car;
import com.koodipalvelu.airiot.fi.setupcompare.model.scan.SetupScanResults;
import com.koodipalvelu.airiot.fi.setupcompare.model.scan.Track;
import com.koodipalvelu.airiot.fi.setupcompare.reader.SetupFilesReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SetupsService {

    private static final String AC_SETUP_LOCAL_BASE_DIR = "/Users/petria/code/github/setupcompare/ACsetups/setups_oscar";
    private static final String AC_CONFIG_KEYS_MAP_FILE = "/Users/petria/code/github/setupcompare/ACsetups/config_keys_mapping.ini";


    private final SetupFilesReader reader;

    private Map<String, String> configKeyMapping;
    private Map<String, SetupScanResults> resultsMap = new HashMap<>();

    private Map<String, Car> setupsMap = new HashMap<>();

    private long carIdCounter = 0;
    private long trackIdCounter = 0;

    private long iniIdCounter = 0;

    public SetupsService(SetupFilesReader reader) throws IOException {
        this.reader = reader;
//        readIniFiles();
    }


    public void readIniFiles() throws IOException {
        long start = System.currentTimeMillis();

        this.carIdCounter = 0;
        this.trackIdCounter = 0;
        this.iniIdCounter = 0;

        configKeyMapping = reader.readConfigKeysMappingIniFile(AC_CONFIG_KEYS_MAP_FILE);

        String scanDir = AC_SETUP_LOCAL_BASE_DIR;
        log.debug("Scan dir   : {}", scanDir);

        List<File> carDirFiles = reader.scanForFolders(scanDir);
//        Assertions.assertNotNull(dirNames);

        Set<String> carDirNames = new HashSet<>();
        Set<String> trackDirNames = new HashSet<>();
        int uniqueSetupFiles = 0;

        for (File carFile : carDirFiles) {
            String carFileName = carFile.getName();

            Car carModel = this.setupsMap.get(carFileName);
            if (carModel == null) {
                this.carIdCounter++;
                carModel = new Car();
                carModel.setId(carIdCounter);
                carModel.setCarName(carFileName);
                carModel.setCarFolderName(carFileName);
                this.setupsMap.put(carFileName, carModel);
            }


            List<File> trackFiles = reader.scanForFolders(carFile.getAbsolutePath());
            for (File file : trackFiles) {
                String path = file.getAbsolutePath();
                String[] split = path.split("/");

                String car = split[split.length - 2];
                String track = split[split.length - 1];

                carDirNames.add(car);
                trackDirNames.add(track);
                String key = String.format("%s__%s", car, track);

                String[] iniFiles = reader.scanForIniFiles(path);
                SetupScanResults results
                        = SetupScanResults.builder()
                        .carFolder(car)
                        .trackFolder(track)
                        .iniFilePath(new ArrayList<>())
                        .build();

                for (String iniFile : iniFiles) {
                    uniqueSetupFiles++;
                    Track trackModel = carModel.getTracksWithSetup().get(track);
                    if (trackModel == null) {
                        this.trackIdCounter++;
                        trackModel = new Track();
                        trackModel.setId(this.trackIdCounter);
                        trackModel.setTrackFolderName(track);
                        trackModel.setTrackName(track);
                        carModel.getTracksWithSetup().put(track, trackModel);
                    }
                    trackModel.getIniFilesMap().put(iniFile, iniFile);
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

        log.debug("Setup INIs : {}", uniqueSetupFiles);
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

    public Map<String, Car> getSetupsMap() {
        return setupsMap;
    }

    public List<Car> getCarList() {
        List<Car> carList = new ArrayList<>(this.setupsMap.values());
        carList.sort(Comparator.comparing(Car::getCarName));
        return carList;
    }

    public List<CarForSelection> getCarListForSelection() {

        List<Car> carList = getCarList();
        List<CarForSelection> collect = carList.stream().map(this::convertToCarForSelection).collect(Collectors.toList());

        return collect;
    }

    private CarForSelection convertToCarForSelection(Car car) {
        CarForSelection forSelection
                = CarForSelection.builder()
                .value(car.getId() + "")
                .label((car.getCarName()))
                .build();
        return forSelection;
    }
}
