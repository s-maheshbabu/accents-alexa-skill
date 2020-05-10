package com.amazon.ask.accents.apl;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import com.amazon.ask.accents.util.ObjectMapperFactory;
import com.amazon.ask.model.interfaces.alexa.presentation.apl.RenderDocumentDirective;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DocumentRenderer {
    private DocumentRenderer() {
    }

    public RenderDocumentDirective buildDirective(final String language) {
        if (document == null || visualMetadataDataSource == null) {
            try {
                document = objectMapper.readValue(new FileReader(getClass().getResource(DOCUMENT_PATH).getPath()),
                        HashMap.class);
            } catch (final Exception e) {
                logger.error("Failed loading APL document. GUI would be broken.", e);
                return null;
            }

            try {
                final Map<String, Object> supportedVoicesDataSource = objectMapper.readValue(
                        new FileReader(getClass().getResource(SUPPORTED_VOICES_DATASOURCES_PATH).getPath()),
                        HashMap.class);

                final JsonNode node = objectMapper
                        .readTree(new FileReader(getClass().getResource(VISUAL_METADATA_DATASOURCES_PATH).getPath()));
                setCurrentAccent(node, language);
                visualMetadataDataSource = objectMapper.treeToValue(node, HashMap.class);

                visualMetadataDataSource.putAll(supportedVoicesDataSource);
            } catch (final Exception e) {
                logger.error("Failed loading APL datasources. GUI would be broken.", e);
                return null;
            }
        }
        return RenderDocumentDirective.builder().withToken(APL_TOKEN).withDocument(document)
                .withDatasources(visualMetadataDataSource).build();
    }

    private void setCurrentAccent(final JsonNode node, final String language) {
        ((ObjectNode) node.at(PATH)).put(CURRENT_ACCENT_KEY,
                StringUtils.isEmpty(language) ? I_SPOKE_LIKE : I_SPOKE_LIKE + language);
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
        document = visualMetadataDataSource = null;
    }

    private static final String PATH = "/skillMetadata/properties";
    private static final String APL_TOKEN = "token";
    private static final String I_SPOKE_LIKE = "I spoke like ... ";
    private static final String CURRENT_ACCENT_KEY = "currentAccent";

    private static DocumentRenderer instance;
    private static Map<String, Object> document = null;
    private static Map<String, Object> visualMetadataDataSource = null;
    private String DOCUMENT_PATH = "/resources/apl/document.json";
    private String VISUAL_METADATA_DATASOURCES_PATH = "/resources/apl/skill-metadata-datasource.json";
    private String SUPPORTED_VOICES_DATASOURCES_PATH = "/resources/data/supported_voices.json";
    private ObjectMapper objectMapper = ObjectMapperFactory.getInstance();

    private static final Logger logger = LogManager.getLogger(DocumentRenderer.class);
}
