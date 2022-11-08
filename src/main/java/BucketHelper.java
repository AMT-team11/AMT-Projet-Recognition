import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.InputStream;
import java.util.Base64;

public class BucketHelper implements IDataObjectHelper{
    private Bucket bucket;
    private Object object;
    private final AmazonS3 s3Client;

    @Setter
    @Getter
    private String bucketName = "amt.team11.diduno.education";

    BucketHelper(Regions regions, String profile){
        this.s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(regions)
                .withCredentials(new ProfileCredentialsProvider(profile))
                .build();
    }

    public BucketHelper() {
        this.s3Client = AmazonS3ClientBuilder.standard().withRegion(Regions.EU_WEST_2).build();
    }

    public String createObject(String objectUrl, String filePath){
        if (s3Client.doesBucketExistV2(bucketName)) {
            System.out.format("Bucket %s already exists.\n", bucketName);
        } else {
            try {
                s3Client.createBucket(bucketName);
            } catch (Exception e) {
                System.err.println(e.getLocalizedMessage());
            }
        }
        if (s3Client.doesObjectExist(bucketName, objectUrl)) {
            System.out.format("Object %s already exists.\n", objectUrl);
        } else {
            try {
                byte[] bI = Base64.getDecoder().decode(Base64.getEncoder()
                        .encodeToString(FileUtils.readFileToByteArray(new File(filePath))));
                InputStream is = new java.io.ByteArrayInputStream(bI);
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(bI.length);

                // Checks if the file to upload is an image with the right extension
                switch (getFileExtension(filePath).toLowerCase()) {
                    case "png":
                        metadata.setContentType("image/png");
                        break;
                    case "jpg":
                        metadata.setContentType("image/jpeg");
                        break;
                    default:
                        return "File type not supported";
                }
                metadata.setContentType("image/jpeg");
                metadata.setCacheControl("public, max-age=31536000");
                s3Client.putObject(bucketName, objectUrl, is, metadata);
            } catch (Exception e) {
                System.err.println(e.getLocalizedMessage());
            }
        }
        return "Object created in bucket " + bucketName + " with key " + objectUrl;
    }

    private String getFileExtension(String fileName) {
        String extension = "";
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i+1);
        }
        return extension;
    }

    public String deleteObject(String objectUrl){
        if (s3Client.doesObjectExist(bucketName, objectUrl)) {
            try {
                s3Client.deleteObject(bucketName, objectUrl);
            } catch (Exception e) {
                System.err.println(e.getLocalizedMessage());
            }
        } else {
            System.out.format("Object %s does not exist.\n", objectUrl);
            return "Object does not exist";
        }
        return "Object deleted from bucket " + bucketName + " with key " + objectUrl;
    }

    public String downloadObject(String objectUrl, String filePath){
        if (s3Client.doesObjectExist(bucketName, objectUrl)) {
            try {
                S3Object obj = s3Client.getObject(bucketName, objectUrl);
                InputStream is = obj.getObjectContent();
                FileUtils.copyInputStreamToFile(is, new File(filePath));
            } catch (Exception e) {
                System.err.println(e.getLocalizedMessage());
                return "Error downloading object";

            }
        } else {
            System.out.format("Object %s does not exist.\n", objectUrl);
            return "Object does not exist";
        }
        return "Object downloaded from bucket " + bucketName + " with key " + objectUrl;
    }
}
