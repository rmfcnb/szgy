package hu.elte.szgy.footballapp.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.elte.szgy.footballapp.data.Match;
import hu.elte.szgy.footballapp.data.MatchDTO;
import hu.elte.szgy.footballapp.data.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@RestController
@RequestMapping("match")
@Transactional
public class MatchManager {
    @Autowired
    private MatchRepository matchRepo;

    @PostMapping("/editMatch")
    public ResponseEntity setMatch(@RequestBody MatchDTO matchDTO){
        Optional<Match> match = matchRepo.findById(matchDTO.getMatchId());

        if(match.isPresent()){
            match.get().setHomeGoals(matchDTO.getHomeGoals());
            match.get().setAwayGoals(matchDTO.getAwayGoals());
            return new ResponseEntity(HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/editMatch/{id}")
    public ModelAndView editMatch(@PathVariable("id") int matchId){
        Optional<Match> match = matchRepo.findById(matchId);
        ModelAndView mv = new ModelAndView("edit_match");

        if(!match.isPresent()){
            mv.setStatus(HttpStatus.NOT_FOUND);
        }
        else{
            try {
                String matchJson = (new ObjectMapper()).writeValueAsString(match.get().getMatchDTO());
                mv.addObject("match", matchJson);
            } catch (JsonProcessingException e) {
                mv.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                e.printStackTrace();
            }
        }

        return mv;
    }
}
