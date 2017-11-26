package ifonlygram.service;

import ifonlygram.external.CloudinaryApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static ifonlygram.util.ExecutePython.executePython;
import static ifonlygram.util.FileHelper.deleteImage;
import static ifonlygram.util.FileHelper.saveImage;

@Component
public class CreateAvatarServiceImpl implements CreateAvatarService {

    private static final String PYTHON_AVATAR_SCRIPT = "python/crop_avatar.py";

    @Autowired
    private CloudinaryApiClient cloudinaryApiClient;

    @Override
    public String createAvatar(String originalUrl) {
        originalUrl = "https:" + originalUrl;
        final String originalImagePath = "avatar/originalimage.jpg";
        saveImage(originalUrl, originalImagePath);

        executePython(PYTHON_AVATAR_SCRIPT, "originalimage.jpg");

        final String croppedImagePath = "avatar/cropped.jpg";
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
