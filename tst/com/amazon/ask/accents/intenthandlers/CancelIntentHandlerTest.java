package com.amazon.ask.accents.intenthandlers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.mockito.Mockito;
import com.amazon.ask.accents.model.Intents;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.ui.SsmlOutputSpeech;
import org.junit.Test;

public class CancelIntentHandlerTest
{
    /*
    * Test that canHandle returns true when the right intent is passed.
    */
    @Test
    public void testCanHandle_RightIntentName() throws Exception
    {
        // Arrange
        HandlerInput input = mock(HandlerInput.class, Mockito.RETURNS_DEEP_STUBS);

        IntentRequest intentRequest = mock(IntentRequest.class, Mockito.RETURNS_DEEP_STUBS);
        when(input.getRequestEnvelope().getRequest()).thenReturn(intentRequest);
        when(intentRequest.getIntent().getName()).thenReturn(Intents.CANCEL_INTENT);

        when(input.matches(any())).thenCallRealMethod();

        // Act
        boolean canHandle = unitUnderTest.canHandle(input);

        // Assert
        assertTrue(canHandle);
    }

    @Test
    public void handle() throws Exception
    {
        // Arrange
        RequestEnvelope requestEnvelope = mock(RequestEnvelope.class);
        when(requestEnvelope.getRequest()).thenReturn(mock(IntentRequest.class));
        HandlerInput input = HandlerInput.builder().withRequestEnvelope(requestEnvelope).build();

        // Act
        Optional<Response> response = unitUnderTest.handle(input);

        // Assert
        assertEquals("<speak>Goodbye.</speak>", ((SsmlOutputSpeech) response.get().getOutputSpeech()).getSsml());
        assertTrue("The session should be ended", response.get().getShouldEndSession());
    }

    private CancelIntentHandler unitUnderTest = new CancelIntentHandler();
}