package gruppe3.pollapp;


import gruppe3.pollapp.domain.Poll;
import gruppe3.pollapp.domain.User;
import gruppe3.pollapp.domain.Vote;
import gruppe3.pollapp.domain.VoteOption;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public interface DomainManager {

    User createUser(String username, String password, String email);

    User getUser(Integer id);

    User getUser(String username);

    List<User> getAllUsers();

    void deleteUser(Integer id);

    String extractUsernameFromToken(String authHeader);

    Collection<Poll> getPolls();

    Poll getPoll(Long id);

    void addPoll(Poll poll);

    Vote makeVote(String username, Long pollId, Integer optionId) throws Exception;

    boolean deleteVote(String username, Long pollId, Integer optionId);

    Integer getUserVoteOption(String username, Long pollId);

    Collection<VoteOption> getVoteOptionsByPollId(Long pollId);

}
