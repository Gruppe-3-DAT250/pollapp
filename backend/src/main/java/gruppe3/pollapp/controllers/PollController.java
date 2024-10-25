package gruppe3.pollapp.controllers;

import gruppe3.pollapp.PollManager;
import gruppe3.pollapp.domain.Poll;
import gruppe3.pollapp.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<Poll>> getPolls() {
        List<Poll> pollsForFrontend = manager.getPolls();
        return ResponseEntity.ok(pollsForFrontend);
    }


    @PostMapping(value = "/create_poll", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Poll> createPoll(@RequestBody Poll poll) {
        System.out.println("Received poll: " + poll);

        manager.addPoll(1, poll);
        return ResponseEntity.ok(poll);
    }

}