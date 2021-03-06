package com.amazon.ask.accents.util;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.FileReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Request;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.Slot;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

public final class IntentUtilsTest {
    /**
     * Test that the slots can be obtained in the happy case.
     */
    @Test
    public void testGetSlots() {
        // Arrange
        Map<String, Slot> slots = new HashMap<>();
        slots.put("Slot1", mock(Slot.class));
        slots.put("Slot2", mock(Slot.class));
        final HandlerInput handlerInput = buildHandlerInput(slots);

        // Act
        Map<String, Slot> actualSlots = unitUnderTest.getSlots(handlerInput);

        // Assert
        assertEquals(slots, actualSlots);
    }

    /**
     * Test that the slots can be obtained when there are no slots. We should
     * basically return an empty map of slots.
     */
    @Test
    public void testGetSlots_SlotsEmpty() {
        // Arrange
        final HandlerInput handlerInput = buildHandlerInput(new HashMap<>());

        // Act
        Map<String, Slot> actualSlots = unitUnderTest.getSlots(handlerInput);

        // Assert
        assertEquals(0, actualSlots.size());
    }

    /**
     * Test that the slots can be obtained when the slots are missing in the handler
     * input. We should basically return an empty map of slots.
     */
    @Test
    public void testGetSlots_SlotsMissing() {
        // Arrange
        final HandlerInput handlerInput = buildHandlerInput(null);

        // Act
        Map<String, Slot> actualSlots = unitUnderTest.getSlots(handlerInput);

        // Assert
        assertEquals(0, actualSlots.size());
    }

    /**
     * Test that an exception is raised if the input is null.
     */
    @Test(expected = NullPointerException.class)
    public void testGetSlots_NullInput() {
        // Act && Assert
        unitUnderTest.getSlots(null);
    }

    /**
     * Test that an exception is raised if the Request is not of the type
     * IntentRequest
     */
    @Test(expected = IllegalStateException.class)
    public void testGetSlots_RequestIncorrectType() {
        // Arrange
        RequestEnvelope requestEnvelope = mock(RequestEnvelope.class);

        Request request = new Request() {
        };
        when(requestEnvelope.getRequest()).thenReturn(request);

        // Act && Assert
        unitUnderTest.getSlots(HandlerInput.builder().withRequestEnvelope(requestEnvelope).build());
    }

    /**
     * Test that an exception is raised if the Intent is missing in the handler
     * input
     */
    @Test(expected = IllegalStateException.class)
    public void testGetSlots_MissingIntent() {
        // Arrange
        RequestEnvelope requestEnvelope = mock(RequestEnvelope.class);

        IntentRequest intentRequest = mock(IntentRequest.class);
        when(requestEnvelope.getRequest()).thenReturn(intentRequest);

        Intent intent = null;
        when(intentRequest.getIntent()).thenReturn(intent);

        // Act && Assert
        unitUnderTest.getSlots(HandlerInput.builder().withRequestEnvelope(requestEnvelope).build());
    }

    /**
     * Test that the slot Id can be obtained in the happy case.
     */
    @Test
    public void testGetSlotId() {
        // Arrange
        Slot slot = buildSlot("/testdata/slots/ValidSlot.json");

        // Act
        String slotId = unitUnderTest.getSlotId(slot);

        // Assert
        assertEquals("en-IN", slotId);
    }

    /**
     * Test that an exception is raised if the input is null.
     */
    @Test(expected = NullPointerException.class)
    public void testGetSlotId_NullInput() {
        // Act && Assert
        unitUnderTest.getSlotId(null);
    }

    /**
     * Test that null is returned if there are no resolutions in the slot.
     */
    @Test
    public void testGetSlotId_MissigResolutions() {
        // Arrange
        Slot slot = buildSlot("/testdata/slots/SlotWithoutResolutions.json");

        // Act
        String slotId = unitUnderTest.getSlotId(slot);

        // Assert
        assertNull("slotId should be null.", slotId);
    }

    /**
     * Test that null is returned if there are no resolutionsPerAuthority in the
     * slot.
     */
    @Test
    public void testGetSlotId_MissigResolutionsPerAuthority() {
        // Arrange
        Slot slot = buildSlot("/testdata/slots/SlotWithoutResolutionsPerAuthority.json");

        // Act
        String slotId = unitUnderTest.getSlotId(slot);

        // Assert
        assertNull("slotId should be null.", slotId);
    }

    /**
     * Test that null is returned if the resolutionsPerAuthority is empty in the
     * slot.
     */
    @Test
    public void testGetSlotId_EmptyResolutionsPerAuthority() {
        // Arrange
        Slot slot = buildSlot("/testdata/slots/SlotWithEmptyResolutionsPerAuthority.json");

        // Act
        String slotId = unitUnderTest.getSlotId(slot);

        // Assert
        assertNull("slotId should be null.", slotId);
    }

    /**
     * Test that null is returned if there are no resolutionsPerAuthorityValues in
     * the slot.
     */
    @Test
    public void testGetSlotId_MissigResolutionsPerAuthority_Values() {
        // Arrange
        Slot slot = buildSlot("/testdata/slots/SlotWithoutResolutionsPerAuthorityValues.json");

        // Act
        String slotId = unitUnderTest.getSlotId(slot);

        // Assert
        assertNull("slotId should be null.", slotId);
    }

    /**
     * Test that null is retutned if there resolutionsPerAuthorityValues is empty in
     * the slot.
     */
    @Test
    public void testGetSlotId_EmptyResolutionsPerAuthority_Values() {
        // Arrange
        Slot slot = buildSlot("/testdata/slots/SlotWithEmptyResolutionsPerAuthorityValues.json");

        // Act
        String slotId = unitUnderTest.getSlotId(slot);

        // Assert
        assertNull("slotId should be null.", slotId);
    }

    /**
     * Test that null is returned if there are no resolutionsPerAuthorityValuesValue
     * in the slot.
     */
    @Test
    public void testGetSlotId_MissigResolutionsPerAuthority_Values_Value() {
        // Arrange
        Slot slot = buildSlot("/testdata/slots/SlotWithoutResolutionsPerAuthorityValuesValue.json");

        // Act
        String slotId = unitUnderTest.getSlotId(slot);

        // Assert
        assertNull("slotId should be null.", slotId);
    }

    /**
     * Test that null is returned if there are no
     * resolutionsPerAuthorityValuesValueId in the slot.
     */
    @Test
    public void testGetSlotId_MissigResolutionsPerAuthority_Values_Value_Id() {
        // Arrange
        Slot slot = buildSlot("/testdata/slots/SlotWithoutResolutionsPerAuthorityValuesValueId.json");

        // Act
        String slotId = unitUnderTest.getSlotId(slot);

        // Assert
        assertNull("slotId should be null.", slotId);
    }

    /**
     * Test that the slot raw value can be obtained in the happy case.
     */
    @Test
    public void testGetRawSlotValue() {
        // Arrange
        Slot slot = buildSlot("/testdata/slots/ValidSlot.json");

        // Act
        String slotValue = unitUnderTest.getRawSlotValue(slot);

        // Assert
        assertEquals("Indian", slotValue);
    }

    /**
     * Test that the slot raw value can be obtained even when it is null.
     */
    @Test
    public void testGetRawSlotValue_SlotValueIsNull() {
        // Arrange
        Slot slot = buildSlot("/testdata/slots/SlotWithoutValue.json");

        // Act
        String slotValue = unitUnderTest.getRawSlotValue(slot);

        // Assert
        assertNull(slotValue);
    }

    /**
     * Test that an exception is raised if the input is null.
     */
    @Test(expected = NullPointerException.class)
    public void testGetRawSlotValue_NullInput() {
        // Act && Assert
        unitUnderTest.getRawSlotValue(null);
    }

    /**
     * Test that the slot name can be obtained in the happy case.
     */
    @Test
    public void testGetSlotName() {
        // Arrange
        Slot slot = buildSlot("/testdata/slots/ValidSlot.json");

        // Act
        String slotName = unitUnderTest.getSlotName(slot);

        // Assert
        assertEquals("Indian", slotName);
    }

    /**
     * Test that an exception is raised if the input is null.
     */
    @Test(expected = NullPointerException.class)
    public void testGetSlotName_NullInput() {
        // Act && Assert
        unitUnderTest.getSlotName(null);
    }

    /**
     * Test that null is returned if there are no resolutions in the slot.
     */
    @Test
    public void testGetSlotName_MissigResolutions() {
        // Arrange
        Slot slot = buildSlot("/testdata/slots/SlotWithoutResolutions.json");

        // Act
        String slotName = unitUnderTest.getSlotName(slot);

        // Assert
        assertNull("slotName should be null.", slotName);
    }

    /**
     * Test that null is returned if there are no resolutionsPerAuthority in the
     * slot.
     */
    @Test
    public void testGetSlotName_MissigResolutionsPerAuthority() {
        // Arrange
        Slot slot = buildSlot("/testdata/slots/SlotWithoutResolutionsPerAuthority.json");

        // Act
        String slotName = unitUnderTest.getSlotName(slot);

        // Assert
        assertNull("slotName should be null.", slotName);
    }

    /**
     * Test that null is returned if the resolutionsPerAuthority is empty in the
     * slot.
     */
    @Test
    public void testGetSlotName_EmptyResolutionsPerAuthority() {
        // Arrange
        Slot slot = buildSlot("/testdata/slots/SlotWithEmptyResolutionsPerAuthority.json");

        // Act
        String slotName = unitUnderTest.getSlotName(slot);

        // Assert
        assertNull("slotName should be null.", slotName);
    }

    /**
     * Test that null is returned if there are no resolutionsPerAuthorityValues in
     * the slot.
     */
    @Test
    public void testGetSlotName_MissigResolutionsPerAuthority_Values() {
        // Arrange
        Slot slot = buildSlot("/testdata/slots/SlotWithoutResolutionsPerAuthorityValues.json");

        // Act
        String slotName = unitUnderTest.getSlotName(slot);

        // Assert
        assertNull("slotName should be null.", slotName);
    }

    /**
     * Test that null is retutned if there resolutionsPerAuthorityValues is empty in
     * the slot.
     */
    @Test
    public void testGetSlotName_EmptyResolutionsPerAuthority_Values() {
        // Arrange
        Slot slot = buildSlot("/testdata/slots/SlotWithEmptyResolutionsPerAuthorityValues.json");

        // Act
        String slotName = unitUnderTest.getSlotName(slot);

        // Assert
        assertNull("slotName should be null.", slotName);
    }

    /**
     * Test that null is returned if there are no resolutionsPerAuthorityValuesValue
     * in the slot.
     */
    @Test
    public void testGetSlotName_MissigResolutionsPerAuthority_Values_Value() {
        // Arrange
        Slot slot = buildSlot("/testdata/slots/SlotWithoutResolutionsPerAuthorityValuesValue.json");

        // Act
        String slotName = unitUnderTest.getSlotName(slot);

        // Assert
        assertNull("slotName should be null.", slotName);
    }

    /**
     * Test that null is returned if there are no
     * resolutionsPerAuthorityValuesValueName in the slot.
     */
    @Test
    public void testGetSlotName_MissigResolutionsPerAuthority_Values_Value_Name() {
        // Arrange
        Slot slot = buildSlot("/testdata/slots/SlotWithoutResolutionsPerAuthorityValuesValueName.json");

        // Act
        String slotName = unitUnderTest.getSlotName(slot);

        // Assert
        assertNull("slotName should be null.", slotName);
    }

    private Slot buildSlot(String slotResourcePath) {
        URL url = getClass().getResource(slotResourcePath);

        Slot slot;
        try {
            slot = objectMapper.readValue(new FileReader(url.getPath()), Slot.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed loading the slots from test data files. This is a fatal error.", e);
        }

        return slot;
    }

    private HandlerInput buildHandlerInput(Map<String, Slot> slots) {
        RequestEnvelope requestEnvelope = mock(RequestEnvelope.class);

        IntentRequest intentRequest = mock(IntentRequest.class);
        when(requestEnvelope.getRequest()).thenReturn(intentRequest);

        Intent intent = mock(Intent.class);
        when(intentRequest.getIntent()).thenReturn(intent);

        when(intent.getSlots()).thenReturn(slots == null ? null : new HashMap<>(slots));

        return HandlerInput.builder().withRequestEnvelope(requestEnvelope).build();
    }

    private static final IntentUtils unitUnderTest = IntentUtils.getInstance();
    private final ObjectMapper objectMapper = ObjectMapperFactory.getInstance();
}