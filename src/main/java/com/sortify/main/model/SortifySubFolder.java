package com.sortify.main.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class SortifySubFolder {
    @Id
    @Column(nullable = false)
    private String subFolderId;
    @Column(nullable = false)
    private String subFolderName;

    @ManyToOne
    @JoinColumn(name = "folderId")
    @Column(nullable = false)
    private SortifyFolder parentFolder;

    @OneToMany(mappedBy = "subFolder")
    private List<SortifyImage> imageList;

    public SortifySubFolder() {
        this(null,null,null,null);
    }
    public SortifySubFolder(String subFolderId, String subFolderName, SortifyFolder parentFolder, List<SortifyImage> imageList) {
        super();
        this.subFolderId = subFolderId;
        this.subFolderName = subFolderName;
        this.parentFolder = parentFolder;
        this.imageList = imageList;
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
}
