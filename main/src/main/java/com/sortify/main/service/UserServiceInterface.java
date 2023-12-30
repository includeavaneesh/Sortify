package com.sortify.main.service;

import java.util.ArrayList;


import com.sortify.main.model.SortifyUser;

public interface UserServiceInterface {
	
	void addUser(SortifyUser user);
	void deleteUser(String username);
	ArrayList<SortifyUser> retrieveAll();
	SortifyUser findUserByUsername(String username);
	SortifyUser modifyUser(SortifyUser newUser, SortifyUser targetUser) throws Exception;
	void saveUser(SortifyUser user);
}
