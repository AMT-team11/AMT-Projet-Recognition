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
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.json.Jackson;
import lombok.Getter;
import lombok.Setter;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

public class ImageHelper implements ILabelDetectorHelper {

    private AmazonS3 s3Client;

    private AmazonRekognition rekognitionClient;

    @Getter
    @Setter
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
            // Detects labels in the S3 object
            DetectLabelsResult result = rekognitionClient.detectLabels(request);
            List<Label> labels = result.getLabels();
            // Uploads the result json to the bucket if the analysis is not yet on the bucket
            if (!s3Client.doesObjectExist(bucketName, imageUri + "RekognitionAnalysis.json")) {
                System.out.println("Uploading the result json to the bucket");
                String json = Jackson.toJsonString(labels);
                InputStream is = new ByteArrayInputStream(json.getBytes());
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(json.getBytes().length);
                metadata.setContentType("application/json");
                metadata.setCacheControl("public, max-age=31536000");
                s3Client.putObject(bucketName, imageUri + "RekognitionAnalysis.json", is, metadata);
            }
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
            return "Error";
        }
        return "Success";
    }

    
}
