package gruppe3.pollapp.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import gruppe3.pollapp.domain.VoteOption;
import gruppe3.pollapp.domain.User;
import gruppe3.pollapp.domain.Vote;
import gruppe3.pollapp.domain.Poll;

public interface VoteOptionRepository extends JpaRepository<VoteOption, Long> {

    VoteOption save(VoteOption voteOption);

    List<VoteOption> findByPoll(Poll poll);

    void deleteByPoll(Poll poll);
}
