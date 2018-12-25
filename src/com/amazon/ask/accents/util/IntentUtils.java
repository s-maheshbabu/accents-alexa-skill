package com.amazon.ask.accents.util;

import java.util.Optional;
import java.util.List;

import com.amazon.ask.model.Slot;
import com.amazon.ask.model.slu.entityresolution.Value;
import com.amazon.ask.model.slu.entityresolution.Resolution;

public final class IntentUtils {
    public static String getSlotId(Slot slot) {
        Optional<String> slotId = Optional.ofNullable(slot).map(_slot -> _slot.getResolutions())
                .map(resolutions -> resolutions.getResolutionsPerAuthority())
                .map(resolutionsPerAuthority -> resolutionsPerAuthority.stream().findFirst().orElse(null))
                .map(highestConfidenceResolutionPerAuthority -> highestConfidenceResolutionPerAuthority.getValues())
                .map(values -> values.stream().findFirst().orElse(null)).map(firstValue -> firstValue.getValue())
                .map(value -> value.getId());

        return slotId.orElseThrow(() -> new IllegalStateException("Couldn't find an Id in the slot: " + slot);
    }
}