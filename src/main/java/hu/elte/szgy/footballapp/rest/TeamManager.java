package hu.elte.szgy.footballapp.rest;

import com.fasterxml.jackson.annotation.JsonCreator;
import hu.elte.szgy.footballapp.data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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

    @GetMapping("/{id}")
    public ResponseEntity<Team> getTeam(@PathVariable("id") Integer id){
        Team team = teamRepo.findById(id).get();
        return new ResponseEntity<>(team, HttpStatus.OK);
    }

    @GetMapping("/byname/{name}")
    public ResponseEntity<Team> getTeamByName(@PathVariable("name") String name){
        Optional<Team> team = teamRepo.findByName(name);

        if(!team.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return getTeam(team.get().getTeamId());
    }

    @PostMapping("/new")
    public ResponseEntity<Team> createTeam(@RequestBody TeamDTO team){
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
