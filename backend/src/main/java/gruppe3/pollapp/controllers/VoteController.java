package gruppe3.pollapp.controllers;

import gruppe3.pollapp.DomainManager;
import gruppe3.pollapp.domain.User;
import gruppe3.pollapp.domain.Vote;
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

    private final DomainManager domainManager;

    public VoteController(@Autowired DomainManager domainManager){
        this.domainManager = domainManager;
    }

    @PostMapping("/{optionId}")
    public ResponseEntity<Vote> makeVote(@RequestParam String username, @RequestParam Integer pollId, @PathVariable Integer optionId) throws Exception {
        Vote vote = domainManager.makeVote(username,pollId,optionId);
        return ResponseEntity.ok(vote);
    }


    @DeleteMapping("/{optionId}")
    public ResponseEntity<String> deleteVote(@RequestParam String username, @RequestParam Integer pollId, @PathVariable Integer optionId) {
        boolean isDeleted = domainManager.deleteVote(username, pollId, optionId);
        if (isDeleted) {
            return ResponseEntity.ok("Vote deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vote not found");
        }
    }

    @GetMapping("/hasVoted")
    public ResponseEntity<Integer> hasUserVoted(@RequestParam Integer pollId, @RequestHeader("Authorization") String authHeader) {
        String username = extractUsernameFromToken(authHeader);

        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Integer voteOptionId = domainManager.getUserVoteOption(username, pollId);

        if (voteOptionId == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(voteOptionId);
    }

    // TODO: Replace/Remove with Sprint Security
    private String extractUsernameFromToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String decoded = new String(Base64.getDecoder().decode(token));
            String[] parts = decoded.split(":");
            if (parts.length > 0) {
                return parts[0];
            }
        }
        return null;
    }


}
