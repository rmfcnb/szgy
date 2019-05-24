package hu.elte.szgy.footballapp.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class FAWebSecurity extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/login*").permitAll()
                    .antMatchers("/competition/all").permitAll()
                    .antMatchers("/competitions").permitAll()
                    .antMatchers("/competition/allcomp").permitAll()
                    .antMatchers("/competition/generateCompetitions").permitAll()
                    .antMatchers(HttpMethod.GET, "/","/extjs/**").permitAll()
                    .and()
                .csrf().disable()
                .formLogin()
                    .loginPage("/login")
                    .successForwardUrl("/user/dispatch")
                    .permitAll()
                    .and()
                .logout()
                    .permitAll();
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        return new FAUserService();
    }
}
