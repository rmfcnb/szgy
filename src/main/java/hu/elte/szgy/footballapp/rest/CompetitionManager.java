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
                compList.addAll(leagueRepo.findAll());
            }
            else if(type.equals("CUP")){
                compList.addAll(cupRepo.findAll());
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

    @GetMapping("/byname/{name}")
    public ResponseEntity<Competition> getCompetitionByName(@PathVariable("name") String name){
        return getCompetition(compRepo.findByName(name).getCompId());
    }

    @PostMapping("/new")
    public ResponseEntity<String> createCompetition(@RequestBody NewCompetitionDTO comp){
        Set<Team> teams = new HashSet<>();

        if(comp.getCompetitionName().equals("")){
            return new ResponseEntity<>("Competition must have a name!", HttpStatus.BAD_REQUEST);
        }

        for(String name : comp.getTeamNames()){
            Optional<Team> team = teamRepo.findByName(name);
            if(!team.isPresent()){
                return new ResponseEntity<>("The following team does not exist yet: "+team.get(), HttpStatus.BAD_REQUEST);
            }
            if(teams.contains(team.get())){
                return new ResponseEntity<>("The following team is already in the competition: "+team.get(), HttpStatus.BAD_REQUEST);
            }
            teams.add(team.get());
        }

        Competition newComp = new Competition();
        newComp.setCompId(getNewId());
        newComp.setName(comp.getCompetitionName());
        newComp.setTeams(teams);

        compRepo.save(newComp);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public /*@ResponseBody CompetitionDTO*/ResponseEntity<CompetitionDTO> getCompetitionById(@PathVariable(value = "id", required = true) int compId){
        Optional<Competition> comp = compRepo.findById(compId);

        CompetitionDTO compDTO = new CompetitionDTO();

        if(comp.isPresent()){
            compDTO.setName(comp.get().getName());
            compDTO.setTeams(new LinkedList(comp.get().getTeams()));
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(compDTO, HttpStatus.OK);
    }

    @GetMapping("/byId/{id}")
    public ModelAndView f(@PathVariable("id") int cid){
        ModelAndView mv = new ModelAndView("competition");
        try {
            mv.addObject("competitionId", (new ObjectMapper()).writeValueAsString(cid));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return mv;
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
            if(comps.stream().noneMatch(league -> leagueRepo.findById(league.getCompId()).isPresent())){
                leagueRepo.saveAll(comps);
            }
        }
        catch (Exception e){
            System.out.println(e.getCause());
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private ResponseEntity<Competition> getCompetition(Integer id){
        Competition comp = compRepo.findById(id).get();
        return new ResponseEntity<>(comp, HttpStatus.OK);
    }

    private int getNewId(){
        Optional<Competition> maxIdComp = compRepo.findAll().stream().max(Comparator.comparingInt(Competition::getCompId));
        return maxIdComp.map(team -> team.getCompId() + 1).orElse(0);
    }
}
