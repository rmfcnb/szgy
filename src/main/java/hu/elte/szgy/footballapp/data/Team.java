package hu.elte.szgy.footballapp.data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "team")
@Inheritance(strategy = InheritanceType.JOINED)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonIgnoreProperties({ "type"})
public class Team implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID")
    private int teamId;

    @Column(unique = true)
    private String name;

    @ManyToMany(mappedBy = "teams", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Competition> competitions = new HashSet<>();

    @OneToMany(mappedBy = "homeTeam", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Match> homeMatches;

    @OneToMany(mappedBy = "awayTeam", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Match> awayMatches;

    public int getTeamId() { return teamId; }
    public void setTeamId(int id) { this.teamId = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Set<Competition> getCompetitions() {return competitions;}
    public void setCompetitions(Set<Competition> comps) {this.competitions = comps;}

    public Set<Match> getHomeMatches() {
        return homeMatches;
    }

    public void setHomeMatches(Set<Match> homeMatches) {
        this.homeMatches = homeMatches;
    }

    public Set<Match> getAwayMatches() {
        return awayMatches;
    }

    public void setAwayMatches(Set<Match> awayMatches) {
        this.awayMatches = awayMatches;
    }
}
