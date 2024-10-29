package gruppe3.pollapp;

import gruppe3.pollapp.domain.Poll;
import gruppe3.pollapp.domain.User;
import gruppe3.pollapp.domain.Vote;
import gruppe3.pollapp.domain.VoteOption;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.*;

@Repository
public class PollManager implements DomainManager{
    private final HashMap<Integer, User> users = new HashMap<>();
    private final Map<Integer, Poll> polls = new HashMap<>();

    private Integer idCounter_user = 0;
    private Integer idCounter_poll = 0;

    public PollManager() {
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
        options.put(0,option1);
        options.put(1,option2);

        poll1.setOptions(options);

        addPoll(poll1);
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
    public User getUser(Integer id) {
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
        return  new ArrayList<User>(users.values());
    }

    @Override
    public void deleteUser(Integer id) {
        users.remove(id);
    }

    @Override
    public User verifyUser(String username, String email){
        for (User user : users.values()) {
            if (user.getEmail().equals(email) && user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public Collection<Poll> getPolls(){
        return polls.values();
    }

    public Poll getPoll(Integer id) {
        return polls.get(id);
    }

    @Override
    public void addPoll(Poll poll){
        polls.put(idCounter_poll, poll);
        poll.setId(idCounter_poll);
        idCounter_poll++;
    }

    @Override
    public Vote makeVote(String username, Integer pollId, Integer optionId){
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
    public boolean deleteVote(String username, Integer pollId, Integer optionId) {
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
    public Integer getUserVoteOption(String username, Integer pollId) {
        Poll poll = polls.get(pollId);

        for (VoteOption option : poll.getOptions().values()) {
            for (Vote vote : option.getVotes()) {
                if (vote.getUser().getUsername().equals(username)) {
                    return option.getId();
                }
            }
        }

        return null;
    }


}
