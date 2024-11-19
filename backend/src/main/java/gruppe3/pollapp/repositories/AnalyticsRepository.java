package gruppe3.pollapp.repositories;

import gruppe3.pollapp.domain.Analytics;
import gruppe3.pollapp.domain.EventCount;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Aggregation;
import java.time.LocalDateTime;
import java.util.List;

public interface AnalyticsRepository extends MongoRepository<Analytics, String> {
    List<Analytics> findByEventType(String eventType);
    List<Analytics> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
    
    @Aggregation(pipeline = {
        "{ $match: { timestamp: { $gte: ?0, $lte: ?1 } } }",
        "{ $group: { _id: { $dateToString: { format: '%Y-%m-%d', date: '$timestamp' } }, count: { $sum: 1 } } }",
        "{ $sort: { '_id': 1 } }"
    })
    List<EventCount> getEventCountsByDay(LocalDateTime start, LocalDateTime end);
}