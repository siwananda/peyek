package io.gof.peyek.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Project.
 */
@Entity
@Table(name = "project")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Project implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 10, max = 100)
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @NotNull
    @Size(min = 50, max = 200)
    @Column(name = "description", length = 200, nullable = false)
    private String description;

    @Size(max = 5000)
    @Column(name = "content", length = 5000)
    private String content;

    @Column(name = "vote")
    private Long vote;

    @ManyToMany    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "project_bidder",
               joinColumns = @JoinColumn(name="projects_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="bidders_id", referencedColumnName="ID"))
    private Set<Contractor> bidders = new HashSet<>();

    @ManyToOne
    private User owner;

    @ManyToOne
    private User winner;

    @ManyToOne
    private Contractor bid_winner;

    @OneToMany(mappedBy = "owner")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Document> documentss = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getVote() {
        return vote;
    }

    public void setVote(Long vote) {
        this.vote = vote;
    }

    public Set<Contractor> getBidders() {
        return bidders;
    }

    public void setBidders(Set<Contractor> contractors) {
        this.bidders = contractors;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User user) {
        this.owner = user;
    }

    public User getWinner() {
        return winner;
    }

    public void setWinner(User user) {
        this.winner = user;
    }

    public Contractor getBid_winner() {
        return bid_winner;
    }

    public void setBid_winner(Contractor contractor) {
        this.bid_winner = contractor;
    }

    public Set<Document> getDocumentss() {
        return documentss;
    }

    public void setDocumentss(Set<Document> documents) {
        this.documentss = documents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Project project = (Project) o;

        if ( ! Objects.equals(id, project.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Project{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", content='" + content + "'" +
            ", vote='" + vote + "'" +
            '}';
    }
}
