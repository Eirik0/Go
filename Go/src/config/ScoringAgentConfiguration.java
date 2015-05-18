package config;

import java.util.Arrays;

import agent.IAgent;
import agent.DivideGroupsScoring;
import agent.LightPlayScoring;
import agent.ScoringAgent;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(value = {"gui"})
public class ScoringAgentConfiguration {

    @Bean
    public IAgent getAgent() {
        return new ScoringAgent(Arrays.asList(new DivideGroupsScoring(), new LightPlayScoring()));
    }

}
