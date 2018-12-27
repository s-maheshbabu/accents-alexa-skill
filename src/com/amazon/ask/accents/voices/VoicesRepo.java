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
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.Validate;

/**
 * Maintains knowledge about all supported languages and the voices for each
 * language by gender.
 */
public class VoicesRepo
{

    // A mapping of supported languages and voices. Authoritative list at
    // https://developer.amazon.com/docs/custom-skills/speech-synthesis-markup-language-ssml-reference.html#voice
    private static final String VOICES_DATA = "/resources/data/supported_voices.json";
    private static VoicesRepo instance;
    private final Map<String, Map<String, List<String>>> voicesMap;
    private final ObjectMapper objectMapper = ObjectMapperFactory.getInstance();

    private VoicesRepo()
    {
        URL url = getClass().getResource(VOICES_DATA);

        Map<String, Map<String, List<String>>> son;
        try
        {
            son = objectMapper.readValue(new FileReader(url.getPath()), HashMap.class);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Failed loading the mapping of languages to voices. This is a fatal error.", e);
        }

        voicesMap = Collections.unmodifiableMap(son);
    }

    public static VoicesRepo getInstance()
    {
        if (instance == null)
            instance = new VoicesRepo();
        return instance;
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
     * could be found, returns null.
     * @throws UnsupportedLanguageException if the given language is not known.
     */
    public String getVoice(String language, String gender)
    {
        Validate.notEmpty(language, "Language cannot be empty");

        Map<String, List<String>> voicesByGenderMap = voicesMap.get(language);
        if (voicesByGenderMap == null)
        {
            throw new UnsupportedLanguageException(String.format("The language %s is not supported", language));
        }

        List<String> applicableVoices;
        if (gender != null)
        {
            applicableVoices = voicesByGenderMap.get(gender);
        }
        else
        {
            applicableVoices = new ArrayList<>(voicesByGenderMap.get(Gender.Male.name()));
            applicableVoices.addAll(voicesByGenderMap.get(Gender.Female.name()));
        }
        // TODO: This block of code needs to be tested. We need to be able to inject
        // voicesMap from test class to be albe to test this because the real data has
        // no language/gender combination that doesn't support any voices.
        if (applicableVoices.size() < 1)
        {
            throw new IllegalStateException(String.format(
                    "Each language/gender combination should have at least one supported languages. Language: %s. Gender: %s",
                    language, gender));
        }
        // We need throw an InvalidStateException if applicableVoices is of size 0

        Collections.shuffle(applicableVoices);
        return applicableVoices.get(0);
    }
}