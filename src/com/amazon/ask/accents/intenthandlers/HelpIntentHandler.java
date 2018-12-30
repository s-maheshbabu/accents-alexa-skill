package com.amazon.ask.accents.intenthandlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import com.amazon.ask.accents.model.Intents;
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
        String speechText = "You can tell me your favorite color by saying, my favorite color is red";
        String repromptText = "Please tell me your favorite color by saying, my favorite color is red";
        return input.getResponseBuilder().withSimpleCard("ColorSession", speechText).withSpeech(speechText)
                .withReprompt(repromptText).withShouldEndSession(false).build();
    }
}
