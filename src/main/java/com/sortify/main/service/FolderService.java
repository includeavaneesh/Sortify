package com.sortify.main.service;

import com.sortify.main.model.SortifyFolder;
import com.sortify.main.model.SortifySubFolder;
import com.sortify.main.repository.SortifyFolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class FolderService implements SortifyFolderService{

    @Autowired
    private SortifyFolderRepository sortifyFolderRepository;
    @Autowired
    private SortifyUserService userService;

    @Override
    public void addFolder(SortifyFolder folder) {
        sortifyFolderRepository.save(folder);
    }

    @Override
    public void deleteFolder(String folderId) {
        sortifyFolderRepository.deleteById(folderId);
    }

    @Override
    public SortifyFolder findFolder(String username) {
        String folderId = userService.findUserByUsername(username).getParentFolder().getFolderId();

        if(sortifyFolderRepository.findById(folderId).isPresent()) {
            return sortifyFolderRepository.findById(folderId).get();
        }
        return null;
    }

    @Override
    public ArrayList<SortifySubFolder> getAllSubFolder(String folderId) {
        if(sortifyFolderRepository.findById(folderId).isPresent())
            return (ArrayList<SortifySubFolder>) sortifyFolderRepository.findById(folderId).get().getSubFolders();
        return new ArrayList<>();
    }


}
