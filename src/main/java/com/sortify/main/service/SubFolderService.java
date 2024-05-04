package com.sortify.main.service;

import com.sortify.main.model.SortifyFolder;
import com.sortify.main.model.SortifySubFolder;
import com.sortify.main.repository.SortifySubFolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class SubFolderService implements SortifySubFolderService{

    @Autowired
    SortifySubFolderRepository subFolderRepository;

    @Autowired
    SortifyFolderService folderService;
    @Override
    public void saveSubFolder(SortifySubFolder subFolder) {
        subFolderRepository.save(subFolder);
    }

    @Override
    public void deleteSubFolder(String subFolderId) {
        subFolderRepository.deleteById(subFolderId);
    }

    @Override
    public SortifySubFolder findSubFolder(String subFolderId) {
        if(subFolderRepository.findById(subFolderId).isPresent()) {
            subFolderRepository.findById(subFolderId).get();
        }
        return null;
    }

    @Override
    public void deleteAllSubFolder(String folderId) {
        SortifyFolder folder = folderService.findFolder(folderId);
        ArrayList<SortifySubFolder> subFolderList = new ArrayList<>();
        if(folder != null) {
            subFolderList = (ArrayList<SortifySubFolder>) folder.getSubFolders();
            if(!subFolderList.isEmpty()) {
                subFolderRepository.deleteAll(subFolderList);
            }

            folder.setSubFolders(new ArrayList<>());
            folderService.addFolder(folder);
        }

    }


}
