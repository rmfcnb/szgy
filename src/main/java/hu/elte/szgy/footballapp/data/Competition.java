package hu.elte.szgy.footballapp.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "competition")
@Inheritance(strategy = InheritanceType.JOINED)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @JsonSubTypes.Type(value = League.class, name = "LEAGUE"), @JsonSubTypes.Type(value = Cup.class, name = "CUP") })
@JsonIgnoreProperties({ "type" })
public class Competition implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID")
    private int compId;

    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "competition", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Match> matches = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "team_comp",
            joinColumns = { @JoinColumn(name = "compId", referencedColumnName = "ID")},
            inverseJoinColumns = { @JoinColumn(name = "teamId", referencedColumnName = "ID")})
    @JsonIgnore
    private Set<Team> teams = new HashSet<>();

    public int getCompId() {
        return compId;
    }
    public void setCompId(int id) {
        this.compId = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Set<Match> getMatches() { return matches; }
    public void setMatches(Set<Match> matches) { this.matches = matches; }

    public Set<Team> getTeams() { return teams; }
    public void setTeams(Set<Team> teams) { this.teams = teams; }

    public String getType() {
        if (getClass() == League.class)
            return "LEAGUE";
        if (getClass() == Cup.class)
            return "CUP";
        return "????";
    }

    public CompetitionNameDTO getCompetitionNameDTO(){
        CompetitionNameDTO cnDTO = new CompetitionDTO();
        cnDTO.setName(name);
        return cnDTO;
    }

    public CompetitionDTO getCompetitionDTO(){
        CompetitionDTO cDTO = new CompetitionDTO();
        cDTO.setName(name);
        cDTO.setTeams(teams.stream().map(Team::getTeamNameDTO).collect(Collectors.toList()));
        return cDTO;
    }
}
