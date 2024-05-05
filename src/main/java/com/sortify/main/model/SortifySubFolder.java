package com.sortify.main.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "SORTIFY_CHILD_FOLDER")
public class SortifySubFolder {
    @Id
    private String subFolderId;

    private String subFolderName;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "folderId")
    private SortifyFolder parentFolder;

    @JsonIgnore
    @OneToMany(mappedBy = "subFolder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SortifyImage> imageList = new ArrayList<>();


    public SortifySubFolder() {
        this(null,null,null);
    }
    public SortifySubFolder(String subFolderId, String subFolderName, SortifyFolder parentFolder) {
        super();
        this.subFolderId = subFolderId;
        this.subFolderName = subFolderName;
        this.parentFolder = parentFolder;

    }

    public String getSubFolderId() {
        return subFolderId;
    }

    public void setSubFolderId(String subFolderId) {
        this.subFolderId = subFolderId;
    }

    public String getSubFolderName() {
        return subFolderName;
    }

    public void setSubFolderName(String subFolderName) {
        this.subFolderName = subFolderName;
    }

    public SortifyFolder getParentFolder() {
        return parentFolder;
    }

    public void setParentFolder(SortifyFolder parentFolder) {
        this.parentFolder = parentFolder;
    }

    public List<SortifyImage> getImageList() {
        return imageList;
    }

    public void setImageList(List<SortifyImage> imageList) {
        this.imageList = imageList;
    }

    @Override
    public String toString() {
        return "SortifySubFolder{" +
                "subFolderId='" + subFolderId + '\'' +
                ", subFolderName='" + subFolderName + '\'' +
                '}';
    }
}
