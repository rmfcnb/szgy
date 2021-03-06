package hu.elte.szgy.footballapp.rest;

import hu.elte.szgy.footballapp.data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.Optional;

@RestController
@RequestMapping("user")
@Transactional
public class UserManager {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private FavouriteRepository favRepo;

    @PostMapping("/dispatch")
    public ResponseEntity<Void> dispatchUser() {
        SecurityContext cc = SecurityContextHolder.getContext();
        HttpHeaders headers = new HttpHeaders();
        if (cc.getAuthentication() != null) {
            Authentication auth = cc.getAuthentication();
            try {
                headers.setLocation(new URI("/home"));
            } catch (URISyntaxException ignored) {}
        }

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @PostMapping("/new")
    public ResponseEntity<Void> createUser(@RequestBody User user){

        if(userRepo.findById(user.getUsername()).isPresent() || user.getPassword().length() == 0){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        user.setPassword("{noop}"+user.getPassword());
        user.setType(User.UserType.USER);
        Favourite fav = new Favourite();
        fav.setFavId(getNewFavouriteId());
        fav.setUser(user);
        user.setFavourite(fav);
        try{
            favRepo.save(fav);
        }catch(Exception e){
            System.out.println(e.getCause());
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/isLoggedIn")
    public ResponseEntity<Boolean> isLoggedIn(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>(!(authentication instanceof AnonymousAuthenticationToken),HttpStatus.OK);
    }

    @GetMapping("/isAdmin")
    public ResponseEntity<Boolean> isAdmin(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = false;
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            Optional<User> currentUser = userRepo.findById(currentUserName);

            if(currentUser.isPresent()){
                isAdmin = currentUser.get().getType() == User.UserType.ADMIN;
            }
        }
        return new ResponseEntity<>(isAdmin, HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout(){
        SecurityContextHolder.clearContext();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private int getNewFavouriteId(){
        Optional<Favourite> maxIdFav = favRepo.findAll().stream().max(Comparator.comparingInt(Favourite::getFavId));
        return maxIdFav.map(favourite -> favourite.getFavId() + 1).orElse(0);
    }
}
