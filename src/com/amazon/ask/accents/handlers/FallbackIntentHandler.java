package com.amazon.ask.accents.handlers;

import com.amazon.ask.accents.model.Intents;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class FallbackIntentHandler implements RequestHandler {
    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName(Intents.FALLBACK_INTENT));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        String speechText = "Extract this into a fallback prompt. Probably just repeat the help message";
        return input.getResponseBuilder().withSpeech(speechText).withShouldEndSession(true).build();
    }
}
