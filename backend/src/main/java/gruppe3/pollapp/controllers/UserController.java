package gruppe3.pollapp.controllers;

import gruppe3.pollapp.DomainManager;
import gruppe3.pollapp.domain.User;
import gruppe3.pollapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/v1/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // public UserController(@Autowired DomainManager domainManager) {
    //     this.domainManager = domainManager;
    // }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        User user = new User();
        user.setUsername("Vetle");
        user.setPassword("Password");
        userRepository.save(user);

        return ResponseEntity.ok(userRepository.findAll());
    }

    // @GetMapping("/{id}")
    // public ResponseEntity<User> getUser(Integer id) {
    //     return ResponseEntity.ok(domainManager.getUser(id));
    // }

    // NO PERSISTENCE

    // private final DomainManager domainManager;
    //
    // public UserController(@Autowired DomainManager domainManager) {
    //     this.domainManager = domainManager;
    //
    // }
    //
    // @GetMapping
    // public ResponseEntity<List<User>> getAllUsers() {
    //     return ResponseEntity.ok(domainManager.getAllUsers());
    // }
    //
    // @GetMapping("/{id}")
    // public ResponseEntity<User> getUser(Integer id) {
    //     return ResponseEntity.ok(domainManager.getUser(id));
    // }




}
