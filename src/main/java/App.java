import com.amazonaws.services.s3.model.Bucket;

public class App {
    public static void main(String[] args){
        AwsCloudClient client = AwsCloudClient.getInstance();
        String objectUrl = "test";
        if (args.length == 0) {
            System.out.println("At least one argument needed to upload image");
        }
        //client.createObject(objectUrl, "src/main/resources/1643262960000.jpg");
        //client.deleteObject(objectUrl);
        //client.detectLabels(objectUrl, 10, 0.5f);
        //client.downloadObject(objectUrl + "RekognitionAnalysis.json", "src/main/resources/analysis.json");
    }
}
