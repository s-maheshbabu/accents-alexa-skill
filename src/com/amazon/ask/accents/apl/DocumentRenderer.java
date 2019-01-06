package com.amazon.ask.accents.apl;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import com.amazon.ask.accents.util.ObjectMapperFactory;
import com.amazon.ask.model.interfaces.alexa.presentation.apl.RenderDocumentDirective;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DocumentRenderer
{
    private DocumentRenderer()
    {
    }

    public RenderDocumentDirective buildDirective()
    {
        if (document == null || dataSources == null)
        {
            try
            {
                document = objectMapper.readValue(new FileReader(getClass().getResource(DOCUMENT_PATH).getPath()), HashMap.class);
                dataSources = objectMapper.readValue(new FileReader(getClass().getResource(DATASOURCES_PATH).getPath()), HashMap.class);
            }
            catch (Exception e)
            {
                logger.error("Failed loading APL document and datasources. GUI would be broken.", e);
                return null;
            }
        }
        return RenderDocumentDirective.builder().withToken(APL_TOKEN).withDocument(document).withDatasources(dataSources).build();
    }

    public static DocumentRenderer getInstance()
    {
        if (instance == null)
            instance = new DocumentRenderer();
        return instance;
    }

    private static final String APL_TOKEN = "token";
    private static final Logger logger = LogManager.getLogger(DocumentRenderer.class);
    private static DocumentRenderer instance;
    private static Map<String, Object> document = null;
    private static Map<String, Object> dataSources = null;
    private String DOCUMENT_PATH = "/resources/apl/document.json";
    private String DATASOURCES_PATH = "/resources/apl/datasources.json";
    private ObjectMapper objectMapper = ObjectMapperFactory.getInstance();
}
