import com.amazonaws.services.s3.model.Bucket;

public class App {
    public static void main(String[] args){
        AwsCloudClient client = AwsCloudClient.getInstance();
        client.deleteObject("/test");
    }
}
