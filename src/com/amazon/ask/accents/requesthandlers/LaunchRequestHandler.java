package com.amazon.ask.accents.requesthandlers;

import static com.amazon.ask.request.Predicates.intentName;
import static com.amazon.ask.request.Predicates.requestType;

import java.util.Optional;

import com.amazon.ask.accents.model.Intents;
import com.amazon.ask.accents.prompts.Cards;
import com.amazon.ask.accents.prompts.Prompts;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.ui.Card;
import com.amazon.ask.model.ui.SimpleCard;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LaunchRequestHandler implements RequestHandler
{
    @Override
    public boolean canHandle(HandlerInput input)
    {
        logger.info("Request Envelope: " + input.getRequestEnvelope());
        return input.matches(requestType(LaunchRequest.class)) || input.matches(intentName(Intents.START_OVER_INTENT));
    }

    @Override
    public Optional<Response> handle(HandlerInput input)
    {
        Card card = SimpleCard.builder().withTitle(Cards.CARD_TITLE).withContent(Cards.WELCOME).build();
        return input.getResponseBuilder().withSpeech(Prompts.WELCOME_MESSAGE).withShouldEndSession(false).withCard(card).build();
    }

    private static final Logger logger = LogManager.getLogger(LaunchRequestHandler.class);
}
