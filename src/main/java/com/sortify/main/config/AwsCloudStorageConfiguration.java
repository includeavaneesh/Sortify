package com.sortify.main.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class AwsCloudStorageConfiguration {
	
	
	@Value("${s3.access}")
	private String ACCESS_KEY;
	
	@Value("${s3.secret}")
	private String SECRET_KEY;
	
	@Value("${s3.region}")
	private String REGION;
	
	@Bean
	public AmazonS3 generateS3Client() {
		AWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
		AWSStaticCredentialsProvider credProvider = new AWSStaticCredentialsProvider(credentials);
		return AmazonS3ClientBuilder.
				standard().
				withCredentials(credProvider).
				withRegion(REGION).build();
	}
}
