package gruppe3.pollapp.controllers;

import gruppe3.pollapp.PollManager;
import gruppe3.pollapp.domain.Poll;
import gruppe3.pollapp.domain.User;
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
    private final PollManager manager;

    public PollController(@Autowired PollManager manager) {
        this.manager = manager;
    }

    @GetMapping("/get_polls")
    public ResponseEntity<Collection<Poll>> getPolls() {
        Collection<Poll> pollsForFrontend = manager.getPolls();
        return ResponseEntity.ok(pollsForFrontend);
    }

    @PostMapping(value = "/create_poll", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Poll> createPoll(@RequestBody Poll poll, @RequestHeader("Authorization") String authHeader) {
        String username = manager.extractUsernameFromToken(authHeader);
        User user = manager.getUser(username);
        Long user_id = user.getId();
        poll.setCreator_id(user_id);
        manager.addPoll(poll);
        return ResponseEntity.ok(poll);
    }

}