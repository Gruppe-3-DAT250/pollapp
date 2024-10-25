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
    private int idCounter = 0;

    public PollController(@Autowired PollManager manager) {
        this.manager = manager;
    }

    @GetMapping("/get_polls")
    public ResponseEntity<Collection<Poll>> getPolls() {
        Collection<Poll> pollsForFrontend = manager.getPolls();
        return ResponseEntity.ok(pollsForFrontend);
    }

    @PostMapping(value = "/create_poll", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Poll> createPoll(@RequestBody Poll poll) {
        poll.setId(idCounter);
        manager.addPoll(idCounter, poll);
        idCounter++;
        return ResponseEntity.ok(poll);
    }

}