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
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.json.Jackson;
import lombok.Getter;
import lombok.Setter;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Logger;

public class ImageHelper implements ILabelDetectorHelper {
    private final AmazonS3 s3Client;
    private final AmazonRekognition rekognitionClient;

    static final Logger log = Logger.getLogger(BucketHelper.class.getName());

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
            // TODO : uploader le résultat json dans le bucket
            // Uploads the result json to the bucket if the analysis is not yet on the bucket
            if (!s3Client.doesObjectExist(bucketName, imageUri + "RekognitionAnalysis.json")) {
                log.info("Uploading the result json to the bucket with key "
                        + imageUri + "RekognitionAnalysis.json");
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
            return e.getMessage();
        }
        return "Success";
    }

    // TODO la requête d'analyse lance une exception indiquant un mauvais format -> adapter cette méthode ou l'autre pour gérer tout type de format
    public String MakeAnalysisRequestWithImage64(byte[] image64, String imageUri, int maxLabels, float minConfidence) {
        ObjectMetadata metadata = new ObjectMetadata();
        InputStream fis = new ByteArrayInputStream(image64);
        metadata.setContentLength(image64.length);
        metadata.setContentType("image/png");
        metadata.setCacheControl("public, max-age=31536000");
        PutObjectRequest request = new PutObjectRequest(
                bucketName,
                imageUri,
                fis,
                metadata);
        s3Client.putObject(request);
        return MakeAnalysisRequest(imageUri, maxLabels, minConfidence);
    }
}
