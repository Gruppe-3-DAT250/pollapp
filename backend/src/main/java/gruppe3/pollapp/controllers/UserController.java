package gruppe3.pollapp.controllers;

import gruppe3.pollapp.DomainManager;
import gruppe3.pollapp.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
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
    public ResponseEntity<User> getUser(Integer id) {
        return ResponseEntity.ok(domainManager.getUser(id));
    }




}
