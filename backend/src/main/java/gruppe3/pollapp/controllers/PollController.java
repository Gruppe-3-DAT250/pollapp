package gruppe3.pollapp.controllers;

import gruppe3.pollapp.DomainManager;
import gruppe3.pollapp.domain.Poll;
import gruppe3.pollapp.domain.User;
import gruppe3.pollapp.domain.VoteOption;
import gruppe3.pollapp.domain.Vote;
import gruppe3.pollapp.login.AuthenticationService;
import gruppe3.pollapp.repositories.PollRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public ResponseEntity<Poll> getPollById(@PathVariable Long pollId,
            @RequestHeader("Authorization") String authToken) {
        if (!authenticationService.validateToken(authToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Poll poll = domainManager.getPoll(pollId);
        if (poll != null) {
            return ResponseEntity.ok(poll);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    private static class CreatePollDTO {
        private String question;
        private Instant publishedAt;
        private Instant validUntil;
        private List<VoteOptionDTO> options;

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public Instant getPublishedAt() {
            return publishedAt;
        }

        public void setPublishedAt(Instant publishedAt) {
            this.publishedAt = publishedAt;
        }

        public Instant getValidUntil() {
            return validUntil;
        }

        public void setValidUntil(Instant validUntil) {
            this.validUntil = validUntil;
        }

        public List<VoteOptionDTO> getOptions() {
            return options;
        }

        public void setOptions(List<VoteOptionDTO> options) {
            this.options = options;
        }
    }

    private static class VoteOptionDTO {
        private String caption;
        private Integer presentationOrder;

        public String getCaption() {
            return caption;
        }

        public void setCaption(String caption) {
            this.caption = caption;
        }

        public Integer getPresentationOrder() {
            return presentationOrder;
        }

        public void setPresentationOrder(Integer presentationOrder) {
            this.presentationOrder = presentationOrder;
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Poll> createPoll(@RequestBody CreatePollDTO request,
            @RequestHeader("Authorization") String authToken) {
        if (!authenticationService.validateToken(authToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = authenticationService.extractUsernameFromToken(authToken);
        User user = domainManager.getUser(username);

        Poll poll = new Poll();
        poll.setOwner(user);
        poll.setQuestion(request.getQuestion());
        poll.setPublishedAt(request.getPublishedAt());
        poll.setValidUntil(request.getValidUntil());

        Collection<VoteOption> options = new ArrayList<>();
        for (VoteOptionDTO optionDTO : request.getOptions()) {
            VoteOption option = new VoteOption();
            option.setPoll(poll);
            option.setCaption(optionDTO.getCaption());
            option.setPresentationOrder(optionDTO.getPresentationOrder());
            options.add(option);
        }

        domainManager.addPoll(poll, options);
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

    @GetMapping("/{pollId}/vote-counts")
    public ResponseEntity<Map<String, Long>> getVoteCountsForPoll(@PathVariable Long pollId) {
        Map<VoteOption, Long> voteCounts = domainManager.countVotesForVoteOptions(pollId);

        Map<String, Long> result = voteCounts.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().getCaption(),
                        Map.Entry::getValue
                ));

        return ResponseEntity.ok(result);
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

    @PostMapping("/{pollId}/options/{optionId}")
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
    public ResponseEntity<Collection<Vote>> getVotes(@PathVariable Long pollId,
            @RequestHeader("Authorization") String authToken) {

        Collection<Vote> votes = domainManager.getVotes(pollId);
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




}