package hu.elte.szgy.footballapp.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.elte.szgy.footballapp.data.*;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;
import java.util.stream.Collectors;

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

    @GetMapping("/byname/")
    public ResponseEntity<List<Competition>> getCompetitionsByName(){
        return getCompetitionsByName("");
    }

    @GetMapping("/byname/{name}")
    public ResponseEntity<List<Competition>> getCompetitionsByName(@PathVariable(value = "name") String name){
        List<Competition> competitions = compRepo.findAll();

        return new ResponseEntity<>(competitions.stream().filter(competition -> competition.getName().toLowerCase().contains(name.toLowerCase())).collect(Collectors.toList()), HttpStatus.OK);
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
        newComp.setCompId(getNewCompetitionId());
        newComp.setName(comp.getCompetitionName());
        newComp.setTeams(teams);

        compRepo.save(newComp);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ExceptionHandler(TypeMismatchException.class)
    public ModelAndView handleTypeMismatchExceptionInGetCompetition(TypeMismatchException ex){
        ModelAndView mv = new ModelAndView("competition");
        mv.setStatus(HttpStatus.NOT_FOUND);
        return mv;
    }

    @GetMapping("/byId/{id}")
    public ModelAndView getCompetitionById(@PathVariable("id") int compId){
        ModelAndView mv = new ModelAndView("competition");
        Optional<Competition> comp = compRepo.findById(compId);

        if(comp.isPresent()){
            CompetitionTableDTO compDTO = new CompetitionTableDTO();
            compDTO.setName(comp.get().getName());
            compDTO.setContent(comp.get().getTeams().stream().map(team -> team.getCompetitionTableRecordDTO(compId)).collect(Collectors.toList()));
            compDTO.setMatches(matchRepo.findAll().stream().filter(match -> match.getCompetition().getCompId() == compId).map(Match::getMatchDTO).collect(Collectors.toList()));
            try {
                String competitionJson = (new ObjectMapper()).writeValueAsString(compDTO);
                mv.addObject("competition", competitionJson);
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

    @GetMapping("/home")
    public ResponseEntity<List<Competition>> getCompetitionsForHome(){
        List<Competition> allComp = compRepo.findAll();
        Collections.shuffle(allComp);
        List<Competition> competitions = new LinkedList<>();

        int compNum = allComp.size() > 10 ? 10 : allComp.size();

        for(int i = 0; i<compNum; ++i){
            competitions.add(allComp.get(i));
        }

        competitions.sort(Comparator.comparing(Competition::getName));

        return new ResponseEntity<>(competitions, HttpStatus.OK);
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

            int matchIdx = 0;
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
                Set<Match> matches = generateMatches(comps.get(i), new LinkedList<>(compTeams), matchIdx);
                comps.get(i).setMatches(matches);
                matchIdx = matches.stream().max(Comparator.comparingInt(Match::getMatchId)).get().getMatchId()+1;
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

    private Set<Match> generateMatches(Competition comp, List<Team> teams, int firstIdx){
        Set<Match> matches = new HashSet<>();
        int currId = firstIdx;
        for(int i=0; i<teams.size()-1; ++i){
            for(int j=i+1; j<teams.size(); ++j){
                Match match = new Match();
                match.setMatchId(currId);
                currId++;
                match.setCompetition(comp);
                match.setHomeTeam(teams.get(i));
                match.setAwayTeam(teams.get(j));
                matches.add(match);
            }
        }

        return matches;
    }

    private ResponseEntity<Competition> getCompetition(Integer id){
        Competition comp = compRepo.findById(id).get();
        return new ResponseEntity<>(comp, HttpStatus.OK);
    }

    private int getNewCompetitionId(){
        Optional<Competition> maxIdComp = compRepo.findAll().stream().max(Comparator.comparingInt(Competition::getCompId));
        return maxIdComp.map(comp -> comp.getCompId() + 1).orElse(0);
    }

    private int getNewMatchId(){
        Optional<Match> maxIdComp = matchRepo.findAll().stream().max(Comparator.comparingInt(Match::getMatchId));
        return maxIdComp.map(match -> match.getMatchId() + 1).orElse(0);
    }
}
