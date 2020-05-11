package com.amazon.ask.accents.voices;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "locale", "name", "url", "Female", "Male" })
public class Voice {
    @JsonProperty("locale")
    public void setLocale(String locale) {
        this.locale = locale;
    }

    @JsonProperty("locale")
    public String getLocale() {
        return this.locale;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("name")
    public String getName() {
        return this.name;
    }

    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }

    @JsonProperty("url")
    public String getUrl() {
        return this.url;
    }

    @JsonProperty("female")
    public void setFemale(List<String> female) {
        this.female = female;
    }

    @JsonProperty("female")
    public List<String> getFemale() {
        return this.female;
    }

    @JsonProperty("male")
    public void setMale(List<String> male) {
        this.male = male;
    }

    @JsonProperty("male")
    public List<String> getMale() {
        return this.male;
    }

    @JsonProperty("locale")
    private String locale;

    @JsonProperty("name")
    private String name;

    @JsonProperty("url")
    private String url;

    @JsonProperty("female")
    private List<String> female;

    @JsonProperty("male")
    private List<String> male;
}
