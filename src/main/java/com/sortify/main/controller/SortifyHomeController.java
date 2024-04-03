package com.sortify.main.controller;

import java.util.ArrayList;

import com.sortify.main.model.SortifyFolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sortify.main.model.SortifyUser;
import com.sortify.main.service.SortifyUserService;
import com.sortify.main.service.CloudStorageService;
/**
 * This is the User controller RESTApi endpoint controller class
 */


@RestController
public class SortifyHomeController {
	
	@Autowired
	private SortifyUserService server;

	@Autowired
	private CloudStorageService storageService;

	@GetMapping("/welcome")
	public String getResponse() {
		return "Sortify Landing Page";
	}
	//have one point of entrance to model

	@PostMapping("/signup")
	public SortifyUser signUpUser(@RequestBody SortifyUser user) {

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
		return server.findUserByUsername(user.getUsername());
	}

	@PostMapping("/login")
	public String loginUser(@RequestBody SortifyUser user) {
		return "Under construction";
	}

	@PostMapping("/logout")
	public String logoutUser(@RequestBody SortifyUser user) {
		return "Under construction";
	}



	@GetMapping("/{username}")
	public SortifyUser getUser(@PathVariable String username) {
		return server.findUserByUsername(username);
	}


	@DeleteMapping("/delete/{username}")
	public void removeUser(@PathVariable String username) {
		storageService.deleteUserFolder(username);
		server.deleteUser(username);
	}
	
	@PatchMapping(path="/{username}")
	public ResponseEntity<SortifyUser> updateUser(
			@PathVariable String username, 
			@RequestBody SortifyUser newUserData) {
		
		try {
			SortifyUser tempUser = server.findUserByUsername(username);
			
			if(tempUser!=null) {
				tempUser = server.modifyUser("newUserData", tempUser);
				return ResponseEntity.status(HttpStatus.OK).body(tempUser);
			}
			
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		catch(IllegalAccessException e) {
			ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		} catch (Exception e) {
			ResponseEntity.status(HttpStatus.NON_AUTHORITATIVE_INFORMATION).build();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

	}
	
	
}
