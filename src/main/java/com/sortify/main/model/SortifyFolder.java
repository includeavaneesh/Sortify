package com.sortify.main.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;

@Entity
public class SortifyFolder {

    @Id
    private String folderId;

    private String username;
    private String folderName;
}
