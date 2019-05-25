package hu.elte.szgy.footballapp.data;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class CompetitionTableDTO {
    private String name = "";
    private List<CompetitionTableRecordDTO> content = new LinkedList<>();
    private List<MatchDTO> matches = new LinkedList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CompetitionTableRecordDTO> getContent() {
        return content;
    }

    public void setContent(List<CompetitionTableRecordDTO> content) {
        this.content = content;
        this.content.sort(Comparator.comparingInt(CompetitionTableRecordDTO::getPoints));
    }

    public List<MatchDTO> getMatches() {
        return matches;
    }

    public void setMatches(List<MatchDTO> matches) {
        this.matches = matches;
    }
}
