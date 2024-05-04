package com.sortify.main.service;

import com.sortify.main.model.SortifyImage;

public interface SortifyImageService {
    void saveImage(SortifyImage image);
    void deleteImage(String imageId);
    SortifyImage findImage(String imageId);
}
