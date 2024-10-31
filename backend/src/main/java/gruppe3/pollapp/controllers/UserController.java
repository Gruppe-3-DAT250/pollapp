package gruppe3.pollapp.controllers;

import gruppe3.pollapp.DomainManager;
import gruppe3.pollapp.domain.User;
import gruppe3.pollapp.login.LoginRequest;
import gruppe3.pollapp.login.LoginResponse;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/v1/api/user")
public class UserController {

    private final DomainManager domainManager;

    public UserController(@Autowired DomainManager domainManager) {
        this.domainManager = domainManager;

    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(domainManager.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Integer id) {
        return ResponseEntity.ok(domainManager.getUser(id));
    }

    @PostMapping("/create_user")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        try {
            User createdUser = domainManager.createUser(user.getUsername(), user.getEmail(), user.getPassword());

            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id) {
        try {
            domainManager.deleteUser(id);
            return ResponseEntity.ok("User deleted");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting user");
        }
    }

    @PostMapping("/signIn")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        System.out.println("Login attempt for user: " + loginRequest.getUsername());
        User user = domainManager.getUser(loginRequest.getUsername());
        if(user != null && user.getPassword().equals(loginRequest.getPassword())){
            String token = generateToken(user);
            return ResponseEntity.ok(new LoginResponse(token));
        }
        else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid user");
        }
    }

    // TODO: helper function - should be removed/replaced before production
    private String generateToken(User user) {
        String tokenData = user.getUsername() + ":" + System.currentTimeMillis();
        return Base64.getEncoder().encodeToString(tokenData.getBytes());
    }


}
