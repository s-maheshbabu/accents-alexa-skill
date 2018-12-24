package com.amazon.ask.accents.handlers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.amazon.ask.accents.model.Intents;
import com.amazon.ask.accents.model.Slots;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Request;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.amazon.ask.model.ui.SsmlOutputSpeech;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)

public class TalkLikeSomeoneIntentHandlerTest {

    /*
     * Test that canHandle returns true when the right intent is passed.
     */
    @Test
    public void testCanHandle_RightIntentName() {
        // Arrange
        HandlerInput input = buildHandlerInput(Intents.TALK_LIKE_SOMEONE_INTENT);

        // Act
        boolean actualValue = unitUnderTest.canHandle(input);

        // Assert
        assertTrue("canHandle should return true when TalkLikeSomeoneIntent is passed.", actualValue);
    }

    /*
     * Test that canHandle returns true when the right intent is passed.
     */
    @Test
    public void testCanHandle_IncorrectIntentName() {
        // Arrange
        HandlerInput input = buildHandlerInput("someOtherIntent");

        // Act
        boolean actualValue = unitUnderTest.canHandle(input);

        // Assert
        assertFalse("canHandle should return false when TalkLikeSomeoneIntent is passed.", actualValue);
    }

    /*
     * Test that handle returns the right response in the happy case.
     */
    @Test
    @Ignore
    public void testHandle() {
        // Arrange
        HandlerInput input = buildHandlerInput("someOtherIntent");

        // Act
        Optional<Response> actualResponse = unitUnderTest.handle(input);

        // Assert
        assertEquals("<speak>" + "<voice name=\"Kimberly\">This is how en-US female speak.</voice>" + "</speak>",
                ((SsmlOutputSpeech) actualResponse.get().getOutputSpeech()).getSsml());
        assertTrue("The session should be ended", actualResponse.get().getShouldEndSession());
    }

    private HandlerInput buildHandlerInput(String intentName) {
        Map<String, Slot> slotsMap = new HashMap<>();
        slotsMap.put(Slots.LANGUAGE_SLOT, Slot.builder().withValue("en-US").build());
        slotsMap.put(Slots.GENDER_SLOT, Slot.builder().withValue("female").build());
        Intent intent = Intent.builder().withName(intentName).withSlots(slotsMap).build();
        Request request = IntentRequest.builder().withIntent(intent).build();
        RequestEnvelope requestEnvelope = RequestEnvelope.builder().withRequest(request).build();
        return HandlerInput.builder().withRequestEnvelope(requestEnvelope).build();
    }

    private static final TalkLikeSomeoneIntentHandler unitUnderTest = new TalkLikeSomeoneIntentHandler();
}
