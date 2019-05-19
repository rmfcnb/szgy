package hu.elte.szgy.footballapp.rest;

import hu.elte.szgy.footballapp.data.Competition;
import hu.elte.szgy.footballapp.data.CompetitionRepository;
import hu.elte.szgy.footballapp.data.MatchRepository;
import hu.elte.szgy.footballapp.data.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("competition")
@Transactional
public class CompetitionManager {
    @Autowired
    private CompetitionRepository compRepo;

    @Autowired
    private TeamRepository teamRepo;

    @Autowired
    private MatchRepository matchRepo;

    @GetMapping("/all")
    public ResponseEntity<List<Competition>> getAllCompetition(@RequestParam(name = "type", required = false) String type){
        List<Competition> compList = compRepo.findAll();
        if (type != null) {
            List<Competition> fullList = compList;
            compList = new ArrayList<>();
            for (Competition e : fullList) {
                if (e.getType().equals(type)) {
                    compList.add(e);
                }
            }
        }
        return new ResponseEntity<>(compList, HttpStatus.OK);
    }

    @GetMapping("/allcomp")
    public ModelAndView getAllCompetitionsPage(){
        ModelAndView mv = new ModelAndView("competitions");
        mv.addObject("Competition", getAllCompetition("").toString());
        return mv;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Competition> getCompetition(@PathVariable("id") Integer id){
        Competition comp = compRepo.findById(id).get();
        return new ResponseEntity<>(comp, HttpStatus.OK);
    }

    @GetMapping("/byname/{name}")
    public ResponseEntity<Competition> getCompetitionByName(@PathVariable("name") String name){
        return getCompetition(compRepo.findByName(name).getId());
    }
}
