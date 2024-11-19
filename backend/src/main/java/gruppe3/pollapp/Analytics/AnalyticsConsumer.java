package gruppe3.pollapp.Analytics;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import gruppe3.pollapp.controllers.AnalyticsController;
import gruppe3.pollapp.domain.Analytics;
import gruppe3.pollapp.repositories.AnalyticsRepository;

@Service
public class AnalyticsConsumer {
    
    @Autowired
    private AnalyticsRepository analyticsRepository;
    
    @RabbitListener(queues = "analyticsQueue")
    public void receiveEvent(AnalyticsController.Event event) {
        System.out.println("Received analytics event from RabbitMQ: " + event);
        
        Analytics analytics = Analytics.fromEvent(event);
        analyticsRepository.save(analytics);
        
        System.out.println("Saved analytics event to MongoDB: " + analytics);
    }
}
