package com.amazon.ask.accents.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Request;
import com.amazon.ask.model.Slot;
import org.apache.commons.lang3.Validate;

public final class IntentUtils
{

    /**
     * @param input This input is parsed to extract slots.
     * @return a map of all the slots found in the input. If there are no slots, an empty map is returned.
     * @throws IllegalStateException if the input is in an unexpected state and slots cannot be extracted.
     */
    public static Map<String, Slot> getSlots(HandlerInput input)
    {
        Validate.notNull(input);

        // RequestEnvelope and Request are guaranteed to be present.
        Request request = input.getRequestEnvelope().getRequest();

        if (!(request instanceof IntentRequest))
            throw new IllegalStateException("Request in the handler input should be of the type IntentRequest");
        IntentRequest intentRequest = (IntentRequest) request;

        Intent intent = intentRequest.getIntent();
        if (intent == null)
            throw new IllegalStateException("Handler Input should always come with an intent");

        return intent.getSlots() == null ? new HashMap<>() : intent.getSlots();
    }

    /**
     * @param slot The slot from which the slotId is to be extracted.
     * @return the slotId if found.
     * @throws IllegalStateException if the given slot is in an unexpected state and slotId cannot be extracted.
     */
    public static String getSlotId(Slot slot)
    {
        Validate.notNull(slot);

        Optional<String> slotId = Optional.ofNullable(slot).map(_slot -> _slot.getResolutions())
                .map(resolutions -> resolutions.getResolutionsPerAuthority())
                .map(resolutionsPerAuthority -> resolutionsPerAuthority.stream().findFirst().orElse(null))
                .map(highestConfidenceResolutionPerAuthority -> highestConfidenceResolutionPerAuthority.getValues())
                .map(values -> values.stream().findFirst().orElse(null)).map(firstValue -> firstValue.getValue())
                .map(value -> value.getId());

        return slotId.orElseThrow(() -> new IllegalStateException("Couldn't find an Id in the slot: " + slot));
    }
}