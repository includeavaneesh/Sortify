package com.sortify.main.service;

import com.sortify.main.model.SortifyFolder;
import com.sortify.main.model.SortifyUser;
import com.sortify.main.repository.SortifyFolderRepository;
import com.sortify.main.repository.SortifyUserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class FolderServiceTest {
    @Mock
    SortifyUserRepository userRepository;
    @InjectMocks
    UserService userService;
    @Mock
    SortifyFolderRepository folderRepository;
    @InjectMocks
    FolderService folderService;
    private SortifyUser testUser;
    private SortifyFolder testFolder;

    @BeforeEach
    void setUp() {
        testUser = new SortifyUser();
        testUser.setUsername("johndoe");
        testUser.setPassword("password");
        testUser.setUserFirstName("John");
        testUser.setUserLastName("Doe");

        testFolder = new SortifyFolder();
        testFolder.setFolderName(testUser.getUserFirstName() + "-" + testUser.getUserLastName());
        testFolder.setFolderId(testUser.getUsername());
        testUser.setParentFolder(testFolder);

        userService.addUser(testUser);
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void addFolder() {
        when(folderRepository.save(testFolder)).thenReturn(testFolder);
        folderService.addFolder(testFolder);
        verify(folderRepository, times(1)).save(any(SortifyFolder.class));
    }

    @Test
    void deleteFolder() {
        when(folderRepository.save(testFolder)).thenReturn(testFolder);
        folderService.addFolder(testFolder);

        when(folderRepository.findById(testFolder.getFolderId())).thenReturn(Optional.of(testFolder));
        doNothing().when(folderRepository).deleteById(testFolder.getFolderId());
        folderService.deleteFolder(testFolder.getFolderId());
        verify(folderRepository, times(1)).deleteById(any(String.class));
    }

    @Test
    void findFolder() {

        when(folderRepository.save(testFolder)).thenReturn(testFolder);
        folderService.addFolder(testFolder);

        when(userRepository.findById(testUser.getUsername())).thenReturn(Optional.ofNullable(testUser));
        when(folderRepository.findById(testFolder.getFolderId())).thenReturn(Optional.of(testFolder));

        SortifyFolder folder = folderService.findFolder(testFolder.getFolderId());
        assertNotNull(folder);
        assertEquals(folder, testFolder);
    }
}