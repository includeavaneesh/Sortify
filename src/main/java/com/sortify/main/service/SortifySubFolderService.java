package com.sortify.main.service;

import com.sortify.main.model.SortifySubFolder;

public interface SortifySubFolderService {
    void saveSubFolder(SortifySubFolder subFolder);
    void deleteSubFolder(String SubFolderId);
}
