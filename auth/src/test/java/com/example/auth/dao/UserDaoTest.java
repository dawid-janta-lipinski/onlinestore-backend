package com.example.auth.dao;

import com.example.auth.model.Role;
import com.example.auth.model.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserDaoTest {

    @Autowired
    private UserDao underTest;

    @Test
    void findUserByLogin_shouldFindByLogin() {
        //given
        UserEntity user = new UserEntity(1, "ex", "didi", "didi@gmail.com", "12345", Role.USER, true, false);
        underTest.save(user);

        //when
        UserEntity searchedUser = underTest.findUserByLogin("didi").orElse(null);

        //then
        assertThat(searchedUser).isEqualTo(user);
    }

    @Test
    void findUserByEmail() {
    }

    @Test
    void findUserByUuid() {
    }

    @Test
    void findUserByLoginIfNotLockAndEnabled() {
    }

    @Test
    void findUserByLoginAndLockAndEnabledAndIsAdmin() {
    }
}