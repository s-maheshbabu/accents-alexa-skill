package com.amazon.ask.accents.handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.amazon.ask.accents.model.Intents;
import com.amazon.ask.accents.model.Slots;
import com.amazon.ask.accents.util.IntentUtils;
import com.amazon.ask.accents.utterances.UtterancesRepo;
import com.amazon.ask.accents.voices.VoicesRepo;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TalkLikeSomeoneIntentHandler implements RequestHandler
{
    private static final Logger logger = LogManager.getLogger(TalkLikeSomeoneIntentHandler.class);
    private VoicesRepo voicesRepo = VoicesRepo.getInstance();
    private UtterancesRepo utterancesRepo = UtterancesRepo.getInstance();
    private IntentUtils intentUtils = IntentUtils.getInstance();

    @Override
    public boolean canHandle(HandlerInput input)
    {
        logger.info("Handler Input: " + input);
        return input.matches(intentName(Intents.TALK_LIKE_SOMEONE_INTENT));
    }

    @Override
    public Optional<Response> handle(HandlerInput input)
    {
        Map<String, Slot> slots = intentUtils.getSlots(input);

        Slot languageSlot = slots.get(Slots.LANGUAGE_SLOT);
        Slot genderSlot = slots.get(Slots.GENDER_SLOT);

        String genderSlotId = null;
        if(genderSlot != null) genderSlotId = intentUtils.getSlotId(genderSlot);
        String voice = voicesRepo.getVoice(intentUtils.getSlotId(languageSlot),
                genderSlotId);

        List<String> utterances = utterancesRepo.getUtterances(intentUtils.getSlotId(languageSlot));

        String speechText = String.format("<voice name=\"%s\">%s</voice>", voice, utterances.get(0));
        return input.getResponseBuilder().withSpeech(speechText).withShouldEndSession(true).build();
    }
}
