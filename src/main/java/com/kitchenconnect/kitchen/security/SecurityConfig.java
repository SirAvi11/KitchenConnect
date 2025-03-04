package com.kitchenconnect.kitchen.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                .requestMatchers("/accounts/dashboard").authenticated() // Require authentication
                .anyRequest().permitAll() // Allow all other URLs
            )
            .formLogin(form -> form
            .loginPage("/accounts/login") // Custom login page
            .loginProcessingUrl("/loginUser") // This should match the login form action
            .defaultSuccessUrl("/dashboard", true) // Default redirect after login
            .permitAll()
             )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/") // Redirect to home after logout
                .invalidateHttpSession(true) // Invalidate session
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .csrf().disable(); // Disable CSRF if not needed (use with caution)

        return http.build();
    }
}
