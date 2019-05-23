package hu.elte.szgy.footballapp.data;

public class MatchDTO {
    private TeamNameDTO homeTeam;
    private TeamNameDTO awayTeam;
    private CompetitionNameDTO competition;

    private int homeGoals;
    private int awayGoals;

    public TeamNameDTO getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(TeamNameDTO homeTeam) {
        this.homeTeam = homeTeam;
    }

    public TeamNameDTO getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(TeamNameDTO awayTeam) {
        this.awayTeam = awayTeam;
    }

    public CompetitionNameDTO getCompetition() {
        return competition;
    }

    public void setCompetition(CompetitionNameDTO competition) {
        this.competition = competition;
    }

    public int getHomeGoals() {
        return homeGoals;
    }

    public void setHomeGoals(int homeGoals) {
        this.homeGoals = homeGoals;
    }

    public int getAwayGoals() {
        return awayGoals;
    }

    public void setAwayGoals(int awayGoals) {
        this.awayGoals = awayGoals;
    }
}
