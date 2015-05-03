package config;

import agent.AlmostRandomAgent;
import agent.IAgent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by grubino on 4/28/15.
 */
@Configuration
@ComponentScan(value = {"gui"})
public class AlmostRandomAgentConfiguration {

    @Bean
    public IAgent getAgent() {
        return new AlmostRandomAgent();
    }

}
