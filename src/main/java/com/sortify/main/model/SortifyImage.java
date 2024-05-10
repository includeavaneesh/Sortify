package com.sortify.main.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.apache.commons.math3.ml.clustering.Clusterable;

import java.util.Date;

@Entity(name = "SORTIFY_IMAGE")
public class SortifyImage implements Clusterable {

	@Id
	private String fileName;
	private String fileType;
	private double geoLocationX;
	private double geoLocationY;
	private String imageUrl;
	private Date dateCreated;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
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

	public SortifyImage(String fileName, String fileType) {
		super();
		this.fileName = fileName;
		this.fileType = fileType;
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


	@Override
	public double[] getPoint() {
		return new double[]{geoLocationX, geoLocationY};
	}
}
