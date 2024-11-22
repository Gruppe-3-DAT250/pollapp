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

    User getUser(Long id);

    User getUser(String username);

    List<User> getAllUsers();

    void deleteUser(Long id);

    Collection<Poll> getPolls();

    Poll getPoll(String id);

    void addPoll(Poll poll);

    Vote makeVote(String username, String pollId, Integer optionId) throws Exception;

    boolean deleteVote(String username, String pollId, Integer optionId);

    Integer getUserVoteOption(String username, String pollId);

    Collection<VoteOption> getVoteOptionsByPollId(String pollId);

}
