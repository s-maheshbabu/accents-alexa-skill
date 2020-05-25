package com.amazon.ask.accents.requesthandlers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.mockito.Mockito;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.SessionEndedRequest;
import org.junit.Test;

public class SessionEndedRequestHandlerTest {
    /**
     * Test that we return true if the request is a SessionEndedRequest.
     */
    @Test
    public void canHandle_SessionEndedRequest() throws Exception {
        // Arrange
        HandlerInput input = mock(HandlerInput.class, Mockito.RETURNS_DEEP_STUBS);

        SessionEndedRequest sessionEndedRequest = SessionEndedRequest.builder().build();
        when(input.getRequestEnvelope().getRequest()).thenReturn(sessionEndedRequest);
        when(input.matches(any())).thenCallRealMethod();

        // Act
        boolean canHandle = unitUnderTest.canHandle(input);

        // Assert
        assertTrue(canHandle);
    }

    /**
     * Test that the handling of the request ends the session and that nothing else
     * is said or shown.
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
        assertTrue("The session should be ended.", response.get().getShouldEndSession());

        assertNull("No speech should be rendered when the user requests for the session to end",
                response.get().getOutputSpeech());

        assertNull("No cards should be rendered when the user requests for the session to end",
                response.get().getCard());
    }

    private SessionEndedRequestHandler unitUnderTest = new SessionEndedRequestHandler();
}