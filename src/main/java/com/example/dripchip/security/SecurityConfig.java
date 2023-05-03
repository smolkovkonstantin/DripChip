package com.example.dripchip.security;

import com.example.dripchip.security.filter.UserAuthenticatedFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final BCryptPasswordEncoder passwordEncoder;

    private final UserDetailsService userDetailsService;

    private final UserAuthenticatedFilter userAuthenticatedFilter;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .addFilterAfter(userAuthenticatedFilter, BasicAuthenticationFilter.class)
                .authorizeHttpRequests(request -> request
                        .requestMatchers(
                                AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/registration"),
                                AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/accounts/search"),
                                AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/accounts/{accountId}"),
                                AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/animals/{animalId}"),
                                AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/animals/search"),
                                AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/animals/types/{typeId}"),
                                AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/locations/{pointId}"),
                                AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/animals/{animalId}/locations")
                        )
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .httpBasic();
        return httpSecurity.build();
    }

    @Autowired
    public void authenticationManagerBuilder(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }
}
