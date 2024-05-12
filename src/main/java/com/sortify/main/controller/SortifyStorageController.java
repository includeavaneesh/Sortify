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
	private SortifyImageService IMAGE_SERVICE;
	
	@Autowired
	private SortifyCloudStorageService CLOUD_SERVICE;

	/**
	 * This function is just to test the landing of /{username}/file endpoint
	 * @param principal
	 * @return Current logged in user
	 */
	@GetMapping("/accessUser")
	public String testPage(Principal principal) {
		return "This is accessed by: " + principal.toString();
	}

	/**
	 * This function allows the user to upload his images on AWS S3 folder assigned to him.
	 * @param file
	 * @return HTTP Response 200 if uploaded successfully
	 */
	@PostMapping("/uploadImage")
	public ResponseEntity<String> upload(@RequestParam(value="file") MultipartFile file, @PathVariable String username) throws ImageProcessingException, IOException {
		String folderName = USER_SERVICE.findUserByUsername(username).getParentFolder().getFolderId();
		return new ResponseEntity<>(CLOUD_SERVICE.uploadFile(file, folderName),HttpStatus.OK);
	}

	/**
	 * This function allows the user to download their uploaded images from the cloud storage.
	 * @param fileName
	 * @return Image File requested
	 */
	@GetMapping("/downloadImage")
	public ResponseEntity<ByteArrayResource> download(@RequestParam String fileName, @PathVariable String username) {

		String folderName = USER_SERVICE.findUserByUsername(username).getParentFolder().getFolderId();
		byte[] data = CLOUD_SERVICE.downloadFile(fileName, folderName);
		ByteArrayResource rsc = new ByteArrayResource(data);

		return ResponseEntity
				.status(HttpStatus.OK)
				.contentLength(data.length)
				.header("Content-type","application/octet-stream")
				.header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
				.body(rsc);
	}

	/**
	 * This function allows the user to delete any of their uploaded images from the cloud storage.
	 * @param fileName
	 * @return HTTP Response 200 if deleted successfully
	 */
	@GetMapping("/deleteImage")
	public ResponseEntity<String> delete(@RequestParam String fileName, @PathVariable String username) {
		String folderName = USER_SERVICE.findUserByUsername(username).getParentFolder().getFolderId();
		return new ResponseEntity<>(CLOUD_SERVICE.deleteFile(fileName, folderName), HttpStatus.OK);
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
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with the username: " + username + " does not exist");
		}

		SortifyFolder parentFolder = user.getParentFolder();
		if(SUBFOLDER_SERVICE.findSubFolder(parentFolder.getFolderId() + "_0") != null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Folder already exists");
		}
		SortifySubFolder subFolder = new SortifySubFolder();
		subFolder.setSubFolderId(parentFolder.getFolderId() + "_0");
		subFolder.setSubFolderName(parentFolder.getFolderId() + "_0");
		subFolder.setParentFolder(parentFolder);
		parentFolder.addSubFolder(subFolder);
		FOLDER_SERVICE.addFolder(parentFolder);

		return ResponseEntity.ok().body(SUBFOLDER_SERVICE.findSubFolder(subFolder.getSubFolderId()));
	}

	@GetMapping("/allFolder")
	public ResponseEntity<?> getAllSubFolder(@PathVariable String username) {
		String folderId = USER_SERVICE.findUserByUsername(username).getParentFolder().getFolderId();
		return ResponseEntity.ok().body(FOLDER_SERVICE.getAllSubFolder(folderId));
	}

	@GetMapping("/getFolder")
	public ResponseEntity<?> getSubFolder(@RequestBody SortifySubFolder subFolder) {
		String subFolderId = subFolder.getSubFolderId();
		SortifySubFolder retrievedSubFolder = SUBFOLDER_SERVICE.findSubFolder(subFolderId);
		if(retrievedSubFolder != null){
			return ResponseEntity.ok().body(SUBFOLDER_SERVICE.findSubFolder(subFolderId).getImageList());
		}
		else{
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

//	@GetMapping("/updateFolder")
//	public ResponseEntity<?> updateFolder(@RequestParam(name = "folder") String folderId, @RequestParam(name = "image") String imageId) {
//		SortifyImage image = IMAGE_SERVICE.updateImage(imageId, folderId);
//
//		return ResponseEntity.ok().body(image);
//	}


}
