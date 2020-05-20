package com.amazon.ask.accents.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Request;
import com.amazon.ask.model.Slot;
import com.amazon.ask.model.slu.entityresolution.Value;

import org.apache.commons.lang3.Validate;

/**
 * A set of utility functions to work with Alexa Skill input structures. Note:
 * This class can be a set of static helper functions but is modeled as a
 * singleton to enable testing without using frameworks like PowerMockito.
 */
public final class IntentUtils {
    private IntentUtils() {
    }

    /**
     * @param input This input is parsed to extract slots.
     * @return a map of all the slots found in the input. If there are no slots, an
     *         empty map is returned.
     * @throws IllegalStateException if the input is in an unexpected state and
     *                               slots cannot be extracted.
     */
    public Map<String, Slot> getSlots(HandlerInput input) {
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
     * @return the slotId if found and null otherwise.
     */
    public String getSlotId(Slot slot) {
        Validate.notNull(slot);

        Optional<String> slotId = getSlotValue(slot).map(value -> value.getId());

        return slotId.orElse(null);
    }

    /**
     * @param slot The slot from which the slotValue is to be extracted.
     * @return the slotValue if found and null otherwise.
     */
    public String getSlotName(Slot slot) {
        Validate.notNull(slot);

        Optional<String> slotName = getSlotValue(slot).map(value -> value.getName());

        return slotName.orElse(null);
    }

    /**
     * @param slot The slot from which the raw slot value is to be extracted. This
     *             is the value that the user actually uttered.
     * @return the raw slot value if found and null otherwise.
     */
    public String getRawSlotValue(Slot slot) {
        Validate.notNull(slot);

        return slot.getValue();
    }

    public static IntentUtils getInstance() {
        if (instance == null)
            instance = new IntentUtils();
        return instance;
    }

    private Optional<Value> getSlotValue(Slot slot) {
        return Optional.ofNullable(slot).map(_slot -> _slot.getResolutions())
                .map(resolutions -> resolutions.getResolutionsPerAuthority())
                .map(resolutionsPerAuthority -> resolutionsPerAuthority.stream().findFirst().orElse(null))
                .map(highestConfidenceResolutionPerAuthority -> highestConfidenceResolutionPerAuthority.getValues())
                .map(values -> values.stream().findFirst().orElse(null)).map(firstValue -> firstValue.getValue());
    }

    private static IntentUtils instance;
}