package gruppe3.pollapp;

import gruppe3.pollapp.domain.Poll;
import gruppe3.pollapp.domain.User;
import gruppe3.pollapp.domain.Vote;
import gruppe3.pollapp.domain.VoteOption;

import org.springframework.stereotype.Repository;
import org.springframework.context.annotation.Primary;

import java.time.Instant;
import java.util.*;

@Repository
// @Primary
public class MockPollManager implements DomainManager {
    private final HashMap<Long, User> users = new HashMap<>();
    private final Map<String, Poll> polls = new HashMap<>();

    private Long idCounter_user = 0L;
    private Long idCounter_poll = 0L;

    public MockPollManager() {
        initializeMockData();
    }

    private void initializeMockData() {
        createUser("user1", "password", "user@example.com");

        Poll poll1 = new Poll();
        poll1.setQuestion("What's your favorite programming language?");
        poll1.setPublishedAt(Instant.now());
        poll1.setValidUntil(Instant.now().plusSeconds(86400));

        VoteOption option1 = new VoteOption();
        option1.setId(0);
        option1.setCaption("Java");
        option1.setPresentationOrder(1);
        option1.setVotes(new ArrayList<>());

        VoteOption option2 = new VoteOption();
        option2.setId(1);
        option2.setCaption("Python");
        option2.setPresentationOrder(2);
        option2.setVotes(new ArrayList<>());

        Map<Integer, VoteOption> options = new HashMap<>();
        options.put(0, option1);
        options.put(1, option2);

        poll1.setOptions(options);

        addPoll(poll1);

        Poll poll2 = new Poll();
        poll2.setQuestion("What's your favourite food?");
        poll2.setPublishedAt(Instant.now());
        poll2.setValidUntil(Instant.now().minusSeconds(5));

        VoteOption option1_poll2 = new VoteOption();
        option1_poll2.setId(0);
        option1_poll2.setCaption("Pizza");
        option1_poll2.setPresentationOrder(1);
        option1_poll2.setVotes(new ArrayList<>());

        VoteOption option2_poll2 = new VoteOption();
        option2_poll2.setId(1);
        option2_poll2.setCaption("Salmon with ketchup and bearnaise sauce");
        option2_poll2.setPresentationOrder(2);

        List<Vote> votesToAdd = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            votesToAdd.add(new Vote());
        }
        option2_poll2.setVotes(votesToAdd);

        Map<Integer, VoteOption> options_poll2 = new HashMap<>();
        options_poll2.put(0, option1_poll2);
        options_poll2.put(1, option2_poll2);

        poll2.setOptions(options_poll2);

        addPoll(poll2);
    }

    @Override
    public User createUser(String username, String password, String email) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setId(idCounter_user);
        users.put(idCounter_user, user);
        idCounter_user++;
        return user;
    }

    @Override
    public User getUser(Long id) {
        return users.get(id);
    }

    @Override
    public User getUser(String username) {
        for (User user : users.values()) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<User>(users.values());
    }

    @Override
    public void deleteUser(Long id) {
        users.remove(id);
    }

    @Override
    public Collection<Poll> getPolls() {
        return polls.values();
    }

    @Override
    public Poll getPoll(String id) {
        return polls.get(id);
    }

    @Override
    public void addPoll(Poll poll) {
        // String id = "aaaaaaaaaaaaaaaaaaaaaaa" + idCounter_poll.toString();
        String id = idCounter_poll.toString();
        polls.put(id, poll);
        poll.setId(id);
        idCounter_poll++;
    }

    @Override
    public Vote makeVote(String username, String pollId, Integer optionId) {
        User user = getUser(username);
        Poll poll = polls.get(pollId);
        if (user == null || poll == null) {
            return null;
        }

        VoteOption option = poll.getOptions().get(optionId);
        if (option == null) {
            return null;
        }

        Vote vote = new Vote();
        vote.setVoteOption(option);
        vote.setUser(user);
        option.addVote(vote);
        return vote;
    }

    @Override
    public boolean deleteVote(String username, String pollId, Integer optionId) {
        User user = getUser(username);
        Poll poll = polls.get(pollId);
        if (user == null || poll == null) {
            return false;
        }

        VoteOption option = poll.getOptions().get(optionId);
        if (option == null) {
            return false;
        }

        Vote userVote = option.getVotes().stream()
                .filter(vote -> vote.getUser().getUsername().equals(username))
                .findFirst()
                .orElse(null);

        if (userVote == null) {
            return false;
        }

        option.removeVote(userVote);

        return true;
    }

    @Override
    public Integer getUserVoteOption(String username, String pollId) {
        Poll poll = polls.get(pollId);

        for (VoteOption option : poll.getOptions().values()) {
            for (Vote vote : option.getVotes()) {
                if (vote.getUser().getUsername().equals(username)) {
                    return vote.getVoteOption().getId();
                }
            }
        }

        return null;
    }

    @Override
    public Collection<VoteOption> getVoteOptionsByPollId(String pollId) {
        Poll poll = polls.get(pollId);
        return poll.getOptions().values();
    }

}
