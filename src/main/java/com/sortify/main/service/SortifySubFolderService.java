package com.sortify.main.service;

import com.sortify.main.model.SortifyImage;
import com.sortify.main.model.SortifySubFolder;

import java.util.ArrayList;
import java.util.List;

public interface SortifySubFolderService {
    void saveSubFolder(SortifySubFolder subFolder);
    void deleteSubFolder(String subFolderId);
    SortifySubFolder findSubFolder(String subFolderId);
    void deleteAllSubFolder(String folderId);
    void deleteClusteringFolder(String folderId);
    List<SortifyImage> getAllPhotos(String folderId);
}
