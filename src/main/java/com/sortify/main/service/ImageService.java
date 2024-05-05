package com.sortify.main.service;

import com.sortify.main.model.SortifyImage;
import com.sortify.main.repository.SortifyImageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageService implements SortifyImageService{

    @Autowired
    private SortifyImageRepository imageRepository;
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
}
