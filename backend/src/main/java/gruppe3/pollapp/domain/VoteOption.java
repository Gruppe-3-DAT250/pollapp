package gruppe3.pollapp.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.ArrayList;
import java.util.List;

public class VoteOption {

    private String caption;
    private long presentationOrder;
    private Integer id;

    @JsonManagedReference
    private List<Vote> votes;


    public VoteOption() {
        this.votes = new ArrayList<>();
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }

    public void addVote(Vote vote) {
        votes.add(vote);
    }

    public void removeVote(Vote vote){
        votes.remove(vote);
    }


}
