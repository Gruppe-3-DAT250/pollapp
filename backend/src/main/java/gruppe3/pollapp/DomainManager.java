package gruppe3.pollapp;


import gruppe3.pollapp.domain.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DomainManager {

    User createUser(String username, String password);

    User getUser(Integer id);

    List<User> getAllUsers();

    void deleteUser(Integer id);

    User verifyUser(String username, String email);





}
