package com.sortify.main.service;

import com.sortify.main.model.SortifyFolder;
import com.sortify.main.model.SortifySubFolder;
import com.sortify.main.repository.SortifySubFolderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@Slf4j
public class SubFolderService implements SortifySubFolderService{

    @Autowired
    SortifySubFolderRepository SUBFOLDER_REPOSITORY;

    @Autowired
    SortifyFolderService FOLDER_SERVICE;
    @Override
    public void saveSubFolder(SortifySubFolder subFolder) {
        SUBFOLDER_REPOSITORY.save(subFolder);
    }

    @Override
    public void deleteSubFolder(String subFolderId) {
        SUBFOLDER_REPOSITORY.deleteById(subFolderId);
    }

    @Override
    public SortifySubFolder findSubFolder(String subFolderId) {

        if(SUBFOLDER_REPOSITORY.findById(subFolderId).isPresent()) {
            return SUBFOLDER_REPOSITORY.findById(subFolderId).get();
        }
        else{
            return null;
        }

    }

    @Override
    public void deleteAllSubFolder(String folderId) {
        SortifyFolder folder = FOLDER_SERVICE.findFolder(folderId);
        ArrayList<SortifySubFolder> subFolderList = new ArrayList<>();
        if(folder != null) {
            subFolderList = (ArrayList<SortifySubFolder>) folder.getSubFolders();
            if(!subFolderList.isEmpty()) {
                SUBFOLDER_REPOSITORY.deleteAll(subFolderList);
            }

            folder.setSubFolders(new ArrayList<>());
            FOLDER_SERVICE.addFolder(folder);
        }

    }

    @Override
    public void deleteClusteringFolder(String folderId) {
        SortifyFolder folder = FOLDER_SERVICE.findFolder(folderId);
        List<SortifySubFolder> subFolderList = folder.getSubFolders();
        SortifySubFolder mainSubFolder = subFolderList.get(0);
        subFolderList.removeIf(subFolder -> subFolder != mainSubFolder && subFolder.getImageList().isEmpty());
        FOLDER_SERVICE.addFolder(folder);
    }

}
