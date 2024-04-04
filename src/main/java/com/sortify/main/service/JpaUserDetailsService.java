package com.sortify.main.service;

import com.sortify.main.model.SortifySecurityUser;
import com.sortify.main.repository.SortifyUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    private final SortifyUserRepository userRepository;

    public JpaUserDetailsService(SortifyUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findById(username)
                .map(SortifySecurityUser::new)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found: " + username));

    }
}
