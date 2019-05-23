package hu.elte.szgy.footballapp.data;

import java.util.List;

public class CompetitionDTO extends CompetitionNameDTO {
    private List<TeamNameDTO> teams;

    public List<TeamNameDTO> getTeams() {
        return teams;
    }

    public void setTeams(List<TeamNameDTO> teams) {
        this.teams = teams;
    }
}
