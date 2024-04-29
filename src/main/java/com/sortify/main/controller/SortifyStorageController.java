package com.sortify.main.controller;

import com.drew.imaging.ImageProcessingException;
import com.sortify.main.model.SortifyFolder;
import com.sortify.main.model.SortifySubFolder;
import com.sortify.main.model.SortifyUser;
import com.sortify.main.service.SortifyFolderService;
import com.sortify.main.service.SortifyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.sortify.main.service.CloudStorageService;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/{username}/file")
public class SortifyStorageController {
	@Autowired
	private SortifyUserService userService;

	@Autowired
	private SortifyFolderService sortifyFolderService;
	
	@Autowired
	private CloudStorageService storageService;

	/**
	 * This function is just to test the landing of /{username}/file endpoint
	 * @param principal
	 * @return Current logged in user
	 */
	@GetMapping("")
	public String testPage(Principal principal) {
		return "This is accessed by: " + principal.toString();
	}

	/**
	 * This function allows the user to upload his images on AWS S3 folder assigned to him.
	 * @param file
	 * @return HTTP Response 200 if uploaded successfully
	 */
	@PostMapping("/upload")
	public ResponseEntity<String> upload(@RequestParam(value="file") MultipartFile file, @PathVariable String username) throws ImageProcessingException, IOException {
		String folderName = userService.findUserByUsername(username).getParentFolder().getFolderId();
		return new ResponseEntity<>(storageService.uploadFile(file, folderName),HttpStatus.OK);
	}

	/**
	 * This function allows the user to download their uploaded images from the cloud storage.
	 * @param fileName
	 * @return Image File requested
	 */
	@GetMapping("/download/{fileName}")
	public ResponseEntity<ByteArrayResource> download(@PathVariable String fileName, @PathVariable String username) {

		String folderName = userService.findUserByUsername(username).getParentFolder().getFolderId();
		byte[] data = storageService.downloadFile(fileName, folderName);
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
	@GetMapping("/delete/{fileName}")
	public ResponseEntity<String> delete(@PathVariable String fileName, @PathVariable String username) {
		String folderName = userService.findUserByUsername(username).getParentFolder().getFolderId();
		return new ResponseEntity<>(storageService.deleteFile(fileName, folderName), HttpStatus.OK);
	}

	/**
	 * This function allows the user to add subfolders in his account. (Note: this will be automated later)
	 * @param username
	 * @return Currently added folder
	 */
	@GetMapping("/addFolder")
	public String createFolder(@PathVariable String username) {
		SortifyUser user = userService.findUserByUsername(username);
		SortifyFolder parentFolder = user.getParentFolder();


		SortifySubFolder subFolder = new SortifySubFolder();
		subFolder.setSubFolderId("test2");
		subFolder.setParentFolder(parentFolder);
		subFolder.setSubFolderName("testingName2");

		parentFolder.addSubFolder(subFolder);

		sortifyFolderService.addFolder(parentFolder);
		return  parentFolder.getSubFolders().get(0).toString();
	}



}
