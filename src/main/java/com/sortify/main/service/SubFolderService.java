package com.sortify.main.service;

import com.sortify.main.model.SortifySubFolder;
import com.sortify.main.repository.SortifySubFolderRepository;

public class SubFolderService implements SortifySubFolderService{

    SortifySubFolderRepository subFolderRepository;
    @Override
    public void saveSubFolder(SortifySubFolder subFolder) {
        subFolderRepository.save(subFolder);
    }

    @Override
    public void deleteSubFolder(String subFolderId) {
        subFolderRepository.deleteById(subFolderId);
    }
}
