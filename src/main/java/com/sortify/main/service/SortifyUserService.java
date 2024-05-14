package com.sortify.main.service;

import java.util.ArrayList;


import com.sortify.main.model.SortifyUser;
import org.springframework.data.domain.Page;

public interface SortifyUserService {
	
	SortifyUser addUser(SortifyUser user);
	void deleteUser(String username);
	ArrayList<SortifyUser> retrieveAll();
	Page<SortifyUser> getPaginatedUsers(int page, int size);
	SortifyUser findUserByUsername(String username);
	SortifyUser modifyUser(String username, SortifyUser newUser) throws Exception;
}
