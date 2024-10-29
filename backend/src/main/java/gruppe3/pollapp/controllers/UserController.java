package gruppe3.pollapp.controllers;

import gruppe3.pollapp.DomainManager;
import gruppe3.pollapp.domain.User;
import gruppe3.pollapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin // FIXME: REMOVE
@RestController
@RequestMapping("/v1/api/users")
public class UserController {

    private final DomainManager domainManager;

    @Autowired
    private UserRepository userRepository;

    public UserController(@Autowired DomainManager domainManager) {
        this.domainManager = domainManager;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        User user = new User();
        user.setUsername("Vetle");
        user.setPassword("Password");
        userRepository.save(user);

        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(domainManager.getUser(id));
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

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        try {
            User createdUser = domainManager.createUser(user.getUsername(), user.getEmail(), user.getPassword());

            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        try {
            domainManager.deleteUser(id);
            return ResponseEntity.ok("User deleted");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting user");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestParam String username, @RequestParam String email) {
        try {
            User user = domainManager.verifyUser(username, email);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }




}
