import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClient;
import com.amazonaws.services.rekognition.model.DetectLabelsRequest;
import com.amazonaws.services.rekognition.model.DetectLabelsResult;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.Label;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;

import java.util.List;

public class ImageHelper implements ILabelDetectorHelper {

    private AmazonS3 s3Client;

    private AmazonRekognition rekognitionClient;

    private String bucketName = "amt.team11.diduno.education";

    ImageHelper(Regions regions, String profile){
        this.s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(regions)
                .withCredentials(new ProfileCredentialsProvider(profile))
                .build();
        this.rekognitionClient = AmazonRekognitionClient.builder()
                .withRegion(regions)
                .withCredentials(new ProfileCredentialsProvider(profile))
                .build();
    }

    // TODO : uploader le resultat json dans le bucket
    public String MakeAnalysisRequest(String imageUri, int maxLabels, float minConfidence) {
        DetectLabelsRequest request = new DetectLabelsRequest()
                .withImage(new Image()
                        .withS3Object(new com.amazonaws.services.rekognition.model.S3Object()
                                .withName(imageUri)
                                .withBucket(bucketName)))
                .withMaxLabels(maxLabels)
                .withMinConfidence(minConfidence);
        try {
            DetectLabelsResult result = rekognitionClient.detectLabels(request);
            List<Label> labels = result.getLabels();
            System.out.println("Detected labels for " + imageUri);
            for (Label label: labels)
                System.out.println(label.getName() + ": " + label.getConfidence().toString());
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
            return "Error";
        }
        return "Success";
    }

    
}
