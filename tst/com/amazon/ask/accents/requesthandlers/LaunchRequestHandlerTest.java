package com.amazon.ask.accents.requesthandlers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.mockito.Mockito;
import com.amazon.ask.accents.model.Intents;
import com.amazon.ask.accents.prompts.Cards;
import com.amazon.ask.accents.prompts.Prompts;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.ui.SimpleCard;
import com.amazon.ask.model.ui.SsmlOutputSpeech;
import org.junit.Test;

public class LaunchRequestHandlerTest {
    /**
     * Test that we return true if the request is a LaunchRequest.
     */
    @Test
    public void canHandle_LaunchRequest() throws Exception {
        // Arrange
        HandlerInput input = mock(HandlerInput.class, Mockito.RETURNS_DEEP_STUBS);

        LaunchRequest launchRequest = LaunchRequest.builder().build();
        when(input.getRequestEnvelope().getRequest()).thenReturn(launchRequest);
        when(input.matches(any())).thenCallRealMethod();

        // Act
        boolean canHandle = unitUnderTest.canHandle(input);

        // Assert
        assertTrue(canHandle);
    }

    /**
     * Test that we return true if the intent is a StartOverIntent.
     */
    @Test
    public void canHandle_StartOverIntent() throws Exception {
        // Arrange
        HandlerInput input = mock(HandlerInput.class, Mockito.RETURNS_DEEP_STUBS);

        IntentRequest intentRequest = mock(IntentRequest.class, Mockito.RETURNS_DEEP_STUBS);
        when(input.getRequestEnvelope().getRequest()).thenReturn(intentRequest);
        when(intentRequest.getIntent().getName()).thenReturn(Intents.START_OVER_INTENT);

        when(input.matches(any())).thenCallRealMethod();

        // Act
        boolean canHandle = unitUnderTest.canHandle(input);

        // Assert
        assertTrue(canHandle);
    }

    /**
     * Test that the correct response is returned while handling the launch request.
     */
    @Test
    public void handle() throws Exception {
        // Arrange
        RequestEnvelope requestEnvelope = mock(RequestEnvelope.class);
        when(requestEnvelope.getRequest()).thenReturn(mock(IntentRequest.class));
        HandlerInput input = HandlerInput.builder().withRequestEnvelope(requestEnvelope).build();

        // Act
        Optional<Response> response = unitUnderTest.handle(input);

        // Assert
        assertEquals("<speak>" + Prompts.WELCOME_MESSAGE + "</speak>",
                ((SsmlOutputSpeech) response.get().getOutputSpeech()).getSsml());

        assertEquals(Cards.CARD_TITLE, ((SimpleCard) response.get().getCard()).getTitle());
        assertEquals(Cards.WELCOME, ((SimpleCard) response.get().getCard()).getContent());

        assertFalse("The session should be left open.", response.get().getShouldEndSession());
    }

    private LaunchRequestHandler unitUnderTest = new LaunchRequestHandler();
}