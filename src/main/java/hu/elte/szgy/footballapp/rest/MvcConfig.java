package hu.elte.szgy.footballapp.rest;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    public void addViewControllers(ViewControllerRegistry registry){
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/registration").setViewName("registration");
        registry.addViewController("/competitions").setViewName("competitions");
        registry.addViewController("/teams").setViewName("teams");
        registry.addViewController("/newTeam").setViewName("new_team");
        registry.addViewController("/newCompetition").setViewName("new_competition");
        registry.addViewController("/favourites").setViewName("favourites");
    }

}
