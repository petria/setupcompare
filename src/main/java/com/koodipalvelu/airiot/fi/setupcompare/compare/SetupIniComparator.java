package com.koodipalvelu.airiot.fi.setupcompare.compare;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class SetupIniComparator {

    private final Map<String, String> configKeyMapping;

    public SetupIniComparator(Map<String, String> configKeyMapping ) {
        this.configKeyMapping = configKeyMapping;
    }

    public void compare(Map<String, String> base, Map<String, String> other) {
        for (String key : configKeyMapping.keySet()) {
            String valueBase = base.get(key);
            String valueOther = other.get(key);

            if (valueBase != null && valueOther != null) {
                if (valueBase.equals(valueOther)) {
                    // same
                } else {
                    log.debug("{} :: {} != {}", key, valueBase, valueOther);
                }
            }

        }
    }


}
