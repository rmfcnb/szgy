package hu.elte.szgy.footballapp.rest;

import hu.elte.szgy.footballapp.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("user")
@Transactional
public class UserManager {

    @Autowired
    private UserRepository userRepo;

    @PostMapping("/dispatch")
    public ResponseEntity<Void> dispatchUser() {
        SecurityContext cc = SecurityContextHolder.getContext();
        HttpHeaders headers = new HttpHeaders();
        if (cc.getAuthentication() != null) {
            Authentication a = cc.getAuthentication();
            try {
                headers.setLocation(
                        new URI("/" + userRepo.getOne(a.getName()).getType().toString().toLowerCase() + "_home.html"));
            } catch (URISyntaxException e) {}
        }

        return new ResponseEntity<Void>(headers, HttpStatus.FOUND);
    }
}
