package com.amazon.ask.accents.util;

import static org.junit.Assert.assertEquals;

import java.io.FileReader;
import java.lang.reflect.Type;
import java.net.URL;

import com.amazon.ask.model.Slot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import org.junit.Test;

public final class IntentUtilsTest {

    /**
     * Test that the slot Id can be obtained in the happy case.
     */
    @Test
    public void testGetSlotId() {
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
    public void testGetSlotId_MissigResolutions() {
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
    public void testGetSlotId_MissigResolutionsPerAuthority() {
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
    public void testGetSlotId_EmptyResolutionsPerAuthority() {
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
    public void testGetSlotId_MissigResolutionsPerAuthority_Values() {
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
    public void testGetSlotId_EmptyResolutionsPerAuthority_Values() {
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
    public void testGetSlotId_MissigResolutionsPerAuthority_Values_Value() {
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
    public void testGetSlotId_MissigResolutionsPerAuthority_Values_Value_Id() {
        // Arrange
        Slot slot = buildSlot("/testdata/slots/SlotWithoutResolutionsPerAuthorityValuesValueId.json");

        // Act && Assert
        IntentUtils.getSlotId(slot);
    }

    private Slot buildSlot(String slotResourcePath) {
        URL url = getClass().getResource(slotResourcePath);

        Type slotType = new TypeToken<Slot>() {
        }.getType();
        Slot slot;
        try {
            JsonReader reader = new JsonReader(new FileReader(url.getPath()));
            slot = new Gson().fromJson(reader, slotType);
        } catch (Exception e) {
            throw new RuntimeException("Failed loading the mapping of languages to voices. This is a fatal error.", e);
        }

        return slot;
    }

}