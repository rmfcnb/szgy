package hu.elte.szgy.footballapp.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.elte.szgy.footballapp.data.Favourite;
import hu.elte.szgy.footballapp.data.FavouriteDTO;
import hu.elte.szgy.footballapp.data.FavouriteRepository;
import hu.elte.szgy.footballapp.data.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@RestController
@RequestMapping("favourites")
@Transactional
public class FavouritesManager {

    @Autowired
    private FavouriteRepository favRepo;

    @GetMapping("/all")
    public ModelAndView getFavourites(){
        ModelAndView mv = new ModelAndView("favourites");
        Optional<User> currUser = (new UserManager()).getCurrentUser();
        if(currUser.isPresent()){
            Optional<Favourite> fav = favRepo.findByUser(currUser.get().getUsername());
            if(fav.isPresent()){
                FavouriteDTO favDTO = fav.get().getFavouriteDTO();
                try {
                    String favJson = (new ObjectMapper()).writeValueAsString(favDTO);
                    mv.addObject("team", favJson);
                } catch (JsonProcessingException e) {
                    mv.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                    e.printStackTrace();
                }
            }
        } else{
            mv.setStatus(HttpStatus.NOT_FOUND);
        }

        return mv;
    }
}
