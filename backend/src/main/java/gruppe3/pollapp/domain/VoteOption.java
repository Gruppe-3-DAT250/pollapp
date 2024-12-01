package gruppe3.pollapp.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class VoteOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String caption;
    private long presentationOrder;

    @ManyToOne
    private Poll poll;

    public VoteOption() {
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public long getPresentationOrder() {
        return presentationOrder;
    }

    public void setPresentationOrder(long presentationOrder) {
        this.presentationOrder = presentationOrder;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }

    // public void addVote(Vote vote) {
    // votes.add(vote);
    // }
    //
    // public void removeVote(Vote vote) {
    // votes.remove(vote);
    // }

}
