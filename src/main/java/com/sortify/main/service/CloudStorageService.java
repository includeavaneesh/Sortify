package com.sortify.main.service;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.iterable.S3Objects;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.amazonaws.services.s3.model.*;


@Service
public class CloudStorageService {
	
	@Value("${s3.bucket}")
	private String s3BucketName;
	
	@Autowired
	private AmazonS3 cloudClient;
	
	public String uploadFile(MultipartFile file) {
		File uploadObject = toFile(file);
		String fileName = "test/" + file.getOriginalFilename();
		cloudClient.putObject(new PutObjectRequest(s3BucketName, fileName, uploadObject));
		uploadObject.delete();
		return "File uploaded: " + fileName;
		
	}
	
	private File toFile(MultipartFile file) {
		File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
		
		try (FileOutputStream fos = new FileOutputStream(convertedFile)){
			fos.write(file.getBytes());

		}
		catch(IOException e){
			System.out.println("Error in file");
		}
		return convertedFile;
	}
	
	public byte[] downloadFile(String fileName) {
		S3Object s3Obj = cloudClient.getObject(s3BucketName,"photos/"+fileName);
		S3ObjectInputStream inputStream = s3Obj.getObjectContent();
		try {
            return IOUtils.toByteArray(inputStream);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		
		return null;
	}
	
	public String deleteFile(String fileName) {
		cloudClient.deleteObject(s3BucketName, fileName);
		return "Removed: " + fileName;
	}
	
	//temp func
	public void getAllFile() {
		//ListObjectsRequest listObjectRequest = new ListObjectsRequest().withBucketName(s3BucketName).withDelimiter("/").withPrefix("photos/");
		S3Objects objects = S3Objects.withPrefix(cloudClient, s3BucketName,"photos/");
		objects.withDelimiter("/");
		
		objects.forEach((S3ObjectSummary objectSummary) -> {
		    // TODO: Consume `objectSummary` the way you need
		    System.out.println(objectSummary.getKey());
		});
				
	}

	/**
	 * This function creates the main folder of the user
	 */
	public void createUserFolder(String userFolderName) {
		// Assign prefix
		userFolderName += "/";

		// Initiate with null
		ObjectMetadata folderMetadata = new ObjectMetadata();
		folderMetadata.setContentLength(0);
		InputStream emptyContent = new ByteArrayInputStream(new byte[0]);

		// put folder as key in S3 bucket
		cloudClient.putObject(s3BucketName,userFolderName,emptyContent,folderMetadata);
    }

	/**
	 * This function deletes a bucket
	 */
	public void deleteUserFolder(String userFolderName) {
		userFolderName += "/";
		ObjectListing objectListing = cloudClient.listObjects(s3BucketName, userFolderName);
		List<DeleteObjectsRequest.KeyVersion> keys = new ArrayList<>();
		for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
			keys.add(new DeleteObjectsRequest.KeyVersion(objectSummary.getKey()));
		}
		if (!keys.isEmpty()) {
			DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(s3BucketName)
					.withKeys(keys)
					.withQuiet(false);
			cloudClient.deleteObjects(deleteObjectsRequest);
		}
		cloudClient.deleteObject(s3BucketName,userFolderName);
	}
}
