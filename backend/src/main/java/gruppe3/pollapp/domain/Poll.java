package gruppe3.pollapp.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Poll {

    private int id;
    private String question;
    private Instant publishedAt;
    private Instant validUntil;

    //@JsonBackReference
    //private User creator;
    @JsonManagedReference
    private Map<String, VoteOption> options;

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

    public Map<String, VoteOption> getOptions() {
        return options;
    }

    public void setOptions(Map<String, VoteOption> options) {
        this.options = options;
    }

    public void setOptionsList(List<VoteOption> optionsList) {
        this.options = new HashMap<>();
        for (VoteOption option : optionsList) {
            this.options.put(option.getId(), option);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
