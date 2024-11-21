package gruppe3.pollapp.repositories;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import gruppe3.pollapp.domain.Vote;
import gruppe3.pollapp.domain.VoteOption;
import gruppe3.pollapp.domain.User;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    Vote save(Vote vote);

    List<Vote> findByVoteOption(VoteOption voteOption);

    List<Vote> findByVoteOptionAndUser(VoteOption voteOption, User user);

    Boolean existsByVoteOptionAndUser(VoteOption voteOption, User user);

    void deleteByVoteOptionAndUser(VoteOption voteOption, User user);
}
