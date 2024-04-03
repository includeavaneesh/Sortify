package com.sortify.main.service;

import com.sortify.main.model.SortifyUser;
import com.sortify.main.repository.SortifyUserRepository;
import org.junit.jupiter.api.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
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

    private SortifyUser testUser;

    @BeforeEach
    void setUp() {
        testUser = new SortifyUser();
        testUser.setUsername("johndoe");
        testUser.setPassword("password");
        testUser.setUserFirstName("John");
        testUser.setUserLastName("Doe");
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addUser() {
        when(userRepository.save(testUser)).thenReturn(testUser);
        SortifyUser savedUser = userService.addUser(testUser);
        assertNotNull(savedUser);
        verify(userRepository, times(1)).save(any(SortifyUser.class));
    }

    @Test
    void deleteUser() {
        when(userRepository.save(testUser)).thenReturn(testUser);
        userService.addUser(testUser);

        when(userRepository.findById(testUser.getUsername())).thenReturn(Optional.of(testUser));
        doNothing().when(userRepository).deleteById(testUser.getUsername());
        userService.deleteUser(testUser.getUsername());

        assertAll(()->userService.deleteUser(testUser.getUsername()));
    }

    @Test
    void retrieveAll() {
        ArrayList<SortifyUser> userList = new ArrayList<>();
        userList.add(testUser);
        when(userRepository.findAll()).thenReturn(userList);
        ArrayList<SortifyUser> userRetrieved = userService.retrieveAll();
        assertEquals(1, userRetrieved.size());
    }

    @Test
    void findUserByUsername() {
        when(userRepository.findById(testUser.getUsername())).thenReturn(Optional.ofNullable(testUser));
        SortifyUser userRetrieved = userService.findUserByUsername(testUser.getUsername());
        assertEquals(userRetrieved, testUser);
    }

    @Test
    void modifyUser() throws IllegalAccessException {
        SortifyUser modifiedUser = new SortifyUser(testUser);
        modifiedUser.setUserFirstName("ben");
        when(userRepository.save(testUser)).thenReturn(modifiedUser);
        when(userRepository.findById(testUser.getUsername())).thenReturn(Optional.ofNullable(testUser));
        SortifyUser userRetrieved = userService.modifyUser(testUser.getUsername(), modifiedUser);
        assertEquals("ben",userRetrieved.getUserFirstName());
    }

}