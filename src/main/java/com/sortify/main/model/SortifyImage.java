package com.sortify.main.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class SortifyImage {

	@Id
	private String fileName;
	private String fileType;
	private double geoLocationX;
	private double geoLocationY;
	private String imageUrl;
	private Date dateCreated;
	@ManyToOne
	@JoinColumn(name = "subFolderId")
	private SortifySubFolder subFolder;

	public SortifyImage() {
		this(null,null,0.0,0.0,null,null,null);
	}
	public SortifyImage(String fileName, String fileType, double geoLocationX, double geoLocationY, String imageUrl, Date dateCreated, SortifySubFolder subFolder) {
		super();
		this.fileName = fileName;
		this.fileType = fileType;
		this.geoLocationX = geoLocationX;
		this.geoLocationY = geoLocationY;
		this.imageUrl = imageUrl;
		this.dateCreated = dateCreated;
		this.subFolder = subFolder;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public double getGeoLocationX() {
		return geoLocationX;
	}

	public void setGeoLocationX(double geoLocationX) {
		this.geoLocationX = geoLocationX;
	}

	public double getGeoLocationY() {
		return geoLocationY;
	}

	public void setGeoLocationY(double geoLocationY) {
		this.geoLocationY = geoLocationY;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public SortifySubFolder getSubFolder() {
		return subFolder;
	}

	public void setSubFolder(SortifySubFolder subFolder) {
		this.subFolder = subFolder;
	}

	@Override
	public String toString() {
		return "SortifyImage{" +
				"fileName='" + fileName + '\'' +
				", fileType='" + fileType + '\'' +
				", geoLocationX=" + geoLocationX +
				", geoLocationY=" + geoLocationY +
				", imageUrl='" + imageUrl + '\'' +
				", dateCreated=" + dateCreated +
				", subFolder=" + subFolder +
				'}';
	}


}
