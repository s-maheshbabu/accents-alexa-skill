package com.amazon.ask.accents.handlers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.FileReader;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import com.amazon.ask.accents.intenthandlers.TalkLikeSomeoneIntentHandler;
import com.amazon.ask.accents.model.Slots;
import com.amazon.ask.accents.util.ObjectMapperFactory;
import com.amazon.ask.accents.utterances.UtterancesRepo;
import com.amazon.ask.accents.voices.VoicesRepo;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.amazon.ask.model.slu.entityresolution.Resolution;
import com.amazon.ask.model.slu.entityresolution.Resolutions;
import com.amazon.ask.model.slu.entityresolution.Value;
import com.amazon.ask.model.slu.entityresolution.ValueWrapper;
import com.amazon.ask.model.ui.SsmlOutputSpeech;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)

public class TalkLikeSomeoneIntentHandlerTest
{

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
        Slot languageSlot = buildSlot(Slots.LANGUAGE_SLOT, languageSlotRawValue, languageSlotResolvedValue, languageSlotId);
        Slot genderSlot = buildSlot(Slots.GENDER_SLOT, genderSlotRawValue, genderSlotResolvedValue, genderSlotId);

        Map<String, Slot> slots = new HashMap<>();
        slots.put(Slots.LANGUAGE_SLOT, languageSlot);
        slots.put(Slots.GENDER_SLOT, genderSlot);

        HandlerInput input = buildHandlerInput(slots);

        when(voicesRepo.getVoice(languageSlotId, genderSlotId)).thenReturn(voice);

        List<String> utterances = Arrays.asList("utterance-1", "utterance-2");
        when(utterancesRepo.getUtterances(languageSlotId)).thenReturn(utterances);

        // Act
        Optional<Response> actualResponse = unitUnderTest.handle(input);

        // Assert
        String combinedUtterance = "";
        for (String utterance : utterances)
        {
            combinedUtterance += utterance + ". ";
        }
        assertEquals(ssmlize("<voice name=\"" + voice + "\">" + combinedUtterance + "</voice>"),
                ((SsmlOutputSpeech) actualResponse.get().getOutputSpeech()).getSsml());
        assertTrue("The session should be ended", actualResponse.get().getShouldEndSession());
    }

    /*
     * Test that handle returns a response even when gender slot is missing.
     */
    @Test
    public void testHandle_NoGenderSlot()
    {
        // Arrange
        Slot languageSlot = buildSlot(Slots.LANGUAGE_SLOT, languageSlotRawValue, languageSlotResolvedValue, languageSlotId);

        Map<String, Slot> slots = new HashMap<>();
        slots.put(Slots.LANGUAGE_SLOT, languageSlot);

        HandlerInput input = buildHandlerInput(slots);

        when(voicesRepo.getVoice(languageSlotId, null)).thenReturn(voice);

        List<String> utterances = Arrays.asList("utterance-1", "utterance-2", "utterance-3");
        when(utterancesRepo.getUtterances(languageSlotId)).thenReturn(utterances);

        // Act
        Optional<Response> actualResponse = unitUnderTest.handle(input);

        // Assert
        String combinedUtterance = "";
        for (String utterance : utterances)
        {
            combinedUtterance += utterance + ". ";
        }
        assertEquals(ssmlize("<voice name=\"" + voice + "\">" + combinedUtterance + "</voice>"),
                ((SsmlOutputSpeech) actualResponse.get().getOutputSpeech()).getSsml());
        assertTrue("The session should be ended", actualResponse.get().getShouldEndSession());
    }

    private Slot buildSlot(String slotName, String slotRawValue, String slotResolvedValue, String slotId)
    {
        ValueWrapper resolvedValueWrapper = ValueWrapper.builder().withValue(Value.builder().withId(slotId).withName(slotResolvedValue).build()).build();
        List<ValueWrapper> values = Arrays.asList(resolvedValueWrapper);
        Resolution resolutionPerAuthority = Resolution.builder().withValues(values).build();
        List<Resolution> resolutionsPerAuthority = Arrays.asList(resolutionPerAuthority);
        Resolutions resolutions = Resolutions.builder().withResolutionsPerAuthority(resolutionsPerAuthority).build();

        return Slot.builder().withName(slotName).withValue(slotRawValue).withResolutions(resolutions).build();
    }

    private HandlerInput buildHandlerInput(Map<String, Slot> slots)
    {
        RequestEnvelope requestEnvelope = mock(RequestEnvelope.class);

        IntentRequest intentRequest = mock(IntentRequest.class);
        when(requestEnvelope.getRequest()).thenReturn(intentRequest);

        Intent intent = mock(Intent.class);
        when(intentRequest.getIntent()).thenReturn(intent);

        when(intent.getSlots()).thenReturn(slots == null ? null : new HashMap<>(slots));

        return HandlerInput.builder().withRequestEnvelope(requestEnvelope).build();
    }

    private String ssmlize(String input)
    {
        return "<speak>" + input + "</speak>";
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

    @InjectMocks
    private static final com.amazon.ask.accents.intenthandlers.TalkLikeSomeoneIntentHandler unitUnderTest = new TalkLikeSomeoneIntentHandler();
    private static final String languageSlotRawValue = "languageSlotRawValue";
    private static final String languageSlotResolvedValue = "languageSlotResolvedValue";
    private static final String languageSlotId = "languageSlotId";
    private static final String genderSlotRawValue = "genderSlotRawValue";
    private static final String genderSlotResolvedValue = "genderSlotResolvedValue";
    private static final String genderSlotId = "genderSlotId";
    private static final String voice = "nameOfVoice";
    private final ObjectMapper objectMapper = ObjectMapperFactory.getInstance();
    @Mock
    private VoicesRepo voicesRepo;
    @Mock
    private UtterancesRepo utterancesRepo;
}
