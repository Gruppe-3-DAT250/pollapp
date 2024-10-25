package gruppe3.pollapp;

import gruppe3.pollapp.domain.Poll;
import gruppe3.pollapp.domain.User;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class PollManager implements DomainManager{
    private final HashMap<Integer, User> users = new HashMap<>();
    private final Map<Integer, Poll> polls = new HashMap<>();

    private Integer idCounter_user = 0;
    private Integer idCounter_poll = 0;


    public PollManager() {

    }

    @Override
    public User createUser(String username, String password, String email) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        users.put(idCounter_user, user);
        idCounter_user++;
        return user;
    }

    @Override
    public User getUser(Integer id) {
        return users.get(id);
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

    @Override
    public void addPoll(Integer id, Poll poll){
        polls.put(id, poll);

    }

}
