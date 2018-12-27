package com.amazon.ask.accents.handlers;

import static org.mockito.Mockito.when;

import java.io.FileReader;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import com.amazon.ask.accents.util.ObjectMapperFactory;
import com.amazon.ask.accents.utterances.UtterancesRepo;
import com.amazon.ask.accents.voices.VoicesRepo;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.ui.SsmlOutputSpeech;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)

public class TalkLikeSomeoneIntentHandlerTest
{

    @InjectMocks
    private static final TalkLikeSomeoneIntentHandler unitUnderTest = new TalkLikeSomeoneIntentHandler();
    private final ObjectMapper objectMapper = ObjectMapperFactory.getInstance();
    @Mock
    private VoicesRepo voicesRepo;
    @Mock
    private UtterancesRepo utterancesRepo;

    /*
     * Test that canHandle returns true when the right intent is passed.
     */
    @Test
    public void testCanHandle_RightIntentName()
    {
        // Arrange
        HandlerInput input = buildHandlerInput("/testdata/handlerInputs/talkLikeSomeoneIntent/ValidHandlerInput.json");

        // Act
        boolean actualValue = unitUnderTest.canHandle(input);

        // Assert
        assertTrue("canHandle should return true when TalkLikeSomeoneIntent is passed.", actualValue);
    }

    /*
     * Test that canHandle returns true when the right intent is passed.
     */
    @Test
    public void testCanHandle_IncorrectIntentName()
    {
        // Arrange
        HandlerInput input = buildHandlerInput(
                "/testdata/handlerInputs/talkLikeSomeoneIntent/IncorrectIntentName.json");

        // Act
        boolean actualValue = unitUnderTest.canHandle(input);

        // Assert
        assertFalse("canHandle should return false when TalkLikeSomeoneIntent is passed.", actualValue);
    }

    /*
     * Test that handle returns the right response in the happy case.
     */
    @Test
    public void testHandle()
    {
        // Arrange
        HandlerInput input = buildHandlerInput("/testdata/handlerInputs/talkLikeSomeoneIntent/ValidHandlerInput.json");

        String voice = "nameOfVoice";
        when(voicesRepo.getVoice("en-IN", "Male")).thenReturn(voice);

        List<String> utterances = Arrays.asList("utterance-1", "utterance-2");
        when(utterancesRepo.getUtterances("en-IN")).thenReturn(utterances);

        // Act
        Optional<Response> actualResponse = unitUnderTest.handle(input);

        // Assert
        assertEquals("<speak>" + "<voice name=\"" + voice + "\">" + utterances.get(0) + "</voice></speak>",
                ((SsmlOutputSpeech) actualResponse.get().getOutputSpeech()).getSsml());
        assertTrue("The session should be ended", actualResponse.get().getShouldEndSession());
    }

    private HandlerInput buildHandlerInput(String handlerInputResourcePath)
    {
        URL url = getClass().getResource(handlerInputResourcePath);

        RequestEnvelope requestEnvelope;
        try
        {
            requestEnvelope = objectMapper.readValue(new FileReader(url.getPath()), RequestEnvelope.class);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Failed loading the handler inputs from test data files. This is a fatal error.",
                    e);
        }

        return HandlerInput.builder().withRequestEnvelope(requestEnvelope).build();
    }
}
