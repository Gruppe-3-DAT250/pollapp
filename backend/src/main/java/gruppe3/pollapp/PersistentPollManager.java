package gruppe3.pollapp;

import gruppe3.pollapp.domain.Poll;
import gruppe3.pollapp.domain.User;
import gruppe3.pollapp.domain.Vote;
import gruppe3.pollapp.domain.VoteOption;
import gruppe3.pollapp.repositories.UserRepository;
import gruppe3.pollapp.repositories.PollRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Repository
@Primary
public class PersistentPollManager implements DomainManager {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PollRepository pollRepository;

    public PersistentPollManager() {
    }

    @PostConstruct
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
        User savedUser = userRepository.save(user);
        return savedUser;
    }

    @Override
    public User getUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new EntityNotFoundException("User with id " + id + " does not exist.");
        }
    }

    @Override
    public User getUser(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Collection<Poll> getPolls() {
        return pollRepository.findAll();
    }

    @Override
    public Poll getPoll(String id) {
        Optional<Poll> poll = pollRepository.findById(id);
        if (poll.isPresent()) {
            return poll.get();
        } else {
            throw new EntityNotFoundException("Poll with id " + id + " does not exist.");
        }
    }

    @Override
    public void addPoll(Poll poll) {
        pollRepository.save(poll);
    }

    @Override
    @Transactional
    public Vote makeVote(String username, String pollId, Integer optionId) {
        User user = getUser(username);
        Poll poll = getPoll(pollId);
        if (user == null || poll == null) {
            return null;
        }

        VoteOption option = poll.getOptions().get(optionId);
        if (option == null) {
            return null;
        }

        Vote vote = poll.addVote(user, optionId);

        // pollRepository.save(poll); // FIXME: Her går det gale

        return vote;
    }

    @Override
    public boolean deleteVote(String username, String pollId, Integer optionId) {
        User user = getUser(username);
        Poll poll = getPoll(pollId);
        System.out.println("username " + username);
        System.out.println("pollId " + pollId);
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
        Poll poll = getPoll(pollId);

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
        Poll poll = getPoll(pollId);
        return poll.getOptions().values();
    }

}
