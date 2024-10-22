package gruppe3.pollapp;

import gruppe3.pollapp.domain.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class PollManager implements DomainManager{
    HashMap<Integer, User> users = new HashMap<>();
    private Integer idCounter = 0;
    public PollManager() {

        this.createUser( "vetle", "1234");
    }

    @Override
    public void createUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        users.put(idCounter, user);
        idCounter++;
    }

    @Override
    public User getUser(Integer id) {
        return users.get(id);
    }

    @Override
    public List<User> getAllUsers() {
        return  new ArrayList<User>(users.values());
    }
}
