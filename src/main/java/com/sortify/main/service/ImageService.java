package com.sortify.main.service;

import com.sortify.main.model.SortifyImage;
import com.sortify.main.model.SortifySubFolder;
import com.sortify.main.repository.SortifyImageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ImageService implements SortifyImageService{

    @Autowired
    private SortifyImageRepository imageRepository;

    @Autowired
    private  SortifySubFolderService SUBFOLDER_SERVICE;
    @Override
    @CachePut(value = "user_cache", key = "#image.fileName")
    public void saveImage(SortifyImage image) {
        imageRepository.save(image);
    }

    @Override
    @CacheEvict(value = "user_cache", key = "#fileName")
    public void deleteImage(String fileName) {
        imageRepository.deleteById(fileName);
    }

    @Override
    @Cacheable(value = "user_cache", key = "#fileName")
    public SortifyImage findImage(String fileName) {
        return imageRepository.findById(fileName).get();

    }

    @CachePut(value = "user_cache", key = "#fileName")
    public SortifyImage updateImage(String fileName, String subFolderId) {
        SortifyImage image = findImage(fileName);
        SortifySubFolder subFolder = SUBFOLDER_SERVICE.findSubFolder(subFolderId);
        image.setSubFolder(subFolder);
        SUBFOLDER_SERVICE.saveSubFolder(subFolder);
        return image;
    }
}
