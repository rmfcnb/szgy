package hu.elte.szgy.footballapp.data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
@JsonIgnoreProperties({"type"})
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

    public TeamNameDTO getTeamNameDTO(){
        TeamNameDTO tnDTO = new TeamNameDTO();
        tnDTO.setTeamId(teamId);
        tnDTO.setName(name);
        return tnDTO;
    }

    public TeamDTO getTeamDTO(){
        TeamDTO tDTO = new TeamDTO();
        tDTO.setTeamId(teamId);
        tDTO.setName(name);
        tDTO.setCompetitions(competitions.stream().map(Competition::getCompetitionNameDTO).collect(Collectors.toList()));
        List<MatchDTO> matches = homeMatches.stream().map(Match::getMatchDTO).collect(Collectors.toList());
        matches.addAll(awayMatches.stream().map(Match::getMatchDTO).collect(Collectors.toList()));
        tDTO.setMatches(matches);
        return tDTO;
    }

    public CompetitionTableRecordDTO getCompetitionTableRecordDTO(int compId){
        CompetitionTableRecordDTO ctrDTO = new CompetitionTableRecordDTO();

        List<Match> hMatches = homeMatches.stream().filter(match -> match.getCompetition().getCompId() == compId).collect(Collectors.toList());
        List<Match> aMatches = awayMatches.stream().filter(match -> match.getCompetition().getCompId() == compId).collect(Collectors.toList());

        ctrDTO.setTeamId(teamId);
        ctrDTO.setName(name);

        int winNum = (int)hMatches.stream().filter(match -> match.getWasPlayed() && match.isHomeWin()).count()+
                (int)aMatches.stream().filter(match -> match.getWasPlayed() && match.isAwayWin()).count();

        int drawNum = (int)hMatches.stream().filter(match -> match.getWasPlayed() && match.isDraw()).count()+
                (int)aMatches.stream().filter(match -> match.getWasPlayed() && match.isDraw()).count();

        int loseNum = (int)hMatches.stream().filter(match -> match.getWasPlayed() && match.isAwayWin()).count()+
                (int)aMatches.stream().filter(match -> match.getWasPlayed() && match.isHomeWin()).count();

        ctrDTO.setWins(winNum);
        ctrDTO.setDraws(drawNum);
        ctrDTO.setLoses(loseNum);

        ctrDTO.setMatches(winNum + drawNum + loseNum);

        int goals = hMatches.stream().map(Match::getHomeGoals).reduce(0, Integer::sum)+
                aMatches.stream().map(Match::getAwayGoals).reduce(0, Integer::sum);

        int goalsAgainst = aMatches.stream().map(Match::getHomeGoals).reduce(0, Integer::sum)+
                hMatches.stream().map(Match::getAwayGoals).reduce(0, Integer::sum);

        ctrDTO.setGoals(goals);
        ctrDTO.setGoalsAgainst(goalsAgainst);
        ctrDTO.setGoalDif(goals-goalsAgainst);
        ctrDTO.setPoints((winNum*3) + drawNum);
        return ctrDTO;
    }
}
