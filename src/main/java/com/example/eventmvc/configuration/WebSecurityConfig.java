package com.example.eventmvc.configuration;

import com.example.eventmvc.handler.CustomAuthenticationFailureHandler;
import com.example.eventmvc.security.CurrentUserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CurrentUserDetailServiceImpl userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/add-event-page").hasAnyAuthority("user")
                .antMatchers("/add-event").hasAnyAuthority("user")
                .antMatchers("/searchUsert").hasAnyAuthority("user")
                .antMatchers("/addContact").hasAnyAuthority("user")
                .antMatchers("/addmyevent").hasAnyAuthority("user")
                .antMatchers("/invite").hasAnyAuthority("user")
                .antMatchers("/seeAllNotification").hasAnyAuthority("user")
                .antMatchers("/plus45Messages").hasAnyAuthority("user")
                .antMatchers("/faceMessage").hasAnyAuthority("user")
                .antMatchers("/seeMessages").hasAnyAuthority("user")
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/page-login")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/loginSuccess")
                .failureHandler(new CustomAuthenticationFailureHandler())
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/myhtml")
                .and()
                .rememberMe()
                .tokenValiditySeconds(2*604800) // 1 week
                .and();
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
