package hu.elte.szgy.footballapp.data;

public class TeamNameDTO {
    protected int teamId;
    protected String name;

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }
}
