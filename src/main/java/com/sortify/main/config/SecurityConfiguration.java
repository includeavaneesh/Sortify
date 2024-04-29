package com.sortify.main.config;

import com.sortify.main.service.JpaUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final JpaUserDetailsService jpaUserDetailsService;

    public SecurityConfiguration(JpaUserDetailsService jpaUserDetailsService) {
        this.jpaUserDetailsService = jpaUserDetailsService;
    }


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // Disable CSRF
                .cors(cors -> cors.disable()) // Disable CORS
                .authorizeHttpRequests(auth -> {
                            // Allow sign up page to have no auth needed
                            auth.requestMatchers(HttpMethod.POST,"/").permitAll();
                            auth.requestMatchers(HttpMethod.GET,"/").permitAll();
                            auth.requestMatchers(HttpMethod.POST,"/{username}/**").permitAll();
//                            auth.requestMatchers("/{username}/**").access(new WebExpressionAuthorizationManager("#username == authentication.name"));
                            auth.anyRequest().authenticated();
                        }
                )
                .userDetailsService(jpaUserDetailsService)
                .formLogin(withDefaults())
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
