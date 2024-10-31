package gruppe3.pollapp;


import gruppe3.pollapp.domain.Poll;
import gruppe3.pollapp.domain.User;
import gruppe3.pollapp.domain.Vote;
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

    void addPoll(Poll poll);

    Vote makeVote(String username, Integer pollId, Integer optionId) throws Exception;

    boolean deleteVote(String username, Integer pollId, Integer optionId);

    Integer getUserVoteOption(String username, Integer pollId);

}
