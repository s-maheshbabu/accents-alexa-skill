package com.amazon.ask.accents;

import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;
import com.amazon.ask.accents.intenthandlers.CancelIntentHandler;
import com.amazon.ask.accents.intenthandlers.HelpIntentHandler;
import com.amazon.ask.accents.intenthandlers.StopIntentHandler;
import com.amazon.ask.accents.intenthandlers.TalkLikeSomeoneIntentHandler;
import com.amazon.ask.accents.requesthandlers.LaunchRequestHandler;

public class AccentsStreamHandler extends SkillStreamHandler {
    public AccentsStreamHandler() {
        super(getSkill());
    }

    private static Skill getSkill() {
        return Skills
                .custom().addRequestHandlers(new StopIntentHandler(), new CancelIntentHandler(),
                        new HelpIntentHandler(), new LaunchRequestHandler(), new TalkLikeSomeoneIntentHandler())
                .withSkillId(SKILl_ID).build();
    }

    private static final String SKILl_ID = "amzn1.ask.skill.75abfdf4-a8c0-445a-8413-9a08ae0d8fbc";
}
