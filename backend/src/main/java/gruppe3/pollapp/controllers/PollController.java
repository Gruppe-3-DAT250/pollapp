package gruppe3.pollapp.controllers;

import gruppe3.pollapp.DomainManager;
import gruppe3.pollapp.domain.Poll;
import gruppe3.pollapp.domain.User;
import gruppe3.pollapp.domain.VoteOption;
import gruppe3.pollapp.domain.Vote;
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
@RequestMapping("/api/v1/polls")
@CrossOrigin
public class PollController {

    @Autowired
    private AuthenticationService authenticationService;

    private final DomainManager domainManager;

    public PollController(@Autowired DomainManager domainManager) {
        this.domainManager = domainManager;
    }

    @GetMapping
    public ResponseEntity<Collection<Poll>> getPolls(@RequestHeader("Authorization") String authToken) {
        if (!authenticationService.validateToken(authToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Collection<Poll> pollsForFrontend = domainManager.getPolls();
        return ResponseEntity.ok(pollsForFrontend);
    }

    @GetMapping("/{pollId}")
    public ResponseEntity<Poll> getPollById(@PathVariable Long id, @RequestHeader("Authorization") String authToken) {
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

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Poll> createPoll(@RequestBody Poll poll, @RequestHeader("Authorization") String authToken) {
        if (!authenticationService.validateToken(authToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = authenticationService.extractUsernameFromToken(authToken);
        User user = domainManager.getUser(username);
        poll.setOwner(user);
        domainManager.addPoll(poll);
        return ResponseEntity.ok(poll);
    }

    @GetMapping("/{pollId}/options")
    public ResponseEntity<Collection<VoteOption>> getVoteOptions(@PathVariable Long pollId,
            @RequestHeader("Authorization") String authToken) {
        if (!authenticationService.validateToken(authToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Collection<VoteOption> options = domainManager.getVoteOptionsByPollId(pollId);
        return ResponseEntity.ok(options);
    }

    @DeleteMapping("/{pollId}")
    public ResponseEntity<String> deletePoll(@PathVariable Long pollId,
            @RequestHeader("Authorization") String authToken) {
        if (!authenticationService.validateToken(authToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        domainManager.deleteVoteOptions(domainManager.getPoll(pollId));
        domainManager.deletePoll(pollId);
        return ResponseEntity.ok("Poll deleted");
    }

    @PostMapping("/{pollId}/{optionId}")
    public ResponseEntity<Vote> createVote(@PathVariable Long pollId, @PathVariable Long optionId,
            @RequestHeader("Authorization") String authToken) throws Exception {
        if (!authenticationService.validateToken(authToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = authenticationService.extractUsernameFromToken(authToken);
        Vote vote = domainManager.makeVote(username, optionId);

        return ResponseEntity.ok(vote);
    }

    @DeleteMapping("/{pollId}/votes/")
    public ResponseEntity<String> deleteVote(@PathVariable Long pollId,
            @RequestHeader("Authorization") String authToken) {
        if (!authenticationService.validateToken(authToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = authenticationService.extractUsernameFromToken(authToken);
        boolean isDeleted = domainManager.deleteVote(username, pollId);
        if (isDeleted) {
            return ResponseEntity.ok("Vote deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vote not found");
        }
    }

    @GetMapping("{pollId}/votes")
    public ResponseEntity<Long> getVotes(@PathVariable Long pollId,
            @RequestHeader("Authorization") String authToken) {

        return ResponseEntity.noContent().build();
    }

    @GetMapping("{pollId}/votes/self")
    public ResponseEntity<Long> hasUserVoted(@PathVariable Long pollId,
            @RequestHeader("Authorization") String authToken) {

        if (!authenticationService.validateToken(authToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = authenticationService.extractUsernameFromToken(authToken);

        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long voteOptionId = domainManager.getUserVoteOptionId(username, pollId);
        if (voteOptionId == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(voteOptionId);
    }

}
