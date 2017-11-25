import ifonlygram.external.CloudinaryApiClient;
import ifonlygram.external.CloudinaryApiClientImpl;
import ifonlygram.service.CropImageService;
import ifonlygram.service.CropImageServiceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) {
//        final String url = "http://res.cloudinary.com/graduatework/image/upload/v1511626167/vg0dx01mqdgob2iojqel.jpg
        final String url = "croppedimages/processed.jpg";
        CloudinaryApiClient cropImageService = new CloudinaryApiClientImpl();
        cropImageService.saveImage(url);
    }

//    public static void main(String[] args) throws IOException {
//        Process p = Runtime.getRuntime().exec("python python/process_image.py -i croppedimages/originalimage.jpg");
//
//        String s = null;
//        System.out.println("Python output:\n");
//        BufferedReader stdInput = new BufferedReader(new
//                InputStreamReader(p.getInputStream()));
//
//        BufferedReader stdError = new BufferedReader(new
//                InputStreamReader(p.getErrorStream()));
//
//        // read the output from the command
//        System.out.println("Here is the standard output of the command:\n");
//        while ((s = stdInput.readLine()) != null) {
//            System.out.println(s);
//        }
//
//        // read any errors from the attempted command
//        System.out.println("Here is the standard error of the command (if any):\n");
//        while ((s = stdError.readLine()) != null) {
//            System.out.println(s);
//        }
//
//    }
}
