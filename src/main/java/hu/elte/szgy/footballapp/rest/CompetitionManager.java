package hu.elte.szgy.footballapp.rest;

import hu.elte.szgy.footballapp.data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@RestController
@RequestMapping("competition")
@Transactional
public class CompetitionManager {
    @Autowired
    private CompetitionRepository compRepo;

    @Autowired
    private LeagueRepository leagueRepo;

    @Autowired
    private CupRepository cupRepo;

    @Autowired
    private TeamRepository teamRepo;

    @Autowired
    private MatchRepository matchRepo;

    @GetMapping("/all")
    public ResponseEntity<List<Competition>> getAllCompetition(@RequestParam(name = "type", required = false) String type) {
        List<Competition> compList = new LinkedList<>();
            if(type == null){
                compList = compRepo.findAll();
            }
            else if(type.equals("LEAGUE")){
                for(League league : leagueRepo.findAll()){
                    compList.add(league);
                }
            }
            else if(type == null || type.equals("CUP")){
                for(Cup cup : cupRepo.findAll()){
                    compList.add(cup);
                }
            }
            else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        //}
        return new ResponseEntity<>(compList, HttpStatus.OK);
    }

    @GetMapping("/allcomp")
    public ModelAndView getAllCompetitionsPage(){
        ModelAndView mv = new ModelAndView("competitions");
        mv.addObject("Competition", getAllCompetition("").toString());
        return mv;
    }

    @GetMapping("/{id}/teams")
    public ResponseEntity<LinkedList<Team>> getAllTeamsByCompetition (@PathVariable("id") int id){
        Optional<Competition> comp = compRepo.findById(id);
        return comp.map(competition -> new ResponseEntity<>(new LinkedList<>(competition.getTeams()), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    @GetMapping("/{id}")
    public ResponseEntity<Competition> getCompetition(@PathVariable("id") Integer id){
        Competition comp = compRepo.findById(id).get();
        return new ResponseEntity<>(comp, HttpStatus.OK);
    }

    @GetMapping("/byname/{name}")
    public ResponseEntity<Competition> getCompetitionByName(@PathVariable("name") String name){
        return getCompetition(compRepo.findByName(name).getCompId());
    }

    @GetMapping("/generateCompetitions")
    public ResponseEntity<Void> getGenerateCompetitions(){

        try{
            ArrayList<League> comps = new ArrayList<>();
            ArrayList<Team> teams = new ArrayList<>();

            for(int i=0; i<20; i++){
                Team team = new Team();
                team.setTeamId(i);
                team.setName("Team " + i);
                teams.add(team);
            }

            for(int i=0; i<10; i++){
                comps.add(new League(i, "League "+i));
                Set<Team> compTeams = new HashSet<>(8);
                Collections.shuffle(teams);
                for(int j=0; j<8; ++j){
                    compTeams.add(teams.get(j));
                    Set<Competition> teamComps = teams.get(j).getCompetitions();
                    teamComps.add(comps.get(i));
                    teams.get(j).setCompetitions(teamComps);
                }
                comps.get(i).setTeams(compTeams);

            }
            if(!comps.stream().anyMatch(league -> leagueRepo.findById(league.getCompId()).isPresent())){
                leagueRepo.saveAll(comps);
            }
        }
        catch (Exception e){
            System.out.println(e.getCause());
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
