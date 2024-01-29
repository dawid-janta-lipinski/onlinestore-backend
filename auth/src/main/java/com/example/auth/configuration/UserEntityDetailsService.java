package com.example.auth.configuration;

import com.example.auth.dao.UserDao;
import com.example.auth.model.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserEntityDetailsService implements UserDetailsService {
    private final UserDao userDao;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> user = userDao.findUserByLogin(username);
        return user.map(UserEntityDetails::new).orElseThrow(()-> new UsernameNotFoundException("User not found with name: "+ username));

    }
}
