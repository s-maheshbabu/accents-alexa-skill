package com.amazon.ask.accents;

import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;
import com.amazon.ask.accents.handlers.CancelIntentHandler;
import com.amazon.ask.accents.handlers.FallbackIntentHandler;
import com.amazon.ask.accents.handlers.HelpIntentHandler;
import com.amazon.ask.accents.handlers.StopIntentHandler;
import com.amazon.ask.accents.handlers.TalkLikeSomeoneIntentHandler;

public class AccentsStreamHandler extends SkillStreamHandler {

    private static Skill getSkill() {
        return Skills.custom()
                .addRequestHandlers(new StopIntentHandler(), new CancelIntentHandler(), new HelpIntentHandler(),
                        new TalkLikeSomeoneIntentHandler(), new FallbackIntentHandler())
                .withSkillId("amzn1.ask.skill.75abfdf4-a8c0-445a-8413-9a08ae0d8fbc").build();
    }

    public AccentsStreamHandler() {
        super(getSkill());
    }

}
