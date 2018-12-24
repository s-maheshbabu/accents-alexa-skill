package com.amazon.ask.accents.util;

import com.amazon.ask.model.Slot;

public final class IntentUtils {
    public static String getSlotId(Slot slot) {
        return slot.getResolutions().getResolutionsPerAuthority().get(0).getValues().get(0).getValue().getId();
    }
}