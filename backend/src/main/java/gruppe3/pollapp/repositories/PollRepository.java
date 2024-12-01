package gruppe3.pollapp.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import gruppe3.pollapp.domain.Poll;

public interface PollRepository extends JpaRepository<Poll, Long> {

    Poll save(Poll poll);

    void deleteById(Long id);
}
