package ifonlygram.external;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Component
public class CloudinaryApiClientImpl implements CloudinaryApiClient {
    private static Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "graduatework",
            "api_key", "593791151728485",
            "api_secret", "yU_xhmPOozFfRQa3izDzT3sIFmE"));

    @Override
    public String saveImage(String path) {
        File toUpload = new File(path);
        try {
            Map<String, String> uploadResult = cloudinary.uploader().upload(toUpload, ObjectUtils.emptyMap());
            return uploadResult.get("url");
        } catch (IOException e) {
            throw new RuntimeException("cannot upload image");
        }
    }
}
