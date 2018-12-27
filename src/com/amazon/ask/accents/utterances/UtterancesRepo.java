package com.amazon.ask.accents.utterances;

import java.io.FileReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import org.apache.commons.lang3.Validate;

/**
 * Maintains a repo of utterances by language.
 */
public class UtterancesRepo {

    /**
     * Obtains a random set of utterances (in SSML) for the given language from the
     * utterances repo. If no specific utterances are found for the given language,
     * utterances will be returned from a default set.
     * 
     * @param language The language for which utterances are needed. This field is
     *                 required.
     * @return a set of utterances for the given language. There will always be at
     *         least one utterances and atmost three utterances returned.
     */
    public List<String> getUtterances(String language) {
        Validate.notEmpty(language, "Language cannot be empty");

        if (!utterancesMap.containsKey(language))
            language = DEFAULT_LANGUAGE_KEY;
        List<String> allUtterances = utterancesMap.get(language);
        Collections.shuffle(allUtterances);

        return allUtterances.subList(0, Math.min(allUtterances.size(), MAX_NUMBER_UTTERANCES));
    }

    private UtterancesRepo() {
        URL url = getClass().getResource(UTTERANCES_DATA);
        Type utterancesMapType = new TypeToken<Map<String, List<String>>>() {
        }.getType();
        Map<String, List<String>> son = null;
        try {
            JsonReader reader = new JsonReader(new FileReader(url.getPath()));
            son = new Gson().fromJson(reader, utterancesMapType);
        } catch (Exception e) {
            throw new RuntimeException("Failed loading the mapping of languages to utterances. This is a fatal error.",
                    e);
        }

        utterancesMap = Collections.unmodifiableMap(son);
    }

    public static UtterancesRepo getInstance() {
        if (instance == null)
            instance = new UtterancesRepo();
        return instance;
    }

    // Utterances by language. Any utterance would work for any language but the
    // data set is constructed to have some cultural significance. For ex,
    // utterances for en-IN might talk about Cricket.
    private static final String UTTERANCES_DATA = "/resources/data/utterances.json";

    private static UtterancesRepo instance;
    private final Map<String, List<String>> utterancesMap;

    public static final int MAX_NUMBER_UTTERANCES = 5;
    public static final String DEFAULT_LANGUAGE_KEY = "default";
}