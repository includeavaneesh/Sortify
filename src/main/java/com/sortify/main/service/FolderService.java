package com.sortify.main.service;

import com.sortify.main.model.SortifyFolder;
import com.sortify.main.model.SortifySubFolder;
import com.sortify.main.repository.SortifyFolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FolderService implements SortifyFolderService{
    @Autowired
    private SortifyFolderRepository sortifyFolderRepository;
    @Override
    public void addFolder(SortifyFolder folder) {
        sortifyFolderRepository.save(folder);
    }

    @Override
    public void deleteSubFolder(SortifySubFolder SubFolderId) {
        // Todo
    }

    @Override
    public SortifySubFolder findSubFolder(String username) {
        return null;
    }
}
