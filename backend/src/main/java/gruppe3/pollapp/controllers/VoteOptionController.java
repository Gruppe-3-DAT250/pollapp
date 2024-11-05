package gruppe3.pollapp.controllers;

import gruppe3.pollapp.DomainManager;
import gruppe3.pollapp.domain.VoteOption;
import gruppe3.pollapp.login.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/v1/api/voteOption")
@CrossOrigin
public class VoteOptionController {

    private final DomainManager domainManager;

    @Autowired
    private AuthenticationService authenticationService;

    public VoteOptionController(@Autowired DomainManager domainManager){
        this.domainManager = domainManager;
    }


    @GetMapping("/{pollId}/options")
    public ResponseEntity<Collection<VoteOption>> getVoteOptions(@PathVariable Long pollId, @RequestHeader("Authorization") String authToken) {
        if (!authenticationService.validateToken(authToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Collection<VoteOption> options = domainManager.getVoteOptionsByPollId(pollId);
        return ResponseEntity.ok(options);
    }

}
