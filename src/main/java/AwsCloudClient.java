import com.amazonaws.regions.Regions;
import lombok.Getter;
import lombok.Setter;

public class AwsCloudClient implements ICloudClient{
    private BucketHelper dataObjectHelper;
    private ImageHelper imageHelper;
    private static AwsCloudClient instance;
    @Setter
    @Getter
    private Regions regions = Regions.EU_WEST_2;
    @Setter
    @Getter
    private String profile = "default";



    AwsCloudClient() {
        dataObjectHelper = new BucketHelper(regions, profile);
        imageHelper = new ImageHelper(regions, profile);
    }

    public void refreshHelpers() {
        dataObjectHelper = new BucketHelper(regions, profile);
        imageHelper = new ImageHelper(regions, profile);
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
        this.imageHelper.MakeAnalysisRequest(imageUri,maxLabels, minConfidence);
    }

    public void downloadObject(String objectUrl, String filePath){
        this.dataObjectHelper.downloadObject(objectUrl, filePath);
    }

    public void selectBucket(String bucketName) {
        this.dataObjectHelper.setBucketName(bucketName);
        this.imageHelper.setBucketName(bucketName);
    }

    public String getSelectedBucket() {
        return dataObjectHelper.getBucketName();
    }

    public Boolean doesObjectExist(String objectUrl){
        return this.dataObjectHelper.doesObjectExist(objectUrl);
    }

    public void makeAnalysisRequest(String imageUri, int maxLabels, float minConfidence) {
        this.imageHelper.MakeAnalysisRequest(imageUri, maxLabels, minConfidence);
    }

    public void makeAnalysisRequestWithImage64(String objectUri, byte[] b64Image, int maxLabels, float minConfidence) {
        this.imageHelper.MakeAnalysisRequestWithImage64(b64Image, objectUri, maxLabels, minConfidence);
    }
}
