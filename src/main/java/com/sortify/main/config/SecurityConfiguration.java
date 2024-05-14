package com.sortify.main.config;

import com.sortify.main.service.JpaUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final JpaUserDetailsService USER_DETAIL_SERVICE;

    public SecurityConfiguration(JpaUserDetailsService USER_DETAIL_SERVICE) {
        this.USER_DETAIL_SERVICE = USER_DETAIL_SERVICE;
    }


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> {
                            // Allow sign up page to have no auth needed
                            auth.requestMatchers(HttpMethod.POST,"/signup").permitAll();
                            auth.requestMatchers("/{username}/**").access(new WebExpressionAuthorizationManager("#username == authentication.name"));
                            auth.anyRequest().authenticated();
                        }
                )
                .userDetailsService(USER_DETAIL_SERVICE)
                .formLogin(withDefaults())
                .httpBasic(withDefaults()) // Disable in Prod
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF - Enable in Prod
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
