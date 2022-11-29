import com.amazonaws.SdkClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.internal.Mimetypes;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.logging.Logger;

public class BucketHelper implements IDataObjectHelper{
    private Bucket bucket;
    private Object object;
    private final AmazonS3 s3Client;

    static final Logger log = Logger.getLogger(BucketHelper.class.getName());

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
            } catch (SdkClientException e) {
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
                // todo REVIEW Prefer to use MIME.TYPE as extension (user input)
                String mimetype = Files.probeContentType(Path.of(filePath));
                if (mimetype == null || !mimetype.startsWith("image/")) {
                    return "Invalid file type";
                }
                metadata.setContentType(mimetype);
                metadata.setCacheControl("public, max-age=31536000");
                s3Client.putObject(bucketName, objectUrl, is, metadata);
            } catch (SdkClientException | IOException e) {
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
            } catch (SdkClientException e) {
                System.err.println(e.getLocalizedMessage());
            }
        } else {
            System.out.format("Object %s does not exist.\n", objectUrl);
            return "Object does not exist";
        }
        return "Object deleted from bucket " + bucketName + " with key " + objectUrl;
    }

    @Override
    public Boolean doesObjectExist(String objectUrl) {
        return s3Client.doesObjectExist(bucketName, objectUrl);
    }

    @Override
    public byte[] downloadObject(String objectUrl) {
        S3Object s3Object = s3Client.getObject(bucketName, objectUrl);
        byte[] dl = null;
        try {
            dl = s3Object.getObjectContent().readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dl;
    }

    public String downloadObject(String objectUrl, String filePath){
        if (s3Client.doesObjectExist(bucketName, objectUrl)) {
            try {
                S3Object obj = s3Client.getObject(bucketName, objectUrl);
                InputStream is = obj.getObjectContent();
                FileUtils.copyInputStreamToFile(is, new File(filePath));
            // todo REVIEW Do not catch the whole world ! Be more specific.
            } catch (IOException | SdkClientException e) {
                System.err.println(e.getLocalizedMessage());
            }
        } else {
            // todo REVIEW Log this info, but do not use prompt display "Object %s does not exist.\n"
            log.info("Object " + objectUrl + " does not exist.");
            return "Object does not exist";
        }
        return "Object downloaded from bucket " + bucketName + " with key " + objectUrl;
    }

    //TODO REVIEW Remove all Bucket mentions in your public method. Everything is an Object.

    /*
    public void listeBucketContent() {
        ObjectListing objectListing = s3Client.listObjects(bucketName);
        objectListing.getObjectSummaries().forEach(o -> System.out.println(o.getKey()));
    }
     */
}
