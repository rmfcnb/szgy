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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int matchId;

    @ManyToOne
    @JoinColumn(name = "fk_compId", nullable = false)
    @JsonIgnore
    private Competition competition;

    @Column(name = "fk_compId", insertable = false, updatable = false)
    private int competitionId;

    private int homeGoals;
    private int awayGoals;

    /*private Date date;*/

    public int getMatchId() {
        return matchId;
    }
    public void setMatchId(int id) {
        this.matchId = id;
    }

    public int getCompetitionId() {
        return competitionId;
    }
    public void setCompetitionId(int id) {
        this.competitionId = id;
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
}
