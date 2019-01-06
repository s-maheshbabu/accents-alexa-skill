package com.amazon.ask.accents.apl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.Reader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import com.amazon.ask.model.interfaces.alexa.presentation.apl.RenderDocumentDirective;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.BeforeClass;
import org.junit.Test;

public class DocumentRendererTest
{
    /*
    * Test that buildDirective returns the correct directive.
    */
    @Test
    public void testBuildDirective_HappyCase() throws Exception
    {
        // Arrange

        // Act
        RenderDocumentDirective directive = unitUnderTest.buildDirective();

        // Assert
        assertNotNull(directive);
        assertEquals("token", directive.getToken());
    }

    /*
    * Test that we swallow the error if we are unable to read the document.
    */
    @Test
    public void testBuildDirective_ErrorReadingDocument() throws Exception
    {
        // Arrange
        ObjectMapper originalMapper = (ObjectMapper) FieldUtils.readDeclaredField(unitUnderTest, "objectMapper", true);

        ObjectMapper mapper = mock(ObjectMapper.class);
        when(mapper.readValue(any(Reader.class), any(Class.class))).thenThrow(new RuntimeException());
        FieldUtils.writeField(unitUnderTest, "objectMapper", mapper, true);

        // Act
        RenderDocumentDirective directive = unitUnderTest.buildDirective();

        // Assert
        assertNull(directive);

        // Cleanup
        FieldUtils.writeDeclaredField(unitUnderTest, "objectMapper", originalMapper, true);
    }

    @BeforeClass
    public static void setup() throws Exception
    {
        FieldUtils.writeField(unitUnderTest, "DOCUMENT_PATH", TEST_DOCUMENT_PATH, true);
        FieldUtils.writeField(unitUnderTest, "DATASOURCES_PATH", TEST_DATASOURCES_PATH, true);
    }

    private static final DocumentRenderer unitUnderTest = DocumentRenderer.getInstance();

    private static String TEST_DOCUMENT_PATH = "/testdata/apl/document.json";
    private static String TEST_DATASOURCES_PATH = "/testdata/apl/datasources.json";
}