package ifonlygram.service;

import ifonlygram.external.CloudinaryApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import static ifonlygram.util.ExecutePython.executePython;

@Component
public class CropImageServiceImpl implements CropImageService {

    private static final String PYTHON_CROP_SCRIPT = "python/process_image.py";

    @Autowired
    private CloudinaryApiClient cloudinaryApiClient;

    @Override
    public String cropImage(String originalUrl) {
        final String originalImagePath = "croppedimages/originalimage.jpg";
        saveImage(originalUrl, originalImagePath);

        executePython(PYTHON_CROP_SCRIPT, originalImagePath);

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

    private void saveImage(String imageUrl, String destinationFile) {
        URL url = null;
        try {
            url = new URL(imageUrl);
            InputStream is = url.openStream();
            OutputStream os = new FileOutputStream(destinationFile);

            byte[] b = new byte[2048];
            int length;

            while ((length = is.read(b)) != -1) {
                os.write(b, 0, length);
            }

            is.close();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void deleteImage(final String imagePath) {
        try{
            File file = new File(imagePath);
            if(file.delete()){
                System.out.println(file.getName() + " is deleted!");
            }else{
                System.out.println("Delete operation is failed.");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
