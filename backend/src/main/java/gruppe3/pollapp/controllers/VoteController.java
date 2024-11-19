package gruppe3.pollapp.controllers;

import gruppe3.pollapp.DomainManager;
import gruppe3.pollapp.domain.User;
import gruppe3.pollapp.domain.Vote;
import gruppe3.pollapp.login.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.security.Principal;
import java.util.Base64;

@RestController
@RequestMapping("/v1/api/vote")
@CrossOrigin
public class VoteController {

    @Autowired
    private AuthenticationService authenticationService;

    private final DomainManager domainManager;

    public VoteController(@Autowired DomainManager domainManager) {
        this.domainManager = domainManager;
    }

    @PostMapping("/{optionId}")
    public ResponseEntity<Vote> makeVote(@RequestParam Long pollId, @PathVariable Long optionId,
            @RequestHeader("Authorization") String authToken) throws Exception {
        if (!authenticationService.validateToken(authToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = authenticationService.extractUsernameFromToken(authToken);
        Vote vote = domainManager.makeVote(username, optionId);

        return ResponseEntity.ok(vote);
    }

    @DeleteMapping("/{optionId}")
    public ResponseEntity<String> deleteVote(@RequestParam Long pollId, @PathVariable Long optionId,
            @RequestHeader("Authorization") String authToken) {
        if (!authenticationService.validateToken(authToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = authenticationService.extractUsernameFromToken(authToken);
        boolean isDeleted = domainManager.deleteVote(username, optionId);
        if (isDeleted) {
            return ResponseEntity.ok("Vote deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vote not found");
        }
    }

    @GetMapping("/hasVoted")
    public ResponseEntity<Long> hasUserVoted(@RequestParam Long pollId,
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
