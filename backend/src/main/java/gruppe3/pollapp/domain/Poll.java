package gruppe3.pollapp.domain;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Document
public class Poll {

    @Id
    private String id;
    private String question;
    private Instant publishedAt;
    private Instant validUntil;

    private Long creator_id;
    private Map<Integer, VoteOption> options;

    public Poll(){

    }
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

    public Map<Integer, VoteOption> getOptions() {
        return options;
    }

    public VoteOption getOption(Integer optionId){
        return this.getOptions().get(optionId);
    }

    public void setOptions(Map<Integer, VoteOption> options) {
        this.options = options;
    }

    public void setOptionsList(List<VoteOption> optionsList) {
        this.options = new HashMap<>();
        for (VoteOption option : optionsList) {
            this.options.put(option.getId(), option);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(Long creator_id) {
        this.creator_id = creator_id;
    }
    public Vote addVote(User user, Integer optionId){
        VoteOption option = this.getOption(optionId);
        Vote vote = new Vote();
        vote.setUser(user);
        vote.setVoteOption(option);
        option.addVote(vote);
        return vote;
    }
}
