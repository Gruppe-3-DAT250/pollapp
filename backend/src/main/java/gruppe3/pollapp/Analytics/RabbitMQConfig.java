package gruppe3.pollapp.Analytics;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue analyticsQueue() {
        return new Queue("analyticsQueue", true);
    }
}
