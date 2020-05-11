package com.amazon.ask.accents.skillmetadata;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ APLDatasourceKeys.CURRENT_ACCENT, APLDatasourceKeys.HINT })
public class Properties {
    @JsonProperty(APLDatasourceKeys.CURRENT_ACCENT)
    public void setCurrentAccent(String currentAccent) {
        this.currentAccent = currentAccent;
    }

    @JsonProperty(APLDatasourceKeys.CURRENT_ACCENT)
    public String getCurrentAccent() {
        return this.currentAccent;
    }

    @JsonProperty(APLDatasourceKeys.HINT)
    public void setHint(String hint) {
        this.hint = hint;
    }

    @JsonProperty(APLDatasourceKeys.HINT)
    public String getHint() {
        return this.hint;
    }

    private String currentAccent;
    private String hint;
}