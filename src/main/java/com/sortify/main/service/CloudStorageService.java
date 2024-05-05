package com.sortify.main.service;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import ch.qos.logback.classic.Logger;
import com.drew.imaging.ImageProcessingException;
import com.sortify.main.model.SortifyFolder;
import com.sortify.main.model.SortifyImage;
import com.sortify.main.model.SortifySubFolder;
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
	private String S3_BUCKET_NAME;
	
	@Autowired
	private AmazonS3 S3_CLOUD_CLIENT;

	@Autowired
	private SortifySubFolderService SUBFOLDER_SERVICE;

	@Autowired
	private SortifyImageService IMAGE_SERVICE;
	
	@Override
	public String uploadFile(MultipartFile file, String folderName) throws ImageProcessingException, IOException {
		double[] coordinates = getImageCoordinates(file);
		String imageFileName = file.getOriginalFilename();

		String subFolderId = "testFolder";
		SortifySubFolder subFolder = SUBFOLDER_SERVICE.findSubFolder(subFolderId);

		SortifyImage image = new SortifyImage();
		image.setFileName(imageFileName);
		image.setGeoLocationX(coordinates[0]);
		image.setGeoLocationX(coordinates[1]);
		image.setDateCreated(new Date());
		image.setFileType("jpg");
		image.setSubFolder(subFolder);

		IMAGE_SERVICE.saveImage(image);

		File uploadObject = toFile(file);
		String fileName = folderName + "/" + imageFileName;

		S3_CLOUD_CLIENT.putObject(new PutObjectRequest(S3_BUCKET_NAME, fileName, uploadObject));
		URL s3URL = S3_CLOUD_CLIENT.getUrl(S3_BUCKET_NAME, fileName);
		boolean isDeleted = uploadObject.delete();
		if(!isDeleted) {
			return "File could not be uploaded: " + fileName;
		}
		return "File uploaded successfully: " + fileName + " at " + s3URL;
		
	}

	@Override
	public byte[] downloadFile(String fileName, String folderName) {
		S3Object s3Obj = S3_CLOUD_CLIENT.getObject(S3_BUCKET_NAME, folderName + "/" + fileName);
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
		S3_CLOUD_CLIENT.deleteObject(S3_BUCKET_NAME, folderName + "/" + fileName);
		return "Removed: " + fileName;
	}
	
	//temp func
	@Override
	public void getAllFile() {
		//ListObjectsRequest listObjectRequest = new ListObjectsRequest().withBucketName(s3BucketName).withDelimiter("/").withPrefix("photos/");
		S3Objects objects = S3Objects.withPrefix(S3_CLOUD_CLIENT, S3_BUCKET_NAME,"photos/");
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
		S3_CLOUD_CLIENT.putObject(S3_BUCKET_NAME,userFolderName,emptyContent,folderMetadata);
    }

	/**
	 * This function deletes a bucket
	 */
	@Override
	public void deleteUserFolder(String userFolderName) {
		userFolderName += "/";
		ObjectListing objectListing = S3_CLOUD_CLIENT.listObjects(S3_BUCKET_NAME, userFolderName);
		List<DeleteObjectsRequest.KeyVersion> keys = new ArrayList<>();
		for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
			keys.add(new DeleteObjectsRequest.KeyVersion(objectSummary.getKey()));
		}
		if (!keys.isEmpty()) {
			DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(S3_BUCKET_NAME)
					.withKeys(keys)
					.withQuiet(false);
			S3_CLOUD_CLIENT.deleteObjects(deleteObjectsRequest);
		}
		S3_CLOUD_CLIENT.deleteObject(S3_BUCKET_NAME,userFolderName);
	}

	@Override
	public void createUserSubFolder(SortifyUser user) {
		SortifyFolder parentFolder = user.getParentFolder();
		String subFolder = "testSubFolder";
	}
}
