package com.amazon.ask.accents.requesthandlers;

import static com.amazon.ask.request.Predicates.intentName;
import static com.amazon.ask.request.Predicates.requestType;

import java.util.Optional;

import com.amazon.ask.accents.model.Intents;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Response;
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
        return input.getResponseBuilder().withSpeech("Welcome to accents.").withShouldEndSession(true).build();
    }

    private static final Logger logger = LogManager.getLogger(LaunchRequestHandler.class);
}
