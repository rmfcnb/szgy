package hu.elte.szgy.footballapp.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

@Entity
@Table(name = "fmatch")
@Inheritance(strategy = InheritanceType.JOINED)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
public class Match implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID")
    private int matchId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="competitionId")
    @JsonIgnore
    private Competition competition;

    private int homeGoals;
    private int awayGoals;

    @ManyToOne(cascade = CascadeType.ALL)
    private Team homeTeam;

    @ManyToOne(cascade = CascadeType.ALL)
    private Team awayTeam;

    public int getMatchId() {
        return matchId;
    }
    public void setMatchId(int id) {
        this.matchId = id;
    }

    public Competition getCompetition() {
        return competition;
    }
    public void setCompetition(Competition comp) {
        this.competition = comp;
    }

    public int getHomeGoals() {
        return homeGoals;
    }
    public void setHomeGoals(int goals) {
        this.homeGoals = goals;
    }

    public int getAwayGoals() {
        return awayGoals;
    }
    public void setAwayGoals(int goals) {
        this.awayGoals = goals;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(Team homeTeam) {
        this.homeTeam = homeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(Team awayTeam) {
        this.awayTeam = awayTeam;
    }
}
