package com.amazon.ask.accents.skillmetadata;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ APLDatasourceKeys.INPUT_PATH, APLDatasourceKeys.TRANSFORMER })
public class Transformers {
    @JsonProperty(APLDatasourceKeys.INPUT_PATH)
    public void setInputPath(String inputPath) {
        this.inputPath = inputPath;
    }

    @JsonProperty(APLDatasourceKeys.INPUT_PATH)
    public String getInputPath() {
        return this.inputPath;
    }

    @JsonProperty(APLDatasourceKeys.TRANSFORMER)
    public void setTransformer(String transformer) {
        this.transformer = transformer;
    }

    @JsonProperty(APLDatasourceKeys.TRANSFORMER)
    public String getTransformer() {
        return this.transformer;
    }

    private String inputPath;
    private String transformer;

    public static final String HINT_TRANSFORMER = "textToHint";
}