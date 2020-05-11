package com.amazon.ask.accents.intenthandlers;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.UserEventHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.interfaces.alexa.presentation.apl.UserEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserEventHandlerCustom implements UserEventHandler {
    @Override
    public boolean canHandle(HandlerInput input, UserEvent userEvent) {
        logger.info("UserEvent Request Envelope: " + input.getRequestEnvelope());
        return true;
    }

    @Override
    public Optional<Response> handle(HandlerInput input, UserEvent userEvent) {
        logger.error("----- UserEvent handled.");
        return input.getResponseBuilder().withSpeech("user event handled").withShouldEndSession(true).build();
    }

    private static final Logger logger = LogManager.getLogger(UserEventHandlerCustom.class);
}
