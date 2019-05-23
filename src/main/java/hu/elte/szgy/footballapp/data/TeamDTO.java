package hu.elte.szgy.footballapp.data;

import java.util.List;

public class TeamDTO extends TeamNameDTO {
    private List<CompetitionNameDTO> competitions;
    private List<MatchDTO> matches;

    public List<CompetitionNameDTO> getCompetitions() {
        return competitions;
    }

    public void setCompetitions(List<CompetitionNameDTO> competitions) {
        this.competitions = competitions;
    }

    public List<MatchDTO> getMatches() {
        return matches;
    }

    public void setMatches(List<MatchDTO> matches) {
        this.matches = matches;
    }
}
