import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;

import java.util.Base64;

public class BucketHelper implements IDataObjectHelper{
    private Bucket bucket;
    private Object object;
    private final AmazonS3 s3Client;

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
                s3Client.putObject(bucketName, objectUrl, filePath);
            } catch (Exception e) {
                System.err.println(e.getLocalizedMessage());
            }
        }
        return "Object created in bucket " + bucketName + " with key " + objectUrl;
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



    public String createBucket(String bucketUrl){
        this.bucket = s3Client.listBuckets().stream().filter(b -> b.getName().equals(bucketUrl)).findFirst().get();
        return "bucket created";
    }

    public String createObject(String ObjectUrl, Base64 dataRow){
        this.object = s3Client.getObject(bucket.getName(), ObjectUrl);
        return "object created";
    }
}
