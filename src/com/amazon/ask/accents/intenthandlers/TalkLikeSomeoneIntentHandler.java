package com.amazon.ask.accents.intenthandlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.amazon.ask.accents.apl.DocumentRenderer;
import com.amazon.ask.accents.model.Intents;
import com.amazon.ask.accents.model.Slots;
import com.amazon.ask.accents.prompts.Cards;
import com.amazon.ask.accents.prompts.Prompts;
import com.amazon.ask.accents.util.IntentUtils;
import com.amazon.ask.accents.utterances.UtterancesRepo;
import com.amazon.ask.accents.voices.VoicesRepo;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Directive;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.amazon.ask.model.ui.Card;
import com.amazon.ask.model.ui.SimpleCard;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TalkLikeSomeoneIntentHandler implements RequestHandler {
    @Override
    public boolean canHandle(HandlerInput input) {
        logger.info("Request Envelope: " + input.getRequestEnvelope());
        return input.matches(intentName(Intents.TALK_LIKE_SOMEONE_INTENT));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        Map<String, Slot> slots = intentUtils.getSlots(input);

        Slot languageSlot = slots.get(Slots.LANGUAGE_SLOT);
        Slot genderSlot = slots.get(Slots.GENDER_SLOT);

        String genderSlotId = null;
        if (genderSlot != null)
            genderSlotId = intentUtils.getSlotId(genderSlot);
        String voice = voicesRepo.getVoice(intentUtils.getSlotId(languageSlot), genderSlotId);

        Directive documentDirective = documentRenderer.buildDirective(intentUtils.getSlotId(languageSlot));
        if (null == voice) {
            // TODO: Do we need to add cards here?
            return input.getResponseBuilder().addDirective(documentDirective).withSpeech(Prompts.NO_VOICE_FOUND)
                    .withShouldEndSession(true).build();
        }

        List<String> utterances = utterancesRepo.getUtterances(intentUtils.getSlotId(languageSlot));

        String speechText = wrapUtternacesInVoiceTag(voice, utterances);
        Card card = SimpleCard.builder().withTitle(Cards.CARD_TITLE).withContent(Cards.TALK_LIKE_SOMEONE_INFO).build();

        return input.getResponseBuilder().addDirective(documentDirective).withSpeech(speechText).withCard(card)
                .withShouldEndSession(true).build();
    }

    private String wrapUtternacesInVoiceTag(String voice, List<String> utterances) {
        String combinedUtterance = utterances.stream().map(utterance -> utterance + ". ").collect(Collectors.joining());
        return String.format("<voice name=\"%s\">%s</voice>", voice, combinedUtterance);
    }

    private static final Logger logger = LogManager.getLogger(TalkLikeSomeoneIntentHandler.class);
    private VoicesRepo voicesRepo = VoicesRepo.getInstance();
    private UtterancesRepo utterancesRepo = UtterancesRepo.getInstance();
    private IntentUtils intentUtils = IntentUtils.getInstance();
    private DocumentRenderer documentRenderer = DocumentRenderer.getInstance();
}
