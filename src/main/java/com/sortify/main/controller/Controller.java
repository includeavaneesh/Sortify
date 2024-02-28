package com.sortify.main.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.sortify.main.model.SortifyUser;
import com.sortify.main.service.UserService;
import com.sortify.main.service.UserServiceInterface;

/**
 * This is the User controller RESTApi endpoint controller class
 */


@RestController
public class Controller {
	
	@Autowired
	private UserServiceInterface server;

	@GetMapping("/hello")
	public String getResponse() {
		return "hello from servr";
	}
	//have one point of entrance to model
	@GetMapping("/{username}")
	public SortifyUser getUser(@PathVariable String username) {
		return server.findUserByUsername(username);
	}
	@PostMapping("/register")
	public ArrayList<SortifyUser> registerUser(@RequestBody SortifyUser user) {
		server.addUser(user);
		return server.retrieveAll();
	}
	
	@DeleteMapping("/remove/{username}")
	public ArrayList<SortifyUser> removeUser(@PathVariable String username) {
		server.deleteUser(username);
		return server.retrieveAll();
	}
	
	@PatchMapping(path="/{username}")
	public ResponseEntity<SortifyUser> updateUser(
			@PathVariable String username, 
			@RequestBody SortifyUser newUserData) {
		
		try {
			SortifyUser tempUser = server.findUserByUsername(username);
			
			if(tempUser!=null) {
				tempUser = server.modifyUser(newUserData, tempUser);
				server.saveUser(tempUser);
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
