package com.amazon.ask.accents.skillmetadata;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonTypeName(APLDatasourceKeys.VISUAL_SKILL_METADATA)
@JsonTypeInfo(include = As.WRAPPER_OBJECT, use = Id.NAME)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ APLDatasourceKeys.BACKGROUND_IMAGE_URL, APLDatasourceKeys.PROPERTIES,
        APLDatasourceKeys.TRANSFORMERS, APLDatasourceKeys.TITLE, APLDatasourceKeys.LOGO_URL })
public class VisualSkillMetadata {
    @JsonProperty(APLDatasourceKeys.BACKGROUND_IMAGE_URL)
    public void setBackgroundImageUrl(String backgroundImageUrl) {
        this.backgroundImageUrl = backgroundImageUrl;
    }

    @JsonProperty(APLDatasourceKeys.BACKGROUND_IMAGE_URL)
    public String getBackgroundImageUrl() {
        return this.backgroundImageUrl;
    }

    @JsonProperty(APLDatasourceKeys.PROPERTIES)
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    @JsonProperty(APLDatasourceKeys.PROPERTIES)
    public Properties getProperties() {
        return this.properties;
    }

    @JsonProperty(APLDatasourceKeys.TRANSFORMERS)
    public void setTransformers(List<Transformers> transformers) {
        this.transformers = transformers;
    }

    @JsonProperty(APLDatasourceKeys.TRANSFORMERS)
    public List<Transformers> getTransformers() {
        return this.transformers;
    }

    @JsonProperty(APLDatasourceKeys.TITLE)
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty(APLDatasourceKeys.TITLE)
    public String getTitle() {
        return this.title;
    }

    @JsonProperty(APLDatasourceKeys.LOGO_URL)
    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    @JsonProperty(APLDatasourceKeys.LOGO_URL)
    public String getLogoUrl() {
        return this.logoUrl;
    }

    private String backgroundImageUrl;
    private Properties properties;
    private List<Transformers> transformers;
    private String title;
    private String logoUrl;
}