package com.sortify.main.service;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.GpsDirectory;
import com.sortify.main.model.SortifyUser;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public interface SortifyCloudStorageService {
    String uploadFile(MultipartFile file, String folderName) throws ImageProcessingException, IOException;

    /**
     * This function provides the capability to read gps coordinates from the user, unless the user wants to specify his own location
     *
     * @param file
     * @return Latitude and Longitude
     * @throws IOException
     * @throws ImageProcessingException
     */
    default double[] getImageCoordinates(MultipartFile file) throws IOException, ImageProcessingException {
        InputStream inputStream = file.getInputStream();

        Metadata metadata = ImageMetadataReader.readMetadata(inputStream);

        Collection<GpsDirectory> gpsDirectories = metadata.getDirectoriesOfType(GpsDirectory.class);
        for (GpsDirectory gpsDirectory : gpsDirectories) {
            // Try to read out the location, making sure it's non-zero
            GeoLocation geoLocation = gpsDirectory.getGeoLocation();
            if (geoLocation != null && !geoLocation.isZero()) {
                // Add to our collection for use below
                return new double[]{geoLocation.getLatitude(), geoLocation.getLongitude()};
            }
        }
        return new double[]{0.0, 0.0};
    }

    default File toFile(MultipartFile file) {
        File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));

        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return convertedFile;
    }

    byte[] downloadFile(String fileName, String folderName);

    String deleteFile(String fileName, String folderName);

    //temp func
    List<String> getAllFile(String userFolderName);

    void createUserFolder(String userFolderName);

    void deleteUserFolder(String userFolderName);

    void createUserSubFolder(SortifyUser user);

    boolean keyExists(String fileName, String userFolderName);
    URL generatePresignedURL(String fileName);
}
