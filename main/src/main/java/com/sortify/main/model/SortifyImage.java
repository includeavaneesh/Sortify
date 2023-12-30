package com.sortify.main.model;

import java.util.Arrays;
import java.util.Date;

public class SortifyImage {
	
	private String fileName;
	private String fileType;
	private double [] geoLocation;
	private Date dateCreated;
	
	public SortifyImage() {
		this(null, null, null, null);
	}
	
	public SortifyImage(String fileName, String fileType, double[] geoLocation, Date dateCreated) {
		super();
		this.fileName = fileName;
		this.fileType = fileType;
		this.geoLocation = new double[2];
		this.dateCreated = dateCreated;
	}

	public String getFileName() {
		return fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public double[] getGeoLocation() {
		return geoLocation;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public void setGeoLocation(double[] geoLocation) {
		this.geoLocation = geoLocation;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	@Override
	public String toString() {
		return "SortifyImage [fileName=" + fileName + ", fileType=" + fileType + ", geoLocation="
				+ Arrays.toString(geoLocation) + ", dateCreated=" + dateCreated + "]";
	}
	
	
	
	
}
