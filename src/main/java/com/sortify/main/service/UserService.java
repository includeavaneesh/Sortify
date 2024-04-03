package com.sortify.main.service;

import java.util.ArrayList;

import com.sortify.main.repository.SortifyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import com.sortify.main.model.SortifyUser;
import org.springframework.stereotype.Service;

@Service
public class UserService implements SortifyUserService {

	@Autowired
	private SortifyUserRepository userRepo;

	@Override
	public SortifyUser addUser(SortifyUser user) {
		return userRepo.save(user);
	}
	
	@Override
	public void deleteUser(String username) {
		userRepo.deleteById(username);
	}
	
	@Override
	public ArrayList<SortifyUser> retrieveAll() {
		return (ArrayList<SortifyUser>) userRepo.findAll();
	}
	
	@Override
	public SortifyUser findUserByUsername(String username) {
		if(userRepo.findById(username).isPresent()) {
			return userRepo.findById(username).get();
		}

		return null;
	}
	
	@Override
	public SortifyUser modifyUser(String username, SortifyUser newUser) throws IllegalAccessException{
		SortifyUser oldUser = findUserByUsername(username);

		if(oldUser != null && newUser != null) {

			if(oldUser.getUsername()!=null && newUser.getUsername()!=null){
				oldUser.setUsername(newUser.getUsername());
			}

			if(oldUser.getUserFirstName()!=null && newUser.getUserFirstName()!=null) {
				oldUser.setUserFirstName(newUser.getUserFirstName());
			}

			if(oldUser.getUserLastName()!=null && newUser.getUserLastName()!=null) {
				oldUser.setUserLastName(newUser.getUserLastName());
			}

			if(oldUser.getPassword()!=null && newUser.getPassword()!=null) {
				oldUser.setPassword(newUser.getPassword());
			}

			if(oldUser.getUserLastName()!=null && newUser.getUserLastName()!=null) {
				oldUser.setUserLastName(newUser.getUserLastName());
			}

			userRepo.save(oldUser);
		}

		return oldUser;
		
	}
}
