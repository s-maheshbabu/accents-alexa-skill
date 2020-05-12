package com.amazon.ask.accents.apl;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.amazon.ask.accents.skillmetadata.Properties;
import com.amazon.ask.accents.skillmetadata.VisualSkillMetadata;
import com.amazon.ask.accents.util.ObjectMapperFactory;
import com.amazon.ask.accents.voices.Voice;
import com.amazon.ask.accents.voices.VoicesRepo;
import com.amazon.ask.model.interfaces.alexa.presentation.apl.RenderDocumentDirective;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DocumentRenderer {
    private DocumentRenderer() {
    }

    public RenderDocumentDirective buildDirective(final String locale) {
        Validate.isTrue(StringUtils.isNotBlank(locale), "Locale is a required parameter.", locale);

        if (document == null) {
            try {
                document = objectMapper.readValue(new FileReader(getClass().getResource(DOCUMENT_PATH).getPath()),
                        HashMap.class);
            } catch (final Exception e) {
                logger.error("Failed loading APL document. GUI would be broken.", e);
                return null;
            }
        }

        if (visualSkillMetadata == null) {
            try {
                visualSkillMetadata = objectMapper.readValue(
                        new FileReader(getClass().getResource(VISUAL_METADATA_DATASOURCES_PATH).getPath()),
                        VisualSkillMetadata.class);
            } catch (final Exception e) {
                logger.error("Failed loading supported voices data source. GUI would be broken.", e);
                return null;
            }
        }
        setCurrentAccent(locale);

        if (supportedVoicesDataSource == null) {
            try {
                supportedVoicesDataSource = objectMapper.readValue(
                        new FileReader(getClass().getResource(SUPPORTED_VOICES_DATASOURCES_PATH).getPath()),
                        HashMap.class);
            } catch (final Exception e) {
                logger.error("Failed loading supported voices data source. GUI would be broken.", e);
                return null;
            }
        }

        try {
            visualMetadataDataSource = objectMapper.readValue(objectMapper.writeValueAsString(visualSkillMetadata),
                    HashMap.class);
        } catch (final Exception e) {
            logger.error("Failed loading the dynamic values into the data source. GUI would be broken.", e);
            return null;
        }
        visualMetadataDataSource.putAll(supportedVoicesDataSource);

        return RenderDocumentDirective.builder().withToken(APL_TOKEN).withDocument(document)
                .withDatasources(visualMetadataDataSource).build();
    }

    private void setCurrentAccent(final String locale) {
        Map<String, Voice> supportedVoices = voicesRepo.getSupportedVoices();
        Voice voice = supportedVoices.get(locale);

        Properties properties = visualSkillMetadata.getProperties();
        properties.setCurrentAccent(I_SPOKE_LIKE + voice.getName());
        properties.setCurrentAccentUrl(voice.getUrl());

        List<Voice> values = new ArrayList<Voice>(supportedVoices.values());
        Voice randomVoice = values.get(new Random().nextInt(values.size()));
        properties.setHint(HINT_BASE + randomVoice.getName());
    }

    public static DocumentRenderer getInstance() {
        if (instance == null)
            instance = new DocumentRenderer();
        return instance;
    }

    /**
     * Visible for testing purposes only.
     */
    protected void reset() {
        document = supportedVoicesDataSource = visualMetadataDataSource = null;
    }

    private static final VoicesRepo voicesRepo = VoicesRepo.getInstance();

    private static final String APL_TOKEN = "token";
    private static final String I_SPOKE_LIKE = "Here is my ";
    private static final String HINT_BASE = "Talk like ";

    private static DocumentRenderer instance;
    private static VisualSkillMetadata visualSkillMetadata = null;
    private static Map<String, Object> document = null;
    private static Map<String, Object> visualMetadataDataSource = null;
    private static Map<String, Object> supportedVoicesDataSource = null;

    private String DOCUMENT_PATH = "/resources/apl/document.json";
    private String VISUAL_METADATA_DATASOURCES_PATH = "/resources/apl/skill-metadata-datasource.json";
    private String SUPPORTED_VOICES_DATASOURCES_PATH = "/resources/data/supported_voices.json";

    private ObjectMapper objectMapper = ObjectMapperFactory.getInstance();

    private static final Logger logger = LogManager.getLogger(DocumentRenderer.class);
}
