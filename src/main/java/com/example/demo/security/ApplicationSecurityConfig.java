package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import static com.example.demo.security.ApplicationUserPermission.COURSE_WRITE;
import static com.example.demo.security.ApplicationUserRole.*;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    // we are authorizing request; every request must be authenticated with basic http mechanism.
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()//TODO: Write Notes (Without CSRF post, put and delete don't work)
                .authorizeRequests()
                .antMatchers("/","index","/css/*","/js/*" ).permitAll()
                //Only Student can access api's which start from api/anything
                .antMatchers("/api/**").hasRole(STUDENT.name())
                .antMatchers(HttpMethod.DELETE, "/management/api/**").hasAuthority(COURSE_WRITE.name())
                .antMatchers(HttpMethod.POST, "/management/api/**").hasAuthority(COURSE_WRITE.name())
                .antMatchers(HttpMethod.PUT, "/management/api/**").hasAuthority(COURSE_WRITE.name())
                .antMatchers("/management/api/**").hasAnyAuthority(ADMIN.name(), ADMIN_TRAINEE.name())
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }

    @Override
    @Bean //review
    //This is how we retrieve our users from database.
    //How the encoding error got removed with just implement PasswordConfig class
    protected UserDetailsService userDetailsService() {
        UserDetails annaSmith =  User.builder()
                .username("Anna")
                .password(passwordEncoder.encode("password"))
                .roles(STUDENT.name()) //ROLE_STUDENT
                .build();
        UserDetails linda = User.builder()
                .username("Linda")
                .password(passwordEncoder.encode("password123"))
                .roles(ADMIN.name())
                .build();
        UserDetails tom = User.builder()
                .username("Tom")
                .password(passwordEncoder.encode("password123"))
                .roles(ADMIN_TRAINEE.name())
                .build();
        return new InMemoryUserDetailsManager(annaSmith, linda);
    }
}
