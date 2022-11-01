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
        if(bucket == null){
            return createBucket(objectUrl); // verify params
        }
        if(object == null){
            return createObject(objectUrl, filePath); // verify params
        }
        return "bucket and object already exist";
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
