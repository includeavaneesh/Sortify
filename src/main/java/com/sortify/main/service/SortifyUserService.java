package com.sortify.main.service;

import java.util.ArrayList;


import com.sortify.main.model.SortifyUser;

public interface SortifyUserService {
	
	SortifyUser addUser(SortifyUser user);
	void deleteUser(String username);
	ArrayList<SortifyUser> retrieveAll();
	SortifyUser findUserByUsername(String username);
	SortifyUser modifyUser(String username, SortifyUser targetUser) throws Exception;
}
