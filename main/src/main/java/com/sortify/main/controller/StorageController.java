package com.sortify.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.sortify.main.service.CloudStorageService;

@RestController
@RequestMapping("/file")
public class StorageController {
	
	@Autowired
	private CloudStorageService storageService;
	
	@PostMapping("/upload")
	public ResponseEntity<String> upload(@RequestParam(value="file") MultipartFile file) {
		return new ResponseEntity<>(storageService.uploadFile(file),HttpStatus.OK);
				
	}
	
	@GetMapping("/download/{fileName}")
	public ResponseEntity<ByteArrayResource> download(@PathVariable String fileName) {
		byte[] data = storageService.downloadFile(fileName);
		ByteArrayResource rsc = new ByteArrayResource(data);
		
		return ResponseEntity
				.status(HttpStatus.OK)
				.contentLength(data.length)
				.header("Content-type","application/octet-stream")
				.header("Contnt-disposition", "attachment; filename=\"" + fileName + "\"")
				.body(rsc);
	}
	
	@DeleteMapping("/delete/{fileName}")
	public ResponseEntity<String> delete(@PathVariable String fileName) {
		return new ResponseEntity<>(storageService.deleteFile(fileName),HttpStatus.OK);
	}
}
