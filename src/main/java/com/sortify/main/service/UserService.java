package com.sortify.main.service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Component;

import com.sortify.main.model.SortifyUser;

@Component
public class UserService implements UserServiceInterface{
	
	private static List<SortifyUser> userList = new ArrayList<SortifyUser>();
	
	@Override
	public void addUser(SortifyUser user) {
		Iterator<SortifyUser> iter = userList.iterator();
		
		while(iter.hasNext()) {
			SortifyUser tempUser = iter.next();
			if(tempUser.getUsername().equals(user.getUsername())) {
				return;
			}
		}
		
		userList.add(user);
	}
	
	@Override
	public void deleteUser(String username) {
		Iterator<SortifyUser> iter = userList.iterator();
		
		while(iter.hasNext()) {
			SortifyUser tempUser = iter.next();
			if(tempUser.getUsername().equals(username)) {
				iter.remove();
				return;
			}
		}
	}
	
	@Override
	public ArrayList<SortifyUser> retrieveAll() {
		return (ArrayList<SortifyUser>) userList;
	}
	
	@Override
	public SortifyUser findUserByUsername(String username) {
		Iterator<SortifyUser> iter = userList.iterator();
		while(iter.hasNext()) {
			SortifyUser tempUser = iter.next();
			if(tempUser.getUsername().equals(username)) {
				
				return tempUser;
			}
		}
		
		return null;
	}
	
	@Override
	public SortifyUser modifyUser(SortifyUser newUser, SortifyUser targetUser) throws IllegalAccessException{
		Class<?> userClass = SortifyUser.class;
		Field[] userFields = userClass.getDeclaredFields();
		
		for(Field field : userFields) {
			field.setAccessible(true);
			
			Object value = field.get(newUser);
			
			if(value!=null) {
				field.set(targetUser, value);
			}
			
			field.setAccessible(false);
		}
		return targetUser;
		
	}
	
	@Override
	public void saveUser(SortifyUser user) {
		Iterator<SortifyUser> iter = userList.iterator();
		while(iter.hasNext()) {
			SortifyUser tempUser = iter.next();
			if(tempUser.getUsername().equals(user.getUsername())) {
				tempUser = user;
			}
		}
		
		return;
		
	}
}
