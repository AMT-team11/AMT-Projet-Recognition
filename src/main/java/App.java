import com.amazonaws.services.s3.model.Bucket;

public class App {
    public static void main(String[] args){
        AwsCloudClient client = AwsCloudClient.getInstance();
        //client.createObject("/test", "src/main/resources/1643262960000.jpg");
        //client.deleteObject("/test.jpg");
        //client.detectLabels("/test.jpg", 10, 0.5f);
        //client.downloadObject("/test.jpgRekognitionAnalysis.json", "src/main/resources/analysis.json");
    }
}
