package gruppe3.pollapp;


import gruppe3.pollapp.domain.User;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public interface DomainManager {

    public void createUser(String username, String password);
    public User getUser(Integer id);
    public List<User> getAllUsers();




}
