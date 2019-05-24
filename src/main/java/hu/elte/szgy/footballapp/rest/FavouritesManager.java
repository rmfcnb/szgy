package hu.elte.szgy.footballapp.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.elte.szgy.footballapp.data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Autowired
    private UserRepository userRepo;

    @GetMapping("/all")
    public ModelAndView getFavourites(){
        ModelAndView mv = new ModelAndView("favourites");
        Optional<User> currUser = getCurrentUser();
        if(currUser.isPresent()){
            Optional<Favourite> fav = favRepo.findByUser(currUser.get());
            if(fav.isPresent()){
                FavouriteDTO favDTO = fav.get().getFavouriteDTO();
                try {
                    String favJson = (new ObjectMapper()).writeValueAsString(favDTO);
                    mv.addObject("favourites", favJson);
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

    public Optional<User> getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> currentUser = Optional.empty();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            currentUser = userRepo.findById(currentUserName);
        }

        return currentUser;
    }
}
