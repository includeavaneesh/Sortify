package com.sortify.main.service;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import ch.qos.logback.classic.Logger;
import com.amazonaws.HttpMethod;
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
@Slf4j
public class CloudStorageService implements SortifyCloudStorageService {

	@Value("${s3.bucket}")
	private String S3_BUCKET_NAME;
	
	@Autowired
	private AmazonS3 S3_CLOUD_CLIENT;

	@Autowired
	private SortifyUserService USER_SERVICE;

	@Autowired
	private SortifySubFolderService SUBFOLDER_SERVICE;

	@Autowired
	private SortifyImageService IMAGE_SERVICE;
	
	@Override
	public String uploadFile(MultipartFile file, String username) throws ImageProcessingException, IOException {
		SortifyFolder parentFolder = USER_SERVICE.findUserByUsername(username).getParentFolder();
		String parentFolderFolderId = parentFolder.getFolderId();

		double[] coordinates = getImageCoordinates(file);
		String imageFileName = file.getOriginalFilename();

		//		Rule: folderName_0 will be a sub folder assigned by default upon sign up.
		//		This must be not deleted in any case.
		String subFolderId = parentFolderFolderId + "_0";
		SortifySubFolder subFolder = SUBFOLDER_SERVICE.findSubFolder(subFolderId);

		SortifyImage image = new SortifyImage();
		image.setFileName(username + "/" + imageFileName);
		image.setGeoLocationX(coordinates[0]);
		image.setGeoLocationY(coordinates[1]);
		image.setDateCreated(new Date());
		image.setFileType("jpg");
		image.setSubFolder(subFolder);

		IMAGE_SERVICE.saveImage(image);

		File uploadObject = toFile(file);
		String fileName = parentFolderFolderId + "/" + imageFileName;

		S3_CLOUD_CLIENT.putObject(new PutObjectRequest(S3_BUCKET_NAME, fileName, uploadObject));

		boolean isDeleted = uploadObject.delete();
		if(!isDeleted) {
			return "File could not be uploaded: " + fileName;
		}
		return "File uploaded successfully: " + fileName;
		
	}

	@Override
	public byte[] downloadFile(String fileName, String username) {
		SortifyFolder parentFolder = USER_SERVICE.findUserByUsername(username).getParentFolder();
		String parentFolderFolderId = parentFolder.getFolderId();
		S3Object s3Obj = S3_CLOUD_CLIENT.getObject(S3_BUCKET_NAME, parentFolderFolderId + "/" + fileName);
		S3ObjectInputStream inputStream = s3Obj.getObjectContent();
		try {
            return IOUtils.toByteArray(inputStream);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		
		return null;
	}
	
	@Override
	public String deleteFile(String fileName, String username) {
		String sqlImageID = username + "/" + fileName;
		if(IMAGE_SERVICE.findImage(sqlImageID) == null) {
			return "The file:" + fileName + " does not exist";
		}

		IMAGE_SERVICE.deleteImage(sqlImageID);
		String folderName = USER_SERVICE.findUserByUsername(username).getParentFolder().getFolderId();
		S3_CLOUD_CLIENT.deleteObject(S3_BUCKET_NAME, folderName + "/" + fileName);
		return "Removed: " + fileName;
	}

	@Override
	public List<String> getAllFile(String folderName) {
		S3Objects objects = S3Objects.withPrefix(S3_CLOUD_CLIENT, S3_BUCKET_NAME, folderName + "/");
		objects.withDelimiter("/");
		List<String> userFiles = new ArrayList<>();
		objects.forEach((S3ObjectSummary objectSummary) -> {
		    System.out.println(objectSummary.getKey());
			userFiles.add(objectSummary.getKey());
		});

		return userFiles;
				
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

	public boolean keyExists(String fileName, String userFolderName) {
		List<String> userFiles = getAllFile(userFolderName);
		return userFiles.contains(fileName);
	}

	public URL generatePresignedURL(String fileName) {
//		Filename must be user folder + file name
		//		Get pre signed URL
		Date expiration = new Date();
		long expTimeMillis = expiration.getTime();

		expTimeMillis += 1000 * 60 * 60; // 1 hour timeout
		expiration.setTime(expTimeMillis);

		GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(S3_BUCKET_NAME, fileName)
				.withMethod(HttpMethod.GET)
				.withExpiration(expiration);

		URL s3URL = S3_CLOUD_CLIENT.generatePresignedUrl(generatePresignedUrlRequest);
		return s3URL;
	}
}
