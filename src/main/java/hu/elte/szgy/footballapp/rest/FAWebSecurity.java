package hu.elte.szgy.footballapp.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user").password(passwordEncoder().encode("userPsw")).roles("USER")
                .and()
                .withUser("admin").password(passwordEncoder().encode("adminPsw")).roles("ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
//                    .antMatchers("/login*").permitAll()
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
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        return new FAUserService();
    }
}
