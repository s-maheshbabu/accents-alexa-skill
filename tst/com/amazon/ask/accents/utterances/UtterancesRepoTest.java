package com.amazon.ask.accents.utterances;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;

public class UtterancesRepoTest {
    /**
     * Test that a random set of utterances are returned for a given language.
     */
    @Test
    public void getUtterances() {
        // Arrange
        String language = "en-IN";

        int numberOfUtterances = utterancesMap.get(language).size();

        // The idea here is to run this test 1000 times the number of utterances for the
        // given language and hoping that each utterance will get
        // selected at least once. Sure, there is a slim chance that an utterance
        // doesn't get selected in all of those runs making this test undeterministic.
        // However, it is very unlikely and if it happens, it is probably an indication
        // of poor random selection logic in the source code than us getting really
        // unlucky.
        Set<String> allActualUtterances = new HashSet<>();
        for (int i = 0; i < numberOfUtterances * 1000; i++) {
            // Act
            List<String> actualUtterances = unitUnderTest.getUtterances(language);

            assertTrue("At least one utterance should be returned.", actualUtterances.size() > 0);
            assertTrue(String.format("At most %s utterances should be returned.", UtterancesRepo.MAX_NUMBER_UTTERANCES),
                    actualUtterances.size() <= UtterancesRepo.MAX_NUMBER_UTTERANCES);

            allActualUtterances.addAll(actualUtterances);
        }

        // Assert
        List<String> allUtterancesThatExist = utterancesMap.get(language);
        assertEquals(allUtterancesThatExist.size(), allActualUtterances.size());
        for (String utterance : allActualUtterances) {
            assertTrue(allUtterancesThatExist.contains(utterance));
        }
    }

    /**
     * Test that when there are fewer utterances than the maximum that can be
     * returned, we return all the available utterances.
     */
    @Test
    public void getUtterances_TooFewUtterances() {
        // Arrange
        String language = "default";

        // Act
        List<String> actualUtternaces = unitUnderTest.getUtterances(language);

        // Assert
        List<String> expectedUtterances = utterancesMap.get(language);
        assertEquals(expectedUtterances.size(), actualUtternaces.size());
        for (String utterance : actualUtternaces) {
            assertTrue(expectedUtterances.contains(utterance));
        }
    }

    /**
     * Test that when we don't have specific utterances for the given language, we
     * return from the default set.
     */
    @Test
    public void getUtterances_UnsupportedLanguage() {
        // Arrange
        String language = "unknownLanguage";

        // Act
        List<String> actualUtternaces = unitUnderTest.getUtterances(language);

        // Assert
        List<String> expectedUtterances = utterancesMap.get(UtterancesRepo.DEFAULT_LANGUAGE_KEY);
        for (String utterance : actualUtternaces) {
            assertTrue(expectedUtterances.contains(utterance));
        }
    }

    /**
     * Test that an NPE is thrown if the language parameter is null.
     */
    @Test(expected = NullPointerException.class)
    public void getUtterances_NoLanguage() {
        // Arrange
        String language = null;

        // Act
        unitUnderTest.getUtterances(language);
    }

    @BeforeClass
    public static void setup() {
        Map<String, List<String>> map = new HashMap<>();

        map.put("en-IN",
                Arrays.asList("Please do the needful.", "Lakhs and lakhs of people.", "I want pin-drop silence.",
                        "He is out of station.", "My son just passed out of college.",
                        "My neighbour is foreign-returned.", "My daughter is convent-educated.",
                        "Where is the nearest departmental store?", "My teacher is sitting on my head.",
                        "My friend is eating my brain.", "Why This Kolaveri Di?"));

        map.put("it-IT", Arrays.asList("Italian sentence - 1", "Italian sentence - 2", "Italian sentence - 3",
                "Italian sentence - 4", "Italian sentence - 5"));

        map.put("de-DE", Arrays.asList("German sentence - 1", "German sentence - 2", "German sentence - 3"));

        map.put(UtterancesRepo.DEFAULT_LANGUAGE_KEY, Arrays.asList("Default Language sentence - 1",
                "Default Language sentence - 2", "Default Language sentence - 3"));

        utterancesMap = Collections.unmodifiableMap(map);
    }

    private static final UtterancesRepo unitUnderTest = UtterancesRepo.getInstance();
    private static Map<String, List<String>> utterancesMap;
}