package gruppe3.pollapp.controllers;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import gruppe3.pollapp.domain.Analytics;
import gruppe3.pollapp.domain.EventCount;
import gruppe3.pollapp.repositories.AnalyticsRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/analytics")
@CrossOrigin
public class AnalyticsController {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    @Autowired
    private AnalyticsRepository analyticsRepository;

    @PostMapping("/track")
    public ResponseEntity<String> trackEvents(@RequestBody Event event) {
        if (event.getTime() == null) {
            event.setTime(LocalDateTime.now());
        }

        rabbitTemplate.convertAndSend("analyticsQueue", event);
        System.out.println("Event published to RabbitMQ: " + event);

        return  ResponseEntity.ok("Event tracked successfully");
    }

    @GetMapping("/events")
    public ResponseEntity<List<Analytics>> getAllEvents() {
        return ResponseEntity.ok(analyticsRepository.findAll());
    }
    
    @GetMapping("/events/{eventType}")
    public ResponseEntity<List<Analytics>> getEventsByType(@PathVariable String eventType) {
        return ResponseEntity.ok(analyticsRepository.findByEventType(eventType));
    }

    @GetMapping("/daily-counts")
    public ResponseEntity<List<EventCount>> getDailyCounts(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(analyticsRepository.getEventCountsByDay(start, end));
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

