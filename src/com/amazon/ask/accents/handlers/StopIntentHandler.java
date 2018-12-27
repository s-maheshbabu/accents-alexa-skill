package com.amazon.ask.accents.handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import com.amazon.ask.accents.model.Intents;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

public class StopIntentHandler implements RequestHandler
{
    @Override
    public boolean canHandle(HandlerInput input)
    {
        return input.matches(intentName(Intents.STOP_INTENT));
    }

    @Override
    public Optional<Response> handle(HandlerInput input)
    {
        String speechText = "Extract this into an exit prompt. Probably just say Okay";
        return input.getResponseBuilder().withSpeech(speechText).withShouldEndSession(true).build();
    }
}
