package com.sortify.main.controller;

import com.drew.imaging.ImageProcessingException;
import com.sortify.main.model.SortifyFolder;
import com.sortify.main.model.SortifyImage;
import com.sortify.main.model.SortifySubFolder;
import com.sortify.main.model.SortifyUser;
import com.sortify.main.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/{username}")
public class SortifyStorageController {
	@Autowired
	private SortifyUserService USER_SERVICE;

	@Autowired
	private SortifyFolderService FOLDER_SERVICE;

	@Autowired
	private SortifySubFolderService SUBFOLDER_SERVICE;

	@Autowired
	private SortifyImageClustering CLUSTER_SERVICE;
	
	@Autowired
	private SortifyCloudStorageService CLOUD_SERVICE;

	/**
	 * This function is just to test the landing of /{username}/file endpoint
	 * @param principal
	 * @return Current logged in user
	 */
	@GetMapping("/accessUser")
	public String signedInUser(Principal principal) {
		return "This is accessed by: " + principal.toString();
	}

	/**
	 * This function allows the user to upload his images on AWS S3 folder assigned to him.
	 * @param file
	 * @return HTTP Response 200 if uploaded successfully
	 */
	@PostMapping("/uploadImage")
	public ResponseEntity<String> upload(@RequestParam(value="file") MultipartFile file, @PathVariable String username) throws ImageProcessingException, IOException {
		return new ResponseEntity<>(CLOUD_SERVICE.uploadFile(file, username),HttpStatus.OK);
	}

	/**
	 * This function allows the user to download their uploaded images from the cloud storage.
	 * @param fileName
	 * @return Image File requested
	 */
	@GetMapping("/downloadImage")
	public ResponseEntity<ByteArrayResource> download(@RequestParam String fileName, @PathVariable String username) {
		byte[] data = CLOUD_SERVICE.downloadFile(fileName, username);
		ByteArrayResource resource = new ByteArrayResource(data);

		return ResponseEntity
				.status(HttpStatus.OK)
				.contentLength(data.length)
				.header("Content-type","application/octet-stream")
				.header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
				.body(resource);
	}

	/**
	 * This function allows the user to delete any of their uploaded images from the cloud storage.
	 * @param fileName
	 * @return HTTP Response 200 if deleted successfully
	 */
	@GetMapping("/deleteImage")
	public ResponseEntity<String> delete(@RequestParam String fileName, @PathVariable String username) {
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(CLOUD_SERVICE.deleteFile(fileName, username));
	}

	/**
	 * This function allows the user to add subfolders in his account. (Note: this will be automated later)
	 * @param username
	 * @return Currently added folder
	 */
	@GetMapping("/addFolder")
	public ResponseEntity<?> createFolder(@PathVariable String username) {
		SortifyUser user = USER_SERVICE.findUserByUsername(username);

		if(user == null) {
			return ResponseEntity
					.status(HttpStatus.NOT_FOUND)
					.body("User with the username: " + username + " does not exist");
		}

		SortifyFolder parentFolder = user.getParentFolder();
		if(SUBFOLDER_SERVICE.findSubFolder(parentFolder.getFolderId() + "_0") != null) {
			return ResponseEntity
					.status(HttpStatus.CONFLICT)
					.body("Folder already exists");
		}

		SortifySubFolder subFolder = new SortifySubFolder();
		subFolder.setSubFolderId(parentFolder.getFolderId() + "_0");
		subFolder.setSubFolderName(parentFolder.getFolderId() + "_0");
		subFolder.setParentFolder(parentFolder);
		parentFolder.addSubFolder(subFolder);
		FOLDER_SERVICE.addFolder(parentFolder);

		return ResponseEntity
				.ok()
				.body(SUBFOLDER_SERVICE.findSubFolder(subFolder.getSubFolderId()));
	}

	@GetMapping("/allFolder")
	public ResponseEntity<?> getAllSubFolder(@PathVariable String username) {
		return ResponseEntity.ok().body(FOLDER_SERVICE.getAllSubFolder(username));
	}

	@GetMapping("/getFolder")
	public ResponseEntity<?> getSubFolder(@RequestBody SortifySubFolder subFolder, @PathVariable String username) {
		String subFolderId = subFolder.getSubFolderId();
		SortifySubFolder retrievedSubFolder = SUBFOLDER_SERVICE.findSubFolder(subFolderId);
		String parentFolderAccessedBySubFolder = retrievedSubFolder.getParentFolder().getFolderId();
		String parentFolderAccessedByUser = USER_SERVICE.findUserByUsername(username).getParentFolder().getFolderId();

		if(parentFolderAccessedBySubFolder.equals(parentFolderAccessedByUser)){
			return ResponseEntity.ok().body(SUBFOLDER_SERVICE.findSubFolder(subFolderId).getImageList());
		}
		else{
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@GetMapping("/getPhotos")
	public ResponseEntity<?> getAllPhotos(@PathVariable String username) {
		List<SortifyImage> imageList = new ArrayList<>();
		SortifyFolder folder = FOLDER_SERVICE.findFolder(username);
		List<SortifySubFolder> subFolderList = folder.getSubFolders();

		for(SortifySubFolder subFolder : subFolderList) {
			imageList.addAll(subFolder.getImageList());
		}

		CLUSTER_SERVICE.clusterImages(imageList, username);

		return ResponseEntity
				.ok()
				.body(FOLDER_SERVICE.findFolder(username).getSubFolders());
    }

	@GetMapping("/listPhotos")
	public List<SortifyImage> listPhotos(@PathVariable String username) {
		String folderId = USER_SERVICE.findUserByUsername(username).getParentFolder().getFolderId();
		return SUBFOLDER_SERVICE.getAllPhotos(folderId);
	}

}
