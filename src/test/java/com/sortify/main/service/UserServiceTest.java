package com.sortify.main.service;

import com.sortify.main.model.SortifyUser;
import com.sortify.main.repository.SortifyUserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserServiceTest {

    @Mock
    SortifyUserRepository userRepository;

    @InjectMocks
    UserService userService;


    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addUser() throws IOException {
        SortifyUser user = new SortifyUser();
        user.setUsername("johndoe");
        user.setPassword("password");
        user.setUserFirstName("John");
        user.setUserLastName("Doe");

        when(userRepository.save(user)).thenReturn(user);
        SortifyUser savedUser = userService.addUser(user);
        assertNotNull(savedUser);
        verify(userRepository, times(2)).save(any(SortifyUser.class));
    }

    @Test
    void deleteUser() {
        SortifyUser user = new SortifyUser();
        user.setUsername("johndoe");
        user.setPassword("password");
        user.setUserFirstName("John");
        user.setUserLastName("Doe");

        when(userRepository.save(user)).thenReturn(user);
        SortifyUser savedUser = userService.addUser(user);
        assertNotNull(savedUser);
        verify(userRepository, times(1)).save(any(SortifyUser.class));

        doNothing().when(userRepository).deleteById(anyString());
        userService.deleteUser("johndoe");
        verify(userRepository, times(1)).deleteById(anyString());

        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
        SortifyUser foundUser = userService.findUserByUsername("johndoe");

        assertNull(foundUser);
    }

    @Test
    void retrieveAll() {
    }

    @Test
    void findUserByUsername() {
    }

    @Test
    void modifyUser() {
    }

    @Test
    void saveUser() {
    }
}