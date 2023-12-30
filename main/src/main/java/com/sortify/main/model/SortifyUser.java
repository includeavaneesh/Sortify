package com.sortify.main.model;

import java.util.ArrayList;
import java.util.List;

public class SortifyUser {
	private String username;
	private String userFirstName;
	private String userLastName;
	private List<SortifyImage> imageList;
	
	public SortifyUser() {
		this(null,null,null);
	}
	public SortifyUser(String username, String userFirstName) {
		this(username, userFirstName, null);
		this.imageList.add(new SortifyImage("Test",".jpeg",null,null));
	}
	
	public SortifyUser(String username, String userFirstName, String userLastName) {
		super();
		this.imageList = new ArrayList<SortifyImage>();
		this.username = username;
		this.userFirstName = userFirstName;
		this.userLastName = userLastName;
	}
	
	public SortifyUser(SortifyUser user) {
		super();
		this.imageList = user.imageList;
		this.username = user.username;
		this.userFirstName = user.userFirstName;
		this.userLastName = user.userLastName;
	}

	public String getUsername() {
		return username;
	}

	public String getUserFirstName() {
		return userFirstName;
	}

	public String getUserLastName() {
		return userLastName;
	}

	public List<SortifyImage> getImageList() {
		return imageList;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}

	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
	}

	public void setImageList(List<SortifyImage> imageList) {
		this.imageList = imageList;
	}

	@Override
	public String toString() {
		return "SortifyUser [username=" + username + ", userFirstName=" + userFirstName + ", userLastName="
				+ userLastName + ", imageList=" + imageList + "]";
	}

	
	
	
	
	
	
	
}
