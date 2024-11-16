package gruppe3.pollapp.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import gruppe3.pollapp.domain.Poll;

public interface PollRepository extends MongoRepository<Poll, String> {

    Poll save(Poll poll);
}
