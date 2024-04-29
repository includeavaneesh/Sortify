package com.sortify.main.service;

import java.io.*;
import java.util.*;

import ch.qos.logback.classic.Logger;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.GpsDirectory;
import com.sortify.main.model.SortifyFolder;
import com.sortify.main.model.SortifyUser;
import lombok.extern.slf4j.Slf4j;
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
public class CloudStorageService implements SortifyCloudStorageService {

	public static Logger log;

	@Value("${s3.bucket}")
	private String s3BucketName;
	
	@Autowired
	private AmazonS3 cloudClient;
	
	@Override
	public String uploadFile(MultipartFile file, String folderName) throws ImageProcessingException, IOException {
		double[] coordinates = getImageCoordinates(file);
		File uploadObject = toFile(file);
		String fileName = folderName + "/" + file.getOriginalFilename();
		cloudClient.putObject(new PutObjectRequest(s3BucketName, fileName, uploadObject));

		uploadObject.delete();
		return "File uploaded: " + fileName;
		
	}

	@Override
	public byte[] downloadFile(String fileName, String folderName) {
		S3Object s3Obj = cloudClient.getObject(s3BucketName, folderName + "/" + fileName);
		S3ObjectInputStream inputStream = s3Obj.getObjectContent();
		try {
            return IOUtils.toByteArray(inputStream);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		
		return null;
	}
	
	@Override
	public String deleteFile(String fileName, String folderName) {
		cloudClient.deleteObject(s3BucketName, folderName + "/" + fileName);
		return "Removed: " + fileName;
	}
	
	//temp func
	@Override
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
	@Override
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
	@Override
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

	@Override
	public void createUserSubFolder(SortifyUser user) {
		SortifyFolder parentFolder = user.getParentFolder();
		String subFolder = "testSubFolder";
	}
}
