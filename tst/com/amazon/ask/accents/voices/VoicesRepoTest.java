package com.amazon.ask.accents.voices;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.amazon.ask.accents.errors.UnsupportedLanguageException;

import org.apache.logging.log4j.util.Strings;
import org.junit.BeforeClass;
import org.junit.Test;

public class VoicesRepoTest {
    /**
     * Test that a random voice can be obtained in the happy case.
     */
    @Test
    public void getVoice() {
        // Arrange
        System.out.println("$$$$$$$$$$$");
        String language = "en-US";
        String gender = "Female";

        int numberOfVoices = voicesMap.get(language).get(gender).size();

        // The idea here is to run this test 1000 times the number of voices for the
        // given language / gender combination and hoping that each voice will get
        // selected at least once. Sure, there is a slim chance that a voice doesn't get
        // selected in all of those runs making this test undeterministic. However, it
        // is very unlikely and if it happens, it is probably an indication of poor
        // random selection logic in the source code than us getting really unlucky.
        Set<String> actualVoices = new HashSet<>();
        for (int i = 0; i < numberOfVoices * 1000; i++) {
            // Act
            actualVoices.add(unitUnderTest.getVoice(language, gender));
        }

        // Assert
        List<String> expectedVoices = voicesMap.get(language).get(gender);
        assertEquals(expectedVoices.size(), actualVoices.size());
        for (String voice : actualVoices) {
            assertTrue(expectedVoices.contains(voice));
        }
    }

    /**
     * Test that a voice can be obtained in the case when there is only one voiceId
     * that matches the given constraints.
     * 
     * TODO: This is a fragile test because if and when Amazon starts supporting
     * more than once voice for the en-AU/Female combination, this test will break.
     * This test class needs to be refactored to not use the real supported voices
     * but rather injec a mock mapping of languages->voices and inject it into
     * VoicesRepo class.
     */
    @Test
    public void getVoice_OnlyOneVoice() {
        // Arrange
        String language = "en-AU";
        String gender = "Female";

        // Act
        String actualVoice = unitUnderTest.getVoice(language, gender);

        // Assert
        List<String> expectedVoice = voicesMap.get(language).get(gender);
        assertTrue("Returned voiceId should be on of the voices for the given language and gender.",
                expectedVoice.get(0).equals(actualVoice));
    }

    /**
     * Test that a voice can be obtained when a gender is not specified. A voice Id
     * for either gender can be returned.
     */
    @Test
    public void getVoice_NoGender() {
        // Arrange
        String language = "en-AU";
        String gender = null;

        // Act
        String actualVoice = unitUnderTest.getVoice(language, gender);

        // Assert
        List<String> maleVoiceIds = voicesMap.get(language).get("Male");
        List<String> femaleVoiceIds = voicesMap.get(language).get("Female");

        List<String> allVoiceIds = new ArrayList<String>(maleVoiceIds);
        allVoiceIds.addAll(femaleVoiceIds);
        assertTrue("Returned voiceId should be on of the voices for the given language and gender.",
                allVoiceIds.contains(actualVoice));
    }

    /**
     * Test that an NPE is thrown if the language parameter is null.
     */
    @Test(expected = NullPointerException.class)
    public void getVoice_NoLanguage() {
        // Arrange
        String language = null;
        String gender = "anyGender";

        // Act
        unitUnderTest.getVoice(language, gender);
    }

    /**
     * Test that an exception is raised if the language paramter is empty.
     */
    @Test(expected = IllegalArgumentException.class)
    public void getVoice_EmptyLanguage() {
        // Arrange
        String language = Strings.EMPTY;
        String gender = "anyGender";

        // Act
        unitUnderTest.getVoice(language, gender);
    }

    /**
     * Test that an exception is raised if an unsupported language is passed.
     */
    @Test(expected = UnsupportedLanguageException.class)
    public void getVoice_UnsupportedLanguage() {
        // Arrange
        String language = "anUnsupportedLangugage";
        String gender = "anyGender";

        // Act
        unitUnderTest.getVoice(language, gender);
    }

    @BeforeClass
    public static void setup() {
        Map<String, Map<String, List<String>>> map = new HashMap<>();

        Map<String, List<String>> en_USVoicesMapByGender = new HashMap<>();
        en_USVoicesMapByGender.put("Female",
                Collections.unmodifiableList(Arrays.asList("Ivy", "Joanna", "Kendra", "Kimberly", "Salli")));
        en_USVoicesMapByGender.put("Male", Collections.unmodifiableList(Arrays.asList("Joey", "Justin", "Matthew")));
        map.put("en-US", en_USVoicesMapByGender);

        Map<String, List<String>> en_AUVoicesMapByGender = new HashMap<>();
        en_AUVoicesMapByGender.put("Female", Collections.unmodifiableList(Arrays.asList("Nicole")));
        en_AUVoicesMapByGender.put("Male", Collections.unmodifiableList(Arrays.asList("Russell")));
        map.put("en-AU", en_AUVoicesMapByGender);

        Map<String, List<String>> en_GBVoicesMapByGender = new HashMap<>();
        en_GBVoicesMapByGender.put("Female", Collections.unmodifiableList(Arrays.asList("Amy", "Emma")));
        en_GBVoicesMapByGender.put("Male", Collections.unmodifiableList(Arrays.asList("Brian")));
        map.put("en-GB", en_GBVoicesMapByGender);

        Map<String, List<String>> en_INVoicesMapByGender = new HashMap<>();
        en_INVoicesMapByGender.put("Female", Collections.unmodifiableList(Arrays.asList("Aditi")));
        en_INVoicesMapByGender.put("Male", Collections.unmodifiableList(Arrays.asList("Raveena")));
        map.put("en-IN", en_INVoicesMapByGender);

        Map<String, List<String>> de_DEVoicesMapByGender = new HashMap<>();
        de_DEVoicesMapByGender.put("Female", Collections.unmodifiableList(Arrays.asList("Marlene", "Vicki")));
        de_DEVoicesMapByGender.put("Male", Collections.unmodifiableList(Arrays.asList("Hans")));
        map.put("de-DE", de_DEVoicesMapByGender);

        Map<String, List<String>> es_ESVoicesMapByGender = new HashMap<>();
        es_ESVoicesMapByGender.put("Female", Collections.unmodifiableList(Arrays.asList("Conchita")));
        es_ESVoicesMapByGender.put("Male", Collections.unmodifiableList(Arrays.asList("Enrique")));
        map.put("es-ES", es_ESVoicesMapByGender);

        Map<String, List<String>> it_ITVoicesMapByGender = new HashMap<>();
        it_ITVoicesMapByGender.put("Female", Collections.unmodifiableList(Arrays.asList("Carla")));
        it_ITVoicesMapByGender.put("Male", Collections.unmodifiableList(Arrays.asList("Giorgio")));
        map.put("it-IT", it_ITVoicesMapByGender);

        Map<String, List<String>> ja_JPVoicesMapByGender = new HashMap<>();
        ja_JPVoicesMapByGender.put("Female", Collections.unmodifiableList(Arrays.asList("Mizuki")));
        ja_JPVoicesMapByGender.put("Male", Collections.unmodifiableList(Arrays.asList("Takumi")));
        map.put("ja-JP", ja_JPVoicesMapByGender);

        Map<String, List<String>> fr_FRVoicesMapByGender = new HashMap<>();
        fr_FRVoicesMapByGender.put("Female", Collections.unmodifiableList(Arrays.asList("Celine", "Lea")));
        fr_FRVoicesMapByGender.put("Male", Collections.unmodifiableList(Arrays.asList("Mathieu")));
        map.put("fr-FR", fr_FRVoicesMapByGender);

        voicesMap = Collections.unmodifiableMap(map);
    }

    private static final VoicesRepo unitUnderTest = VoicesRepo.getInstance();

    private static Map<String, Map<String, List<String>>> voicesMap;
}