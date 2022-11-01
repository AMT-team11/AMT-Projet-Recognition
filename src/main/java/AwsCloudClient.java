import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;

public class AwsCloudClient implements ICloudClient{
    private AwsDataObjectHelperImpl dataObjectHelper;
    private AwsLabelDetectorHelperImpl labelDetectorHelper;

    static AwsCloudClient instance;

    AwsCloudClient(){
        this.dataObjectHelper = new AwsDataObjectHelperImpl();
        this.labelDetectorHelper = new AwsLabelDetectorHelperImpl();
    }

    public AwsCloudClient getInstance(){
        if(instance == null){
            instance = new AwsCloudClient();
            return instance;
        } else {
            return instance;
        }
    }

    public Bucket getBucket(String bucketName){
        final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.EU_WEST_2).build();
        return s3.listBuckets().stream().filter(b -> b.getName().equals(bucketName)).findFirst().get();
    }

}
