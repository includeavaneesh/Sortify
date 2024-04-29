package com.sortify.main.controller;

import com.sortify.main.model.SortifyUser;
import com.sortify.main.service.SortifyCloudStorageService;
import com.sortify.main.service.SortifyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;

@RestController
@RequestMapping("/{username}")
public class SortifyUserController {
    @Autowired
    private SortifyUserService server;

    @Autowired
    private SortifyCloudStorageService storageService;


    @GetMapping("")
    public SortifyUser getUser(@PathVariable String username) {
        return server.findUserByUsername(username);
    }


    @GetMapping("/delete")
    public void removeUser(Principal principal) {
        String username = principal.getName();
        storageService.deleteUserFolder(username);
        server.deleteUser(username);
    }

    @PatchMapping(path = "/update")
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
