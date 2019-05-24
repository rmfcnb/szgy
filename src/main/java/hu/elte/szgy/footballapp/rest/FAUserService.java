package hu.elte.szgy.footballapp.rest;

import hu.elte.szgy.footballapp.data.User;
import hu.elte.szgy.footballapp.data.UserRepository;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class FAUserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepo;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {
        User user = userRepo.findById(username).get();
        return new FAUserDetails(user);
    }
}
