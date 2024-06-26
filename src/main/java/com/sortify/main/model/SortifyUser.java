package com.sortify.main.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;


@Entity
@Table(name = "SORTIFY_USER")
public class SortifyUser {
	@Id
	@Column(nullable = false)
	private String username;
	@Column(nullable = false)
	private String password;
	@Column(nullable = false)
	private String userFirstName;

	private String userLastName;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinTable(name="User_Folder",
	joinColumns = @JoinColumn(name = "username"),
			inverseJoinColumns = @JoinColumn(name = "folder_id")
	)
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private SortifyFolder parentFolder;

	public SortifyUser() {

	}

	public SortifyUser(String username, String password, String userFirstName, String userLastName, MultipartFile profilePhoto, SortifyFolder parentFolder) {
		super();

		this.username = username;
		this.password = password;
		this.userFirstName = userFirstName;
		this.userLastName = userLastName;
		this.parentFolder = parentFolder;
	}


	public SortifyUser(SortifyUser user) {
		super();

		this.username = user.username;
		this.userFirstName = user.userFirstName;
		this.userLastName = user.userLastName;
		this.password = user.password;
		this.parentFolder = user.parentFolder;
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

	public String getPassword() {
		return password;
	}

	public SortifyFolder getParentFolder() {
		return parentFolder;
	}

	public void setParentFolder(SortifyFolder parentFolder) {
		this.parentFolder = parentFolder;
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

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "SortifyUser{" +
				"username='" + username + '\'' +
				", password='" + password + '\'' +
				", userFirstName='" + userFirstName + '\'' +
				", userLastName='" + userLastName + '\'' +
				", parentFolder=" + parentFolder +
				'}';
	}

	private File toFile(MultipartFile file) {
		File convertedFile;
		if(file != null) {
			 convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename(),"File name cannot be null"));
		}
		else{
			convertedFile = new File("src/main/resources/default/defaultProfilePhoto.png");
		}

		try (FileOutputStream fos = new FileOutputStream(convertedFile)){
			fos.write(file.getBytes());
			fos.close();
		}
		catch(IOException e){
			System.out.println("Error in file");
		}
		return convertedFile;
	}
	private byte[] getProfilePhotoInBytes(MultipartFile file) {
		File convertedFile = toFile(file);
		byte[] profilePhotoImageData = new byte[(int) convertedFile.length()];

		try (FileInputStream fileInputStream = new FileInputStream(convertedFile)) {

			int readStatus = fileInputStream.read(profilePhotoImageData);

        }
		catch (Exception e) {
			e.printStackTrace();
		}

		return profilePhotoImageData;
	}
	
}
