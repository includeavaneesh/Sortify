package com.sortify.main.controller;

import com.sortify.main.model.SortifyFolder;
import com.sortify.main.model.SortifyUser;
import com.sortify.main.service.CloudStorageService;
import com.sortify.main.service.SortifyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class SortifyUserController {
    @Autowired
    private SortifyUserService server;

    @Autowired
    private CloudStorageService storageService;


    @GetMapping("/{username}")
    public SortifyUser getUser(@PathVariable String username) {
        return server.findUserByUsername(username);
    }


    @DeleteMapping("/delete/{username}")
    public void removeUser(@PathVariable String username) {
        storageService.deleteUserFolder(username);
        server.deleteUser(username);
    }

    @PatchMapping(path = "/update/{username}")
    public ResponseEntity<SortifyUser> updateUser(
            @PathVariable String username,
            @RequestBody SortifyUser modifiedUser) {

        try {
            SortifyUser originalUser = server.findUserByUsername(username);

            if (originalUser != null) {
                originalUser = server.modifyUser(username, modifiedUser);
                return ResponseEntity.status(HttpStatus.OK).body(originalUser);
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalAccessException e) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            ResponseEntity.status(HttpStatus.NON_AUTHORITATIVE_INFORMATION).build();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

    }

    @GetMapping("/allUsers")
    public ArrayList<SortifyUser> getAllUsers() {
        return server.retrieveAll();
    }

}
