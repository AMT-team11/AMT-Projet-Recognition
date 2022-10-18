public class AwsCloudClient {
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

}
