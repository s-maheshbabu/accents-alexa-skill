package com.amazon.ask.accents.voices;

import com.amazon.ask.accents.skillmetadata.APLDatasourceKeys;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonTypeName(APLDatasourceKeys.SUPPORTED_VOICES)
@JsonTypeInfo(include = As.WRAPPER_OBJECT, use = Id.NAME)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SupportedVoices {
    @JsonProperty("voices")
    public void setVoices(List<Voice> voices) {
        this.voices = voices;
    }

    @JsonProperty("voices")
    public List<Voice> getVoices() {
        return this.voices;
    }

    private List<Voice> voices;
}