package com.sortify.main.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


@Entity(name = "SORTIFY_PARENT_FOLDER")
public class SortifyFolder {

    @Id
    private String folderId;
    private String folderName;

    @JsonIgnore
    @OneToOne(mappedBy = "parentFolder")
    private SortifyUser user;

    @JsonIgnore
    @OneToMany(mappedBy = "parentFolder",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SortifySubFolder> subFolders = new ArrayList<SortifySubFolder>();

    public SortifyFolder() {

    }

    public SortifyFolder(String folderId, String folderName, SortifyUser user, List<SortifySubFolder> subFolders) {
        super();
        this.folderId = folderId;
        this.folderName = folderName;
        this.user = user;
//        this.subFolders = subFolders;
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

    public void addSubFolder(SortifySubFolder subFolder) {
        this.subFolders.add(subFolder);
    }

    @Override
    public String toString() {
        return "SortifyFolder{" +
                "folderId='" + folderId + '\'' +
                ", folderName='" + folderName + '\'' +
                ", user=" + user.getUsername() +
//                ", subFolders=" + subFolders +
                '}';
    }
}
