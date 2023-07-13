
package com.koodipalvelu.airiot.fi.setupcompare.model.diff;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "iniFile",
    "name",
    "diffs"
})
@Generated("jsonschema2pojo")
public class Setup {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("iniFile")
    private String iniFile;
    @JsonProperty("name")
    private String name;
    @JsonProperty("diffs")
    private List<Diff> diffs;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("iniFile")
    public String getIniFile() {
        return iniFile;
    }

    @JsonProperty("iniFile")
    public void setIniFile(String iniFile) {
        this.iniFile = iniFile;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("diffs")
    public List<Diff> getDiffs() {
        return diffs;
    }

    @JsonProperty("diffs")
    public void setDiffs(List<Diff> diffs) {
        this.diffs = diffs;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
