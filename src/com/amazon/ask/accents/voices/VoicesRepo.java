package com.amazon.ask.accents.voices;

import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazon.ask.accents.errors.UnsupportedLanguageException;
import com.amazon.ask.accents.model.Gender;
import com.amazon.ask.accents.util.ObjectMapperFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.Validate;

/**
 * Maintains knowledge about all supported languages and the voices for each
 * language by gender.
 */
public class VoicesRepo {

    private VoicesRepo() {
        URL url = getClass().getResource(VOICES_DATA);

        Map<String, Voice> map = new HashMap<>();
        try {
            final JsonNode node = objectMapper.readTree(new FileReader(url.getPath()));
            if (!node.at("/supportedVoices/voices").isArray())
                throw new RuntimeException();

            Voice[] voices = objectMapper.treeToValue(node.at("/supportedVoices/voices"), Voice[].class);

            for (Voice v : voices) {
                map.put(v.getLocale(), v);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed loading the mapping of languages to voices. This is a fatal error.", e);
        }

        voicesMap = Collections.unmodifiableMap(map);
    }

    /**
     * Obtains a random voice among the available voices for the given language and
     * gender.
     *
     * @param language The language for which a voice needs to be found. This field
     *                 is required.
     * @param gender   The gender of the voice. If this value is not provided, a
     *                 voice will be chosen randomly across both genders.
     * @return a voice for the given language / gender combination. If no such voice
     *         could be found, returns null.
     * @throws UnsupportedLanguageException if the given language is not known.
     */
    public String getVoice(String language, String gender) {
        Validate.notEmpty(language, "Language cannot be empty");

        Voice voice = voicesMap.get(language);
        if (voice == null) {
            throw new UnsupportedLanguageException(String.format("The language %s is not supported", language));
        }

        List<String> applicableVoices = null;
        if (gender == null || (!Gender.Male.name().equals(gender) && !Gender.Female.name().equals(gender))) {
            applicableVoices = new ArrayList<>(voice.getMale());
            applicableVoices.addAll(voice.getFemale());
        } else {
            if (Gender.Male.name().equals(gender))
                applicableVoices = voice.getMale();
            else {
                applicableVoices = voice.getFemale();
            }
        }

        // This can happen if there are no voices for the specified gender even though
        // the language itself is supported.
        if (applicableVoices == null || applicableVoices.size() < 1) {
            return null;
        }

        Collections.shuffle(applicableVoices);
        return applicableVoices.get(0);
    }

    public Map<String, Voice> getSupportedVoices() {
        return voicesMap;
    }

    public static VoicesRepo getInstance() {
        if (instance == null)
            instance = new VoicesRepo();
        return instance;
    }

    // A mapping of supported languages and voices. Authoritative list at
    // https://developer.amazon.com/docs/custom-skills/speech-synthesis-markup-language-ssml-reference.html#voice
    private static final String VOICES_DATA = "/resources/data/supported_voices.json";
    private static VoicesRepo instance;
    private final Map<String, Voice> voicesMap;
    private final ObjectMapper objectMapper = ObjectMapperFactory.getInstance();
}