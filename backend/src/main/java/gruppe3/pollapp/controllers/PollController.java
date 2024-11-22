package gruppe3.pollapp.controllers;

import gruppe3.pollapp.DomainManager;
import gruppe3.pollapp.domain.Poll;
import gruppe3.pollapp.domain.User;
import gruppe3.pollapp.login.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/api/polls")
@CrossOrigin
public class PollController {

    @Autowired
    private AuthenticationService authenticationService;

    private final DomainManager domainManager;

    public PollController(@Autowired DomainManager domainManager) {
        this.domainManager = domainManager;
    }

    @GetMapping("/get_polls")
    public ResponseEntity<Collection<Poll>> getPolls(@RequestHeader("Authorization") String authToken) {
        if (!authenticationService.validateToken(authToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Collection<Poll> pollsForFrontend = domainManager.getPolls();
        return ResponseEntity.ok(pollsForFrontend);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Poll> getPollById(@PathVariable String id, @RequestHeader("Authorization") String authToken) {
        System.out.println(id);
        if (!authenticationService.validateToken(authToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Poll poll = domainManager.getPoll(id);
        if (poll != null) {
            return ResponseEntity.ok(poll);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping(value = "/create_poll", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Poll> createPoll(@RequestBody Poll poll, @RequestHeader("Authorization") String authToken) {
        if (!authenticationService.validateToken(authToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = authenticationService.extractUsernameFromToken(authToken);
        User user = domainManager.getUser(username);
        Long user_id = user.getId();
        poll.setCreator_id(user_id);
        domainManager.addPoll(poll);
        return ResponseEntity.ok(poll);
    }

}
