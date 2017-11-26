package ifonlygram.service;

import ifonlygram.external.CloudinaryApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static ifonlygram.util.ExecutePython.executePython;
import static ifonlygram.util.FileHelper.deleteImage;
import static ifonlygram.util.FileHelper.saveImage;

@Component
public class CropImageServiceImpl implements CropImageService {

    private static final String PYTHON_CROP_SCRIPT = "python/process_image.py";

    @Autowired
    private CloudinaryApiClient cloudinaryApiClient;

    @Override
    public String cropImage(String originalUrl) {
        final String originalImagePath = "croppedimages/originalimage.jpg";
        saveImage(originalUrl, originalImagePath);

        executePython(PYTHON_CROP_SCRIPT, "originalimage.jpg");

        final String croppedImagePath = "croppedimages/processed.jpg";
        String croppedImageUrl = null;
        try {
            croppedImageUrl = cloudinaryApiClient.saveImage(croppedImagePath);
        } catch (RuntimeException e) {
            if(e.getMessage().contains("Upload preset must be specified when using unsigned upload")) {
                croppedImageUrl = cloudinaryApiClient.saveImage(croppedImagePath);
            }
        }

        deleteImage(originalImagePath);
        deleteImage(croppedImagePath);
        return croppedImageUrl;
    }

}
