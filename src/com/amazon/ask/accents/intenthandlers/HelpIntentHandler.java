package com.amazon.ask.accents.intenthandlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import com.amazon.ask.accents.model.Intents;
import com.amazon.ask.accents.prompts.Prompts;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

public class HelpIntentHandler implements RequestHandler
{
    @Override
    public boolean canHandle(HandlerInput input)
    {
        return input.matches(intentName(Intents.HELP_INTENT));
    }

    @Override
    public Optional<Response> handle(HandlerInput input)
    {
        // TODO: shouldEndSession should be false.
        return input.getResponseBuilder().withSpeech(Prompts.HELP)
                .withReprompt(Prompts.HELP_REPROMPT).withShouldEndSession(true).build();
    }
}
