package gruppe3.pollapp.domain;

import java.time.LocalDateTime;
import org.springframework.data.mongodb.core.mapping.Document;
import gruppe3.pollapp.controllers.AnalyticsController;
import org.springframework.data.annotation.Id;  

@Document(collection = "analytics")
public class Analytics {
    @Id 
    private String id;
    private String eventType;
    private Object details;  
    private LocalDateTime timestamp;
    
    public Analytics() {
    }
    
    public static Analytics fromEvent(AnalyticsController.Event event) {
        Analytics analytics = new Analytics();
        analytics.setEventType(event.getEventType());
        analytics.setDetails(event.getDetails());
        analytics.setTimestamp(event.getTime());
        return analytics;
    }
    
  
    public Object getDetails() {
        return details;
    }
    
    public void setDetails(Object details) {
        this.details = details;
    }
    
    public String getEventType() {
        return eventType;
    }
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}