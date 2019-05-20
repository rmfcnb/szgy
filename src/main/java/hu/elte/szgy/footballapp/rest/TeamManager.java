package hu.elte.szgy.footballapp.rest;

import hu.elte.szgy.footballapp.data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/bname/{name}")
    public ResponseEntity<Team> getTeamByName(@PathVariable("name") String name){
        return getTeam(teamRepo.findByName(name).getTeamId());
    }
}
