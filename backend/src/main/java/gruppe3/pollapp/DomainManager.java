package gruppe3.pollapp;

import gruppe3.pollapp.domain.Poll;
import gruppe3.pollapp.domain.User;
import gruppe3.pollapp.domain.Vote;
import gruppe3.pollapp.domain.VoteOption;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Component
public interface DomainManager {

    User createUser(String username, String password, String email);

    User getUser(Long id);

    User getUser(String username);

    List<User> getAllUsers();

    void deleteUser(Long id);

    Collection<Poll> getPolls();

    Poll getPoll(Long id);

    void addPoll(Poll poll, Collection<VoteOption> options);

    void addVoteOptions(Collection<VoteOption> options);

    Vote makeVote(String username, Long optionId) throws Exception;

    Collection<Vote> getVotes(Long pollId);

    void deletePoll(Long id);

    void deleteVoteOptions(Poll poll);

    boolean deleteVote(String username, Long pollId);

    Long getUserVoteOptionId(String username, Long pollId);

    VoteOption getUserVoteOption(String username, Long pollId);

    Collection<VoteOption> getVoteOptionsByPollId(Long pollId);

    Map<VoteOption, Long> countVotesForVoteOptions(Long pollId);

}
