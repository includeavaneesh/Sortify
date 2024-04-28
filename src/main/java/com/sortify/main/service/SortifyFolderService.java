package com.sortify.main.service;

import com.sortify.main.model.SortifyFolder;
import com.sortify.main.model.SortifySubFolder;

public interface SortifyFolderService {
    void addFolder(SortifyFolder folder);
    void deleteSubFolder(SortifySubFolder SubFolderId);
    SortifySubFolder findSubFolder(String username);
}
