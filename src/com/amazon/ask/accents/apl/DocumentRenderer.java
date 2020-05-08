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
        if (document == null || dataSources == null) {
            try {
                document = objectMapper.readValue(new FileReader(getClass().getResource(DOCUMENT_PATH).getPath()),
                        HashMap.class);

                final JsonNode node = objectMapper
                        .readTree(new FileReader(getClass().getResource(DATASOURCES_PATH).getPath()));
                setAccentSpoken(node, language);

                dataSources = objectMapper.treeToValue(node, HashMap.class);
            } catch (final Exception e) {
                logger.error("Failed loading APL document and datasources. GUI would be broken.", e);
                return null;
            }
        }
        return RenderDocumentDirective.builder().withToken(APL_TOKEN).withDocument(document)
                .withDatasources(dataSources).build();
    }

    private void setAccentSpoken(final JsonNode node, final String language) {
        ((ObjectNode) node.at(PATH)).put(ACCENT_SPOKEN_KEY,
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
        document = dataSources = null;
    }

    private static final String PATH = "/skillMetadata/properties";
    private static final String APL_TOKEN = "token";
    private static final String I_SPOKE_LIKE = "I spoke like ... ";
    private static final String ACCENT_SPOKEN_KEY = "accentSpoken";

    private static final Logger logger = LogManager.getLogger(DocumentRenderer.class);
    private static DocumentRenderer instance;
    private static Map<String, Object> document = null;
    private static Map<String, Object> dataSources = null;
    private final String DOCUMENT_PATH = "/resources/apl/document.json";
    private final String DATASOURCES_PATH = "/resources/apl/datasources.json";
    private final ObjectMapper objectMapper = ObjectMapperFactory.getInstance();
}
