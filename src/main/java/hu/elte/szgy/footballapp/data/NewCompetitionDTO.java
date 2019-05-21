package hu.elte.szgy.footballapp.data;

import java.util.List;

public class NewCompetitionDTO {
    private String competitionName;
    private List<String> teamNames;

    public List<String> getTeamNames(){
        return teamNames;
    }

    public void setTeamNames(List<String> id){
        this.teamNames = id;
    }

    public String getCompetitionName() {
        return competitionName;
    }

    public void setCompetitionName(String competitionName) {
        this.competitionName = competitionName;
    }
}
