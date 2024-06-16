package org.example.backend.config;

import org.example.backend.services.PasswordHashingService;
import org.example.backend.services.UserService;
import org.example.backend.utils.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordHashingService passwordHashingService;
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * This method creates a new SecurityFilterChain
     *
     * @param http the http security
     * @param corsConfigurationSource the cors configuration source
     * @return the security filter chain that has been created
     * @throws Exception if any error oocurs during the creation of the security filter chain
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CorsConfigurationSource corsConfigurationSource) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(registry -> {
                    registry.requestMatchers("/auth/login").permitAll();
                    registry.requestMatchers("/frontend/codes").permitAll();
                    registry.anyRequest().authenticated(); })
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * This method returns the user details service
     *
     * @return the user details service that has been created
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return userService;
    }

    /**
     * This method creates a new AuthenticationProvider
     *
     * @return the authentication provider that has been created
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userService);
        provider.setPasswordEncoder(passwordHashingService);
        return provider;
    }

    /**
     * This method exports the password encoder
     *
     * @return the password encoder that has been created
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return passwordHashingService;
    }

    /**
     * This method creates a new AuthenticationManager
     *
     * @return the authentication manager that has been created
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(authenticationProvider());
    }
}
