package com.sortify.main.service;

import com.sortify.main.model.SortifyFolder;
import com.sortify.main.model.SortifySubFolder;
import com.sortify.main.repository.SortifyFolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FolderService implements SortifyFolderService{

    @Autowired
    private SortifyFolderRepository FOLDER_REPOSITORY;
    @Autowired
    private SortifyUserService USER_SERVICE;

    @Override
    public void addFolder(SortifyFolder folder) {
        FOLDER_REPOSITORY.save(folder);
    }

    @Override
    public void deleteFolder(String folderId) {
        FOLDER_REPOSITORY.deleteById(folderId);
    }

    @Override
    public SortifyFolder findFolder(String username) {
        String folderId = USER_SERVICE.findUserByUsername(username).getParentFolder().getFolderId();

        if(FOLDER_REPOSITORY.findById(folderId).isPresent()) {
            return FOLDER_REPOSITORY.findById(folderId).get();
        }
        return null;
    }

    @Override
    public List<SortifySubFolder> getAllSubFolder(String folderId) {

        return FOLDER_REPOSITORY.findById(folderId).get().getSubFolders();

    }


}
