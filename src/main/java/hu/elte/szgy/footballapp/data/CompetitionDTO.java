package hu.elte.szgy.footballapp.data;

import java.util.List;

public class CompetitionDTO {
    private String name;
    private List<TeamDTO> teams;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TeamDTO> getTeams() {
        return teams;
    }

    public void setTeams(List<TeamDTO> teams) {
        this.teams = teams;
    }
}
