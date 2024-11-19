package gruppe3.pollapp;

import gruppe3.pollapp.domain.Poll;
import gruppe3.pollapp.domain.User;
import gruppe3.pollapp.domain.Vote;
import gruppe3.pollapp.domain.VoteOption;
import gruppe3.pollapp.repositories.UserRepository;
import gruppe3.pollapp.repositories.VoteOptionRepository;
import gruppe3.pollapp.repositories.VoteRepository;
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

    @Autowired
    private VoteOptionRepository voteOptionRepository;

    @Autowired
    private VoteRepository voteRepository;

    public PersistentPollManager() {
    }

    @PostConstruct
    private void initializeMockData() {
        createUser("user1", "password", "user@example.com");

        Poll poll1 = new Poll();
        poll1.setQuestion("What's your favorite programming language?");
        poll1.setPublishedAt(Instant.now());
        poll1.setValidUntil(Instant.now().plusSeconds(86400));

        addPoll(poll1);

        VoteOption option1 = new VoteOption();
        option1.setCaption("Java");
        option1.setPresentationOrder(1);
        option1.setPoll(poll1);

        VoteOption option2 = new VoteOption();
        option2.setCaption("Python");
        option2.setPresentationOrder(2);
        option2.setPoll(poll1);

        ArrayList<VoteOption> options = new ArrayList<>();
        options.add(option1);
        options.add(option2);

        addVoteOptions(options);

        Poll poll2 = new Poll();
        poll2.setQuestion("What's your favourite food?");
        poll2.setPublishedAt(Instant.now());
        poll2.setValidUntil(Instant.now().minusSeconds(5));
        addPoll(poll2);

        VoteOption option1_poll2 = new VoteOption();
        option1_poll2.setCaption("Pizza");
        option1_poll2.setPresentationOrder(1);
        option1_poll2.setPoll(poll2);

        VoteOption option2_poll2 = new VoteOption();
        option2_poll2.setCaption("Salmon with ketchup and bearnaise sauce");
        option2_poll2.setPresentationOrder(2);
        option2_poll2.setPoll(poll2);

        ArrayList<VoteOption> options_poll2 = new ArrayList<>();
        options_poll2.add(option1_poll2);
        options_poll2.add(option2_poll2);

        addVoteOptions(options_poll2);

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
    public Poll getPoll(Long id) {
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
    public void addVoteOptions(Collection<VoteOption> options) {
        voteOptionRepository.saveAll(options);
    }

    @Override
    @Transactional
    public Vote makeVote(String username, Long optionId) {
        User user = getUser(username);
        Optional<VoteOption> maybeOption = voteOptionRepository.findById(optionId);

        VoteOption option;

        if (maybeOption.isPresent()) {
            option = maybeOption.get();
        } else {
            throw new EntityNotFoundException("Poll with id " + optionId + " does not exist.");
        }

        Vote vote = new Vote();
        vote.setUser(user);
        vote.setVoteOption(option);

        voteRepository.save(vote);

        return vote;
    }

    @Override
    public boolean deleteVote(String username, Long optionId) {
        User user = getUser(username);
        Optional<VoteOption> maybeOption = voteOptionRepository.findById(optionId);

        VoteOption option;

        if (maybeOption.isPresent()) {
            option = maybeOption.get();
        } else {
            throw new EntityNotFoundException("Poll with id " + optionId + " does not exist.");
        }

        List<Vote> votes = voteRepository.findByVoteOptionAndUser(option, user);

        voteRepository.deleteAllInBatch(votes);

        return true;
    }

    @Override
    public Long getUserVoteOptionId(String username, Long pollId) {
        VoteOption option = getUserVoteOption(username, pollId);

        if (option == null) {
            return null;
        }

        return option.getId();
    }

    @Override
    public VoteOption getUserVoteOption(String username, Long pollId) {
        User user = getUser(username);
        Poll poll = getPoll(pollId);

        List<VoteOption> options = voteOptionRepository.findByPoll(poll);

        if (options == null) {
            return null;
        }

        for (VoteOption option : options) {
            if (option == null) {
                return null;
            }
            if (voteRepository.existsByVoteOptionAndUser(option, user)) {
                return option;
            }
        }

        return null;
    }

    @Override
    public Collection<VoteOption> getVoteOptionsByPollId(Long pollId) {
        Poll poll = getPoll(pollId);
        return voteOptionRepository.findByPoll(poll);
    }

}
