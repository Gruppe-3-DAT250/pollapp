package gruppe3.pollapp.Analytics;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import gruppe3.pollapp. controllers. AnalyticsController;

@Service
public class AnalyticsConsumer {

    @RabbitListener(queues = "analyticsQueue")
    public void receiveEvent(AnalyticsController.Event event) {
        System.out.println("Received analytics event from RabbitMQ: " + event);

        // Send to DB
    }
}
