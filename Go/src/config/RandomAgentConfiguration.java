package config;

import agent.IAgent;
import agent.RandomAgent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by greg on 4/25/15.
 */
@Configuration
@ComponentScan(value = {"gui"})
public class RandomAgentConfiguration {

    @Bean
    public IAgent getAgent() {
        return new RandomAgent();
    }

}
