package com.sortify.main.service;

import com.sortify.main.model.SortifyFolder;
import com.sortify.main.model.SortifySubFolder;

import java.util.ArrayList;

public interface SortifyFolderService {
    void addFolder(SortifyFolder folder);
    void deleteFolder(String folderId);
    SortifyFolder findFolder(String username);
    ArrayList<SortifySubFolder> getAllSubFolder(String folderId);

}
