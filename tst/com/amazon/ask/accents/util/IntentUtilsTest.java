package com.amazon.ask.accents.util;

import java.io.FileReader;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import com.amazon.ask.model.Slot;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

public final class IntentUtilsTest
{

    private final ObjectMapper objectMapper = ObjectMapperFactory.getInstance();

    /**
     * Test that the slot Id can be obtained in the happy case.
     */
    @Test
    public void testGetSlotId()
    {
        // Arrange
        Slot slot = buildSlot("/testdata/slots/ValidSlot.json");

        // Act
        String slotId = IntentUtils.getSlotId(slot);

        // Assert
        assertEquals("en-IN", slotId);
    }

    /**
     * Test that an exception is raised if there are no resolutions in the slot.
     */
    @Test(expected = IllegalStateException.class)
    public void testGetSlotId_MissigResolutions()
    {
        // Arrange
        Slot slot = buildSlot("/testdata/slots/SlotWithoutResolutions.json");

        // Act && Assert
        IntentUtils.getSlotId(slot);
    }

    /**
     * Test that an exception is raised if there are no resolutionsPerAuthority in
     * the slot.
     */
    @Test(expected = IllegalStateException.class)
    public void testGetSlotId_MissigResolutionsPerAuthority()
    {
        // Arrange
        Slot slot = buildSlot("/testdata/slots/SlotWithoutResolutionsPerAuthority.json");

        // Act && Assert
        IntentUtils.getSlotId(slot);
    }

    /**
     * Test that an exception is raised if there resolutionsPerAuthority is empty in
     * the slot.
     */
    @Test(expected = IllegalStateException.class)
    public void testGetSlotId_EmptyResolutionsPerAuthority()
    {
        // Arrange
        Slot slot = buildSlot("/testdata/slots/SlotWithEmptyResolutionsPerAuthority.json");

        // Act && Assert
        IntentUtils.getSlotId(slot);
    }

    /**
     * Test that an exception is raised if there are no
     * resolutionsPerAuthorityValues in the slot.
     */
    @Test(expected = IllegalStateException.class)
    public void testGetSlotId_MissigResolutionsPerAuthority_Values()
    {
        // Arrange
        Slot slot = buildSlot("/testdata/slots/SlotWithoutResolutionsPerAuthorityValues.json");

        // Act && Assert
        IntentUtils.getSlotId(slot);
    }

    /**
     * Test that an exception is raised if there resolutionsPerAuthorityValues is
     * empty in the slot.
     */
    @Test(expected = IllegalStateException.class)
    public void testGetSlotId_EmptyResolutionsPerAuthority_Values()
    {
        // Arrange
        Slot slot = buildSlot("/testdata/slots/SlotWithEmptyResolutionsPerAuthorityValues.json");

        // Act && Assert
        IntentUtils.getSlotId(slot);
    }

    /**
     * Test that an exception is raised if there are no
     * resolutionsPerAuthorityValuesValue in the slot.
     */
    @Test(expected = IllegalStateException.class)
    public void testGetSlotId_MissigResolutionsPerAuthority_Values_Value()
    {
        // Arrange
        Slot slot = buildSlot("/testdata/slots/SlotWithoutResolutionsPerAuthorityValuesValue.json");

        // Act && Assert
        IntentUtils.getSlotId(slot);
    }

    /**
     * Test that an exception is raised if there are no
     * resolutionsPerAuthorityValuesValueId in the slot.
     */
    @Test(expected = IllegalStateException.class)
    public void testGetSlotId_MissigResolutionsPerAuthority_Values_Value_Id()
    {
        // Arrange
        Slot slot = buildSlot("/testdata/slots/SlotWithoutResolutionsPerAuthorityValuesValueId.json");

        // Act && Assert
        IntentUtils.getSlotId(slot);
    }

    private Slot buildSlot(String slotResourcePath)
    {
        URL url = getClass().getResource(slotResourcePath);

        Slot slot;
        try
        {
            slot = objectMapper.readValue(new FileReader(url.getPath()), Slot.class);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Failed loading the slots from test data files. This is a fatal error.", e);
        }

        return slot;
    }

}