package com.amazon.ask.accents.apl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.Reader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import com.amazon.ask.accents.skillmetadata.APLDatasourceKeys;
import com.amazon.ask.accents.util.ObjectMapperFactory;
import com.amazon.ask.model.interfaces.alexa.presentation.apl.RenderDocumentDirective;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Before;
import org.junit.Test;

public class DocumentRendererTest {
    /*
     * Test that buildDirective returns the correct directive.
     */
    @Test
    public void testBuildDirective_HappyCase() throws Exception {
        // Arrange
        String languageName = "languageName";

        // Act
        RenderDocumentDirective directive = unitUnderTest.buildDirective(languageName);

        // Assert
        assertNotNull(directive);
        assertEquals("token", directive.getToken());

        JsonNode dataSourceNode = objectMapper.convertValue(directive.getDatasources(), JsonNode.class);
        assertFalse(dataSourceNode.at("/" + APLDatasourceKeys.VISUAL_SKILL_METADATA).isMissingNode());
        assertEquals(I_SPOKE_LIKE + languageName, dataSourceNode.at(CURRENT_ACCENT_PATH).asText());

        assertFalse(dataSourceNode.at("/supportedVoices").isMissingNode());
    }

    /*
     * Test that buildDirective throws when no language is passed.
     */
    @Test
    public void testBuildDirective_NoLanguage() throws Exception {
        // Arrange
        String[] languageNameValues = { "", null };

        for (String languageName : languageNameValues) {
            // Act
            try {
                unitUnderTest.buildDirective(languageName);
                fail("Expected an IllegalArgumentException to be thrown");
            } catch (IllegalArgumentException e) {
            }
        }
    }

    /*
     * Test that we swallow the error if we are unable to read the document.
     */
    @Test
    public void testBuildDirective_ErrorReadingDocument() throws Exception {
        // Arrange
        ObjectMapper originalMapper = (ObjectMapper) FieldUtils.readDeclaredField(unitUnderTest, "objectMapper", true);

        ObjectMapper mapper = mock(ObjectMapper.class);
        when(mapper.readValue(any(Reader.class), any(Class.class))).thenThrow(new RuntimeException());
        FieldUtils.writeField(unitUnderTest, "objectMapper", mapper, true);

        // Act
        RenderDocumentDirective directive = unitUnderTest.buildDirective("anyLanguage");

        // Assert
        assertNull(directive);

        // Cleanup
        FieldUtils.writeDeclaredField(unitUnderTest, "objectMapper", originalMapper, true);
    }

    @Before
    public void initialize() {
        unitUnderTest.reset();
    }

    private static final String I_SPOKE_LIKE = "Here is my ";
    private static final String CURRENT_ACCENT_PATH = "/" + APLDatasourceKeys.VISUAL_SKILL_METADATA + "/"
            + APLDatasourceKeys.PROPERTIES + "/" + APLDatasourceKeys.CURRENT_ACCENT;

    private static final DocumentRenderer unitUnderTest = DocumentRenderer.getInstance();

    private ObjectMapper objectMapper = ObjectMapperFactory.getInstance();
}