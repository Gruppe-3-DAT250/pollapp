package gruppe3.pollapp.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.time.Instant;

public class Vote {

    private Instant publishedAt;
    private User user;
    @JsonBackReference
    private VoteOption voteOption;

    public Vote() {

    }

    public Instant getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Instant publishedAt) {
        this.publishedAt = publishedAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public VoteOption getVoteOption() {
        return voteOption;
    }

    public void setVoteOption(VoteOption voteOption) {
        this.voteOption = voteOption;
    }
}
