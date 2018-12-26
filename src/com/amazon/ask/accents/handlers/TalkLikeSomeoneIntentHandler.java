package com.amazon.ask.accents.handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Map;
import java.util.Optional;

import com.amazon.ask.accents.model.Gender;
import com.amazon.ask.accents.model.Intents;
import com.amazon.ask.accents.model.Slots;
import com.amazon.ask.accents.util.IntentUtils;
import com.amazon.ask.accents.voices.VoicesRepo;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Request;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TalkLikeSomeoneIntentHandler implements RequestHandler {
    @Override
    public boolean canHandle(HandlerInput input) {
        logger.info("Handler Input: " + input);
        return input.matches(intentName(Intents.TALK_LIKE_SOMEONE_INTENT));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        Request request = input.getRequestEnvelope().getRequest();
        IntentRequest intentRequest = (IntentRequest) request;
        Intent intent = intentRequest.getIntent();
        Map<String, Slot> slots = intent.getSlots();

        Slot languageSlot = slots.get(Slots.LANGUAGE_SLOT);
        Slot genderSlot = slots.get(Slots.GENDER_SLOT);

        String voice = VoicesRepo.getInstance().getVoice(IntentUtils.getSlotId(languageSlot),
                IntentUtils.getSlotId(genderSlot));

        String speechText = String.format("<voice name=\"%s\">This is how how %s %s speak.</voice>", voice,
                languageSlot.getValue(), genderSlot.getValue());
        return input.getResponseBuilder().withSpeech(speechText).withShouldEndSession(true).build();
    }

    private static final Logger logger = LogManager.getLogger(TalkLikeSomeoneIntentHandler.class);
}
