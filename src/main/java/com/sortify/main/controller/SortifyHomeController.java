package com.sortify.main.controller;

import com.sortify.main.model.SortifyFolder;
import com.sortify.main.service.SortifyCloudStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sortify.main.model.SortifyUser;
import com.sortify.main.service.SortifyUserService;

/**
 * This is the User controller RESTApi endpoint controller class
 */


@RestController
@RequestMapping("")
public class SortifyHomeController {

    @Autowired
    private SortifyUserService server;

    @Autowired
    private SortifyCloudStorageService storageService;


    @GetMapping("")
    public String getResponse() {
        return "Welcome to Sortify \uD83D\uDE80";
    }
    //have one point of entrance to model

    @PostMapping("/signup")
    public ResponseEntity<?> signUpUser(@RequestBody SortifyUser user) {

        //Check if the user with the same username exists or not
        if(server.findUserByUsername(user.getUsername()) == null){
            // Assign a unique parent folder to user (folder ID = username)
            SortifyFolder newFolder = new SortifyFolder();
            newFolder.setUser(user);
            newFolder.setFolderId(user.getUsername());
            newFolder.setFolderName(user.getUserFirstName() + "-" + user.getUserLastName());
            user.setParentFolder(newFolder);

            // Create a folder in S3 Bucket with S3 folder name = folder ID
            storageService.createUserFolder(newFolder.getFolderId());
            // Add user to database
            server.addUser(user);
            // Return added user
            return ResponseEntity.ok().body(server.findUserByUsername(user.getUsername()));
        }

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("User with username " + user.getUsername() + " already exists");

    }

    @DeleteMapping("/deleteUser")
    public String deleteUser(@RequestBody SortifyUser user) {

        storageService.deleteUserFolder(user.getUsername());

        String username = user.getUsername();
        if(server.findUserByUsername(username) != null){
            server.deleteUser(username);
            return "Deleted";
        }

        return "Doesnt exist";

    }

    @PostMapping("/login")
    public String loginUser(@RequestBody SortifyUser user) {
        return "Under construction";
    }

    @PostMapping("/logout")
    public String logoutUser(@RequestBody SortifyUser user) {
        return "Under construction";
    }




}
