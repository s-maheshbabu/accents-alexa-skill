package com.amazon.ask.accents.utterances;

import java.io.FileReader;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazon.ask.accents.util.ObjectMapperFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.Validate;

/**
 * Maintains a repo of utterances by language.
 */
public class UtterancesRepo {
    private UtterancesRepo() {
        URL url = getClass().getResource(UTTERANCES_DATA);

        Map<String, List<String>> son;
        try {
            son = objectMapper.readValue(new FileReader(url.getPath()), HashMap.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed loading the mapping of languages to utterances. This is a fatal error.",
                    e);
        }

        utterancesMap = Collections.unmodifiableMap(son);
    }

    /**
     * Obtains a random set of utterances (in SSML) for the given language from the
     * utterances repo. If no specific utterances are found for the given language,
     * utterances will be returned from a default set.
     *
     * @param language The language for which utterances are needed. This field is
     *                 required.
     * @return a set of utterances for the given language. There will always be at
     *         least one utterances and atmost five utterances returned.
     */
    public List<String> getUtterances(String language) {
        Validate.notEmpty(language, "Language cannot be empty");

        if (!utterancesMap.containsKey(language))
            language = DEFAULT_LANGUAGE_KEY;
        List<String> allUtterances = utterancesMap.get(language);
        Collections.shuffle(allUtterances);

        return allUtterances.subList(0, Math.min(allUtterances.size(), MAX_NUMBER_UTTERANCES));
    }

    public static UtterancesRepo getInstance() {
        if (instance == null)
            instance = new UtterancesRepo();
        return instance;
    }

    public static final int MAX_NUMBER_UTTERANCES = 5;
    public static final String DEFAULT_LANGUAGE_KEY = "default";
    // Utterances by language. Any utterance would work for any language but the
    // data set is constructed to have some cultural significance. For ex,
    // utterances for en-IN might talk about Cricket.
    private static final String UTTERANCES_DATA = "/resources/data/utterances.json";
    private static UtterancesRepo instance;
    private final Map<String, List<String>> utterancesMap;
    private final ObjectMapper objectMapper = ObjectMapperFactory.getInstance();
}