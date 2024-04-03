package com.sortify.main.repository;

import com.sortify.main.model.SortifyUser;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SortifyUserRepositoryTest {
    @Autowired
    private SortifyUserRepository userRepository;
    private SortifyUser testUser;

    @BeforeEach
    void setUp() {
        testUser = new SortifyUser();
        testUser.setUsername("johndoe");
        testUser.setPassword("password");
        testUser.setUserFirstName("John");
        testUser.setUserLastName("Doe");
        userRepository.save(testUser);
    }

    @AfterEach
    void tearDown() {
        userRepository.delete(testUser);
    }

    @Test
    public void saveUser() {
        SortifyUser testUser2 = new SortifyUser();
        testUser2.setUsername("bendover");
        testUser2.setPassword("password");
        testUser2.setUserFirstName("Ben");
        testUser2.setUserLastName("Dover");
        assertNull(userRepository.findById(testUser2.getUsername()).orElse(null));
        userRepository.save(testUser2);
        assertNotNull(userRepository.findById(testUser2.getUsername()).orElse(null));
    }

    @Test
    public void deleteUser() {
        userRepository.deleteById("johndoe");
        SortifyUser foundUser = userRepository.findById(testUser.getUsername()).orElse(null);
        assertNull(foundUser);
    }

    @Test
    public void findUser() {
        assertNotNull(userRepository.findById(testUser.getUsername()).orElse(null));
    }

    @Test
    public void duplicateUsername(){
        SortifyUser testUser2 = new SortifyUser();
        testUser2.setUsername("johndoe");
        testUser2.setPassword("password");
        testUser2.setUserFirstName("Ben");
        testUser2.setUserLastName("Dover");
        userRepository.save(testUser2);
        assertEquals(1,userRepository.findAll().size());
    }

    @Test
    public void updateUser() {
        SortifyUser retrieved = userRepository.findById(testUser.getUsername()).orElse(null);
        retrieved.setUserLastName("Dover");
        userRepository.save(retrieved);
        SortifyUser persistentUser = userRepository.findById(testUser.getUsername()).orElse(null);
        assertEquals(persistentUser.getUserLastName(),"Dover");
    }
}