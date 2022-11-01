import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;

public class AwsCloudClient implements ICloudClient{
    private final Regions regions = Regions.EU_WEST_2;
    private final String profile = "default";
    private BucketHelper dataObjectHelper;
    private ImageHelper labelDetectorHelper;
    private static AwsCloudClient instance;

    AwsCloudClient(){
        this.dataObjectHelper = new BucketHelper(regions, profile);
        this.labelDetectorHelper = new ImageHelper(regions, profile);
    }

    public static AwsCloudClient getInstance(){
        if (instance == null) {
            instance = new AwsCloudClient();
        }
        return instance;
    }
}
