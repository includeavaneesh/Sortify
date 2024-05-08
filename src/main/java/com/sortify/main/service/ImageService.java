package com.sortify.main.service;

import com.sortify.main.model.SortifyImage;
import com.sortify.main.model.SortifySubFolder;
import com.sortify.main.repository.SortifyImageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageService implements SortifyImageService{

    @Autowired
    private SortifyImageRepository imageRepository;

    @Autowired
    private  SortifySubFolderService SUBFOLDER_SERVICE;
    @Override
    public void saveImage(SortifyImage image) {
        imageRepository.save(image);
    }

    @Override
    public void deleteImage(String imageId) {
        imageRepository.deleteById(imageId);
    }

    @Override
    public SortifyImage findImage(String imageId) {
        if(imageRepository.findById(imageId).isPresent()) {
            return imageRepository.findById(imageId).get();
        }

        return null;
    }

    public SortifyImage updateImage(String imageId, String subFolderId) {
        SortifyImage image = findImage(imageId);
        SortifySubFolder subFolder = SUBFOLDER_SERVICE.findSubFolder(subFolderId);
        image.setSubFolder(subFolder);
        SUBFOLDER_SERVICE.saveSubFolder(subFolder);

        return image;

    }
}
