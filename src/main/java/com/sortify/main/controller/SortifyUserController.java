package com.sortify.main.controller;

import com.sortify.main.model.SortifyFolder;
import com.sortify.main.model.SortifyUser;
import com.sortify.main.service.SortifyCloudStorageService;
import com.sortify.main.service.SortifyFolderService;
import com.sortify.main.service.SortifyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/{username}")
public class SortifyUserController {
    @Autowired
    private SortifyUserService USER_SERVICE;

    @Autowired
    private SortifyCloudStorageService CLOUD_SERVICE;


    @GetMapping("")
    public SortifyUser getUser(@PathVariable String username) {
        return USER_SERVICE.findUserByUsername(username);
    }


    @DeleteMapping("/delete")
    public ResponseEntity<?> removeUser(@PathVariable String username) {

        if(USER_SERVICE.findUserByUsername(username)!=null) {
            CLOUD_SERVICE.deleteUserFolder(username);
            USER_SERVICE.deleteUser(username);
            return ResponseEntity.ok().body("User with " + username + " successfully removed.");
        }
        return ResponseEntity.ok().body("User with " + username + " does not exist.");
    }

    @PatchMapping(path = "/update")
    public ResponseEntity<SortifyUser> updateUser(
            @PathVariable String username,
            @RequestBody SortifyUser modifiedUser) {

        try {
            SortifyUser originalUser = USER_SERVICE.findUserByUsername(username);

            if (originalUser != null) {
                originalUser = USER_SERVICE.modifyUser(username, modifiedUser);
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
        return USER_SERVICE.retrieveAll();
    }

}
