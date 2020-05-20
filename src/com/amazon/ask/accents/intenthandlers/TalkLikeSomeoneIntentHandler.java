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

import org.apache.commons.lang3.StringUtils;
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

        String languageSlotId = intentUtils.getSlotId(languageSlot);
        String voice = null;
        if (StringUtils.isNotBlank(languageSlotId))
            voice = voicesRepo.getVoice(languageSlotId, genderSlotId);

        if (null == voice) {
            // TODO: Do we need to add cards/APL document here? Note, because of a bug in
            // ASK, you need to add an APL document at the end of the builder below if you
            // ever decide to add one.
            return input.getResponseBuilder().withSpeech(Prompts.NO_VOICE_FOUND).withShouldEndSession(true).build();
        }

        Directive documentDirective = documentRenderer.buildDirective(languageSlotId);
        List<String> utterances = utterancesRepo.getUtterances(languageSlotId);

        String speechText = buildSpeechText(intentUtils.getRawSlotValue(languageSlot), voice, utterances);
        Card card = SimpleCard.builder().withTitle(Cards.CARD_TITLE).withContent(Cards.TALK_LIKE_SOMEONE_INFO).build();

        return input.getResponseBuilder().addDirective(documentDirective).withSpeech(speechText).withCard(card)
                .withShouldEndSession(true).build();
    }

    private String buildSpeechText(String userRequestedAccent, String voice, List<String> utterances) {
        String intro = String.format(INTRO, userRequestedAccent);

        String combinedUtterance = utterances.stream().map(utterance -> utterance + ". ").collect(Collectors.joining());
        return intro + String.format("<voice name=\"%s\"><s>%s</s></voice>", voice, combinedUtterance);
    }

    private static final String INTRO = "<amazon:emotion name=\"excited\"><s>Okay, here is my %s.</s></amazon:emotion>";

    private static final Logger logger = LogManager.getLogger(TalkLikeSomeoneIntentHandler.class);
    private VoicesRepo voicesRepo = VoicesRepo.getInstance();
    private UtterancesRepo utterancesRepo = UtterancesRepo.getInstance();
    private IntentUtils intentUtils = IntentUtils.getInstance();
    private DocumentRenderer documentRenderer = DocumentRenderer.getInstance();
}
