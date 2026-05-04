package com.example.videogamesshop.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
    private final JwtAccessDeniedHandler accessDeniedHandler;
    private final AppUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                          JwtAuthenticationEntryPoint authenticationEntryPoint,
                          JwtAccessDeniedHandler accessDeniedHandler,
                          AppUserDetailsService userDetailsService,
                          PasswordEncoder passwordEncoder) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/index.html", "/assets/**", "/favicon.ico").permitAll()
                        .requestMatchers("/auth/**", "/api-docs/**", "/swagger-ui/**",
                                "/swagger-ui.html", "/error", "/login/**", "/admin/**", "/user/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/users").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/games/**", "/developers/**", "/publishers/**",
                                "/categories/**").authenticated()
                        .requestMatchers("/async-jobs/**", "/counter/**", "/concurrency/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/users/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/users/*/games/*").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/users/*/games/*").authenticated()
                        .requestMatchers(HttpMethod.POST, "/users").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/developers").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/developers/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/developers/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/developers/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/publishers").authenticated()
                        .requestMatchers(HttpMethod.POST, "/publishers/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/publishers/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/publishers/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/categories").authenticated()
                        .requestMatchers(HttpMethod.POST, "/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/games/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/games/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/games/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
            throws Exception {
        return configuration.getAuthenticationManager();
    }
}
