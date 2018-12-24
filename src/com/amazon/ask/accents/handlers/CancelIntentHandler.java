package com.amazon.ask.accents.handlers;

import com.amazon.ask.accents.model.Intents;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class CancelIntentHandler implements RequestHandler {
    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName(Intents.CANCEL_INTENT));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        String speechText = "Extract this into an exit prompt. Probably just say Okay";
        return input.getResponseBuilder().withSpeech(speechText).withShouldEndSession(true).build();
    }
}
