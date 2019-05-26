package hu.elte.szgy.footballapp.data;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name="favourite")
@PrimaryKeyJoinColumn(name = "favId")
public class Favourite implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID")
    private int favId;

    @OneToOne(mappedBy = "favourite", cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = false)
    private User user;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Competition> competitions = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Team> teams = new HashSet<>();

    public int getFavId() {
        return favId;
    }

    public void setFavId(int favId) {
        this.favId = favId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Competition> getCompetitions() {
        return competitions;
    }

    public void setCompetitions(Set<Competition> competitions) {
        this.competitions = competitions;
    }

    public Set<Team> getTeams() {
        return teams;
    }

    public void setTeams(Set<Team> teams) {
        this.teams = teams;
    }

    public void addCompetition(Competition comp){
        competitions.add(comp);
    }

    public void addTeam(Team team){
        teams.add(team);
    }

    public FavouriteDTO getFavouriteDTO(){
        FavouriteDTO fDTO = new FavouriteDTO();
        fDTO.setCompetitions(competitions.stream().map(Competition::getCompetitionNameDTO).collect(Collectors.toList()));
        fDTO.setTeams(teams.stream().map(Team::getTeamNameDTO).collect(Collectors.toList()));
        return fDTO;
    }
}
