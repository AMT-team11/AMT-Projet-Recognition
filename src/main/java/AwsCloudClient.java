import com.amazonaws.regions.Regions;

public class AwsCloudClient implements ICloudClient{
    private final BucketHelper dataObjectHelper;
    private static AwsCloudClient instance;

    AwsCloudClient(){
        Regions regions = Regions.EU_WEST_2;
        String profile = "default";
        this.dataObjectHelper = new BucketHelper(regions, profile);
        ImageHelper labelDetectorHelper = new ImageHelper(regions, profile);
    }

    public static AwsCloudClient getInstance(){
        if (instance == null) {
            instance = new AwsCloudClient();
        }
        return instance;
    }

    public void deleteObject(String objectUrl){
        this.dataObjectHelper.deleteObject(objectUrl);
    }

    public void createObject(String objectUrl, String filePath){
        this.dataObjectHelper.createObject(objectUrl, filePath);
    }

    public void detectLabels(String imageUri, int maxLabels, float minConfidence){
        this.labelDetectorHelper.MakeAnalysisRequest(imageUri,maxLabels, minConfidence);
    }
}
