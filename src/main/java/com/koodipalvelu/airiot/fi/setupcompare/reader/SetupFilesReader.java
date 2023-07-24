package com.koodipalvelu.airiot.fi.setupcompare.reader;

import lombok.Getter;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SetupFilesReader {

    @Getter
    private Map<String, String> keyToConfigGroup = new HashMap<>();

    public List<String> readSetupFile(String fileName) throws IOException {
        return Files.readAllLines(Path.of(fileName));
    }


    public List<File> scanForFolders(String baseDir) throws IOException {
        List<File> dirs = Files.list(Path.of(baseDir))
                .map(Path::toFile)
                .filter(File::isDirectory)
                .collect(Collectors.toList());
        return dirs;
    }

    public String[] scanForIniFiles(String baseDir) throws IOException {
                File f = new File(baseDir);
        String[] list = f.list((dir, name) -> name.endsWith(".ini"));
        return list;
    }

    public Map<String, String> parseValues(List<String> lines) {
        String value = null;
        String valueName = null;
        Map<String, String> values = new HashMap<>();
        for (String line : lines) {
            line = line.trim();
            if (line.contains("=")) {
                value = line;
            }
            if (line.contains("[")) {
                valueName = line;
            }
            if (valueName != null && value != null) {
                values.put(valueName, value);
                valueName = null;
                value = null;
            }
        }
        return values;
    }

    public Map<String, String> readConfigKeysMappingIniFile(String mappingIniFileName) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(mappingIniFileName));
        for (String line : lines) {
            String[] split = line.split(" ");
            if (split.length != 2) {
                throw new RuntimeException("Invalid line in " + mappingIniFileName + ": " + line);
            }
            keyToConfigGroup.put(split[0], split[1]);
        }

        return keyToConfigGroup;
    }
}
