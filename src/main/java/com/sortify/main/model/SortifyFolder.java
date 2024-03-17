package com.sortify.main.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class SortifyFolder {

    @Id
    private String folderId;
    private String folderName;

    @OneToOne
    @JoinColumn(name = "username")
    private SortifyUser user;
    @OneToMany(mappedBy = "parentFolder")
    private List<SortifySubFolder> subFolders;

    public SortifyFolder() {
        this(null,null,null,null);
    }

    public SortifyFolder(String folderId, String folderName, SortifyUser user, List<SortifySubFolder> subFolders) {
        super();
        this.folderId = folderId;
        this.folderName = folderName;
        this.user = user;
        this.subFolders = subFolders;
    }

    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public SortifyUser getUser() {
        return user;
    }

    public void setUser(SortifyUser user) {
        this.user = user;
    }

    public List<SortifySubFolder> getSubFolders() {
        return subFolders;
    }

    public void setSubFolders(List<SortifySubFolder> subFolders) {
        this.subFolders = subFolders;
    }

    @Override
    public String toString() {
        return "SortifyFolder{" +
                "folderId='" + folderId + '\'' +
                ", folderName='" + folderName + '\'' +
                ", user=" + user +
                ", subFolders=" + subFolders +
                '}';
    }
}
