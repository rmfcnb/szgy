package hu.elte.szgy.footballapp.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.elte.szgy.footballapp.data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("team")
@Transactional
public class TeamManager {

    @Autowired
    private TeamRepository teamRepo;

    @GetMapping("/all")
    public ResponseEntity<List<Team>> getAllTeams(){
        List<Team> teamList = teamRepo.findAll();

        return new ResponseEntity<>(teamList, HttpStatus.OK);
    }

    @GetMapping("/byname/")
    public ResponseEntity<List<Team>> getTeamsByName(){
        return getTeamsByName("");
    }

    @GetMapping("/byname/{name}")
    public ResponseEntity<List<Team>> getTeamsByName(@PathVariable(value = "name") String name){
        List<Team> teams = teamRepo.findAll();

        return new ResponseEntity<>(teams.stream().filter(competition -> competition.getName().toLowerCase().contains(name.toLowerCase())).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/byId/{id}")
    public ModelAndView getCompetitionById(@PathVariable("id") int teamId){
        ModelAndView mv = new ModelAndView("team");
        Optional<Team> team = teamRepo.findById(teamId);

        if(team.isPresent()){
            TeamDTO teamDTO = new TeamDTO();
            teamDTO.setName(team.get().getName());
            List<MatchDTO> matches = team.get().getAwayMatches().stream().map(Match::getMatchDTO).collect(Collectors.toList());
            matches.addAll(team.get().getHomeMatches().stream().map(Match::getMatchDTO).collect(Collectors.toList()));
            teamDTO.setMatches(matches);
            List<CompetitionNameDTO> competitions = team.get().getCompetitions().stream().map(Competition::getCompetitionNameDTO).collect(Collectors.toList());
            teamDTO.setCompetitions(competitions);
            try {
                String teamJson = (new ObjectMapper()).writeValueAsString(teamDTO);
                mv.addObject("team", teamJson);
            } catch (JsonProcessingException e) {
                mv.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                e.printStackTrace();
            }
        }
        else{
            mv.setStatus(HttpStatus.NOT_FOUND);
        }

        return mv;
    }

    @PostMapping("/new")
    public ResponseEntity<Team> createTeam(@RequestBody TeamNameDTO team){
        Team newTeam = new Team();
        newTeam.setTeamId(getNewId());
        newTeam.setName(team.getName());
        teamRepo.saveAndFlush(newTeam);
        return new ResponseEntity<>(newTeam, HttpStatus.OK);
    }

    private int getNewId(){
        Optional<Team> maxIdTeam = teamRepo.findAll().stream().max(Comparator.comparingInt(Team::getTeamId));
        return maxIdTeam.map(team -> team.getTeamId() + 1).orElse(0);
    }
}
