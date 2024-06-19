package com.ameda.kevin.security_reactjs.security;

/*
*
@author ameda
@project security-reactjs
@
*
*/

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class FilterChainConfiguration {
    /*
    * override spring security users and InMemoryUserDetailsManager
    * and passing my own users
    * */


//    @Bean
//    public UserDetailsService userDetailsService(){
//        var user1 = User.withDefaultPasswordEncoder()
//                .username("kevin")
//                .password("{noop}ameda")
//                .roles("USER")
//                .build();
//
//        var user2 = User.withDefaultPasswordEncoder()
//                .username("judy")
//                .password("{noop}mongare")
//                .roles("USER")
//                .build();
//        return new InMemoryUserDetailsManager(List.of(user1,user2));
//    }

//    @Bean
//    public InMemoryUserDetailsManager inMemoryUserDetailsManager(){
//        return new InMemoryUserDetailsManager(
//                User.withUsername("kevin").password("ameda").roles("USER").build(),
//                User.withUsername("judy").password("mongare").roles("USER").build()
//        );
//    }

    /*
    * overriding the provider
    * Here is where the comparison between the passed credentials
    * and the stored ones is happening.
    * Beautifully unfolding
    * DaoAuthenticationProvider is where the magic is happening
    * */

//    @Bean
//    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService){
//        APIAuthenticationProvider ownProvider = new APIAuthenticationProvider(userDetailsService);
//        return new ProviderManager(ownProvider);
//    }

    /*
    * What need's be done now is providing a way to bypass our endpoints because
    * they are currently manned, and we need to have a way to customize the filters
    *  with the UsernamePasswordAuthentication filter which is used by default as a
    * filter
    *
    * */

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
//        return http
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(req->
//                        req.requestMatchers("/user/login").permitAll()
//                                .anyRequest().authenticated()).build();
//    }
}
