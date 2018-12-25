package com.amazon.ask.accents.util;

import java.util.Optional;

import com.amazon.ask.model.Slot;

public final class IntentUtils {
    public static String getSlotId(Slot slot) {
        Optional<String> slotId = Optional.ofNullable(slot).map(_slot -> _slot.getResolutions())
                .map(resolutions -> resolutions.getResolutionsPerAuthority())
                .map(resolutionsPerAuthority -> resolutionsPerAuthority.size() > 0 ? resolutionsPerAuthority.get(0)
                        : null)
                .map(highestConfidenceResolutionPerAuthority -> highestConfidenceResolutionPerAuthority.getValues())
                .map(values -> values.size() > 0 ? values.get(0) : null).map(firstValue -> firstValue.getValue())
                .map(value -> value.getId());

        return slotId.orElseThrow(() -> new IllegalStateException("Couldn't find an Id in the slot: " + slot));
    }
}