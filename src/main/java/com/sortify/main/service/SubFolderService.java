package com.sortify.main.service;

import com.sortify.main.model.SortifySubFolder;
import com.sortify.main.repository.SortifySubFolderRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
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

    @Override
    public SortifySubFolder findSubFolder(String subFolderId) {
        if(subFolderRepository.findById(subFolderId).isPresent()) {
            subFolderRepository.findById(subFolderId).get();
        }
        return null;
    }

    @Override
    public ArrayList<SortifySubFolder> getAllSubFolder(String folderId) {
        ArrayList<SortifySubFolder> subFolderList = new ArrayList<>();
        return subFolderList;
    }

    @Override
    public void deleteAllSubFolder(String folderId) {

    }
}
