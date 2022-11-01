import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;

public class AwsCloudClient implements ICloudClient{
    private BucketHelper dataObjectHelper;
    private ImageHelper labelDetectorHelper;

    static AwsCloudClient instance;



    AwsCloudClient(){
        this.dataObjectHelper = new BucketHelper();
        this.labelDetectorHelper = new ImageHelper();
    }

    public AwsCloudClient getInstance(){
        if(instance == null){
            instance = new AwsCloudClient();
            return instance;
        } else {
            return instance;
        }
    }




}
