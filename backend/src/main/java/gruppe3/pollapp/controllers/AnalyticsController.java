package gruppe3.pollapp.controllers;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;

@RestController
@RequestMapping("/v1/api/analytics")
@CrossOrigin
public class AnalyticsController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostMapping("/track")
    public ResponseEntity<String> trackEvents(@RequestBody Event event) {
        if (event.getTime() == null) {
            event.setTime(LocalDateTime.now());
        }

        rabbitTemplate.convertAndSend("analyticsQueue", event);
        System.out.println("Event published to RabbitMQ: " + event);

        return  ResponseEntity.ok("Event tracked successfully");
    }

    public static class Event {
        private String eventType;
        private Object details;
        private LocalDateTime time;


        public String getEventType() { return eventType; }
        public void setEventType(String eventType) { this.eventType = eventType; }

        public Object getDetails() { return details; }
        public void setDetails(Object details) { this.details = details; }

        public LocalDateTime getTime() { return time; }
        public void setTime(LocalDateTime timestamp) { this.time = timestamp; }

        @Override
        public String toString() {
            return "Event{" +
                    "eventType='" + eventType + '\'' +
                    ", details=" + details +
                    ", time=" + time +
                    '}';
        }

    }

}

