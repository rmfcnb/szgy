package hu.elte.szgy.footballapp.data;

import java.util.List;

public class FavouriteDTO {
    private List<CompetitionNameDTO> competitions;
    private List<TeamNameDTO> teams;

    public List<CompetitionNameDTO> getCompetitions() {
        return competitions;
    }

    public void setCompetitions(List<CompetitionNameDTO> competitions) {
        this.competitions = competitions;
    }

    public List<TeamNameDTO> getTeams() {
        return teams;
    }

    public void setTeams(List<TeamNameDTO> teams) {
        this.teams = teams;
    }
}
