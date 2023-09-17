package com.koodipalvelu.airiot.fi.setupcompare.service;

import com.koodipalvelu.airiot.fi.setupcompare.compare.SetupIniComparator;
import com.koodipalvelu.airiot.fi.setupcompare.model.carselector.*;
import com.koodipalvelu.airiot.fi.setupcompare.model.scan.Car;
import com.koodipalvelu.airiot.fi.setupcompare.model.scan.SetupIniFileScanStats;
import com.koodipalvelu.airiot.fi.setupcompare.model.scan.SetupScanResults;
import com.koodipalvelu.airiot.fi.setupcompare.model.scan.Track;
import com.koodipalvelu.airiot.fi.setupcompare.reader.SetupFilesReader;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.koodipalvelu.airiot.fi.setupcompare.config.StaticConfig.AC_CONFIG_KEYS_MAP_FILE;
import static com.koodipalvelu.airiot.fi.setupcompare.config.StaticConfig.AC_SETUP_LOCAL_BASE_DIR;

@Service
@Slf4j
public class SetupsService {


    private final SetupFilesReader reader;

    private Map<String, String> configKeyMapping;
    private Map<String, SetupScanResults> resultsMap = new HashMap<>();

    private Map<String, Car> setupsMap = new HashMap<>();

    private long carIdCounter = 0;
    private long trackIdCounter = 0;

    private long iniIdCounter = 0;

    public SetupsService(SetupFilesReader reader) throws IOException {
        this.reader = reader;
    }

    @PostConstruct
    public void doInitialScan() {
        try {
            SetupIniFileScanStats stats = scanForSetupIniFiles(AC_CONFIG_KEYS_MAP_FILE, AC_SETUP_LOCAL_BASE_DIR);
            log.debug("Initial scan done: {}", stats);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public synchronized SetupIniFileScanStats scanForSetupIniFiles(String configKeysMapFile, String setupLocalBaseDir) throws IOException {

        SetupIniFileScanStats stats = new SetupIniFileScanStats();
        stats.setConfigKeyMapFile(configKeysMapFile);

        long start = System.currentTimeMillis();

        this.carIdCounter = 0;
        this.trackIdCounter = 0;
        this.iniIdCounter = 0;

        configKeyMapping = reader.readConfigKeysMappingIniFile(AC_CONFIG_KEYS_MAP_FILE);

        String scanDir = setupLocalBaseDir;
        stats.setScanDir(scanDir);

        log.debug("Scan dir   : {}", scanDir);

        List<File> carDirFiles = reader.scanForFolders(scanDir);

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
                if (track.equals("generic")) {
                    // skip generic dir
                    continue;
                }
                carDirNames.add(car);
                trackDirNames.add(track);
                String key = String.format("%s__%s", car, track);

                String[] iniFiles = reader.scanForIniFiles(path);
                SetupScanResults results
                        = SetupScanResults.builder()
                        .carFolder(car)
                        .trackFolder(track)
                        .iniFilePath(new ArrayList<>())
                        .iniFilesMap(new HashMap<>())
                        .build();
                carModel.setIniFileCount(iniFiles.length);

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
                    String pathToIni = path + "/" + iniFile;
                    trackModel.getIniFilesMap().put(iniFile, pathToIni);
                    results.getIniFilesMap().put(iniFile, pathToIni);
                }


                resultsMap.put(key, results);

            }

        }
        long scanTime = System.currentTimeMillis() - start;

        log.debug("Setup INIs : {}", uniqueSetupFiles);
        log.debug("Car dirs   : {}", carDirNames.size());
        log.debug("Track dirs : {}", trackDirNames.size());
        log.debug("Scan time  : {} ms", scanTime);

        stats.setUniqueSetupFiles(uniqueSetupFiles);
        stats.setCarDirs(carDirNames.size());
        stats.setTrackDirs(trackDirNames.size());
        stats.setScanTime(scanTime);

        stats.setUniqueSetupFiles(uniqueSetupFiles);

        return stats;
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

        List<Car> carList = getCarList().stream().filter(car -> car.getIniFileCount() > 0).toList();
        List<CarForSelection> collect = carList.stream().map(this::convertToCarForSelection).collect(Collectors.toList());
        return collect;
    }

    private CarForSelection convertToCarForSelection(Car car) {
        CarForSelection forSelection
                = CarForSelection.builder()
                .id(car.getId())
                .carIniFileCount(car.getIniFileCount())
                .carFolderName(car.getCarFolderName())
                .carName(car.getCarName())
                .build();
        return forSelection;
    }

    public TrackListForCarResponse getTrackListForCar(String carFolderName) {
        TrackListForCarResponse response
                = TrackListForCarResponse.builder()
                .trackList(getTrackByFolderName(carFolderName))
                .build();

        return response;
    }

    private List<TrackForCarSelection> getTrackByFolderName(String carFolderName) {
        Car car = this.setupsMap.get(carFolderName);
        List<TrackForCarSelection> list = new ArrayList<>();
        if (car != null) {
            Stream<String> sorted = car.getTracksWithSetup().keySet().stream().sorted();
            for (String trackName : sorted.toList()) {
                Track track = car.getTracksWithSetup().get(trackName);
                TrackForCarSelection forCarSelection
                        = TrackForCarSelection.builder()
                        .id(track.getId())
                        .trackFolderName(track.getTrackFolderName())
                        .trackName(track.getTrackName())
                        .build();
                list.add(forCarSelection);
            }
        }

        return list;
    }

    public SetupListForCarAndTrackResponse getSetupListForCarAndTrack(String carFolderName, String trackFolderName) {
        List<SetupForCarSelection> list = new ArrayList<>();

        Car car = this.setupsMap.get(carFolderName);
        if (car != null) {
            Track track = car.getTracksWithSetup().get(trackFolderName);
            if (track != null) {
                Stream<String> sorted = track.getIniFilesMap().keySet().stream().sorted();
                int id = 0;
                for (String setupKeyName : sorted.toList()) {
//                    String setupName = track.getIniFilesMap().get(setupKeyName);
                    SetupForCarSelection forCarSelection
                            = SetupForCarSelection.builder()
                            .id(id++)
                            .setupIniFileName(setupKeyName)
                            .build();
                    list.add(forCarSelection);
                }
            }
        }

        SetupListForCarAndTrackResponse response = SetupListForCarAndTrackResponse
                .builder()
                .setupList(list)
                .build();

        return response;
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

    private Map<String, String> getSetupIniValues(String selected) throws IOException {
        String[] split = selected.split(" / ");
        if (split.length != 3) {
            return null;
        }
        String key = String.format("%s__%s", split[0], split[1]);

        SetupScanResults setupScanResults = resultsMap.get(key);
        String iniBase = setupScanResults.getIniFilesMap().get(split[2]);


        Map<String, String> baseValues = reader.parseValues(reader.readSetupFile(iniBase));
        return baseValues;
    }

    public CompareSetupsResponse compareSetups(List<IniSections> iniList) throws IOException {

        List<CompareDifference> differenceList = new ArrayList<>();
        Map<String, String> baseValues = getSetupIniValues(iniList.get(0).getSelected());
        Map<String, String> otherValues = getSetupIniValues(iniList.get(1).getSelected());

        if (baseValues != null && otherValues != null) {
            SetupIniComparator comparator = new SetupIniComparator(configKeyMapping);
            Map<String, List<String>> differenceMap = comparator.compare(baseValues, otherValues);

            Stream<String> sortedKeys = reader.getConfigKeyGroups().keySet().stream().sorted();
            for (String groupKey : sortedKeys.toList()) {

                Set<String> configKeyStrings = reader.getConfigKeyGroups().get(groupKey);
                for (String configKey : configKeyStrings) {
                    //                   log.debug("{} - {}", groupKey, configKey);
                    List<String> difference = differenceMap.get(configKey);
                    if (difference != null) {
                        log.debug("{} - {} - {} <-> {}", groupKey, configKey, difference.get(0), difference.get(1));
                        List<String> list = new ArrayList<>();
                        list.add(groupKey);
                        list.add(configKey);
                        list.add(difference.get(0));
                        list.add(difference.get(1));
                        CompareDifference compareDifference = CompareDifference.builder().differences(list).build();
                        differenceList.add(compareDifference);
                    }
                }
            }
        }
        CompareSetupsResponse response
                = CompareSetupsResponse.builder().differences(differenceList).build();

        return response;
    }
}
