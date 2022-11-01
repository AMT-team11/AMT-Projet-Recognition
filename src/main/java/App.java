import com.amazonaws.services.s3.model.Bucket;

public class App {
    public static void main(String[] args){
        AwsCloudClient client = new AwsCloudClient();
        Bucket bucket = client.getBucket("amt.team11.diduno.education");
        System.out.println(bucket.getName());

    }


}
