package com.app.bank.serviceImpl;

import com.app.bank.entity.User;
import com.app.bank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> user = userRepository.findByEmail(username);

        if(user.isPresent()) {

            User loggingUser = user.get();

            return org.springframework.security.core.userdetails.User.builder()
                    .username(loggingUser.getEmail())
                    .password(loggingUser.getPassword())
                    .roles(loggingUser.getRole())
                    .build();

        } else{
            throw new UsernameNotFoundException("No account found for username"+username);
        }

    }
}
