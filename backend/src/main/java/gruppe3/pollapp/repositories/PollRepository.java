package gruppe3.pollapp.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import gruppe3.pollapp.domain.Poll;

public interface PollRepository extends MongoRepository<Poll, String> {

    // Sjekk ut:
    // https://docs.spring.io/spring-data/jpa/reference/jpa/getting-started.html

    // Disse metodenavnene er spesifikke, og vil automatisk gjøres om
    // til queries.
    // Les mulige metodenavn her:
    // https://docs.spring.io/spring-data/jpa/reference/repositories/query-keywords-reference.html
    Poll save(Poll poll);
}
