package com.sortify.main.service;

import com.sortify.main.model.SortifySubFolder;

import java.util.ArrayList;

public interface SortifySubFolderService {
    void saveSubFolder(SortifySubFolder subFolder);
    void deleteSubFolder(String subFolderId);
    SortifySubFolder findSubFolder(String subFolderId);
    ArrayList<SortifySubFolder> getAllSubFolder(String folderId);
    void deleteAllSubFolder(String folderId);
}
