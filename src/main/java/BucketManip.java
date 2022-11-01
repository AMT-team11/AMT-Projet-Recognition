import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;

import java.util.List;

import static com.amazonaws.regions.Regions.EU_WEST_2;

public class BucketManip {
    public static void main(String[] args) {

        AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(EU_WEST_2).build();
        List<Bucket> buckets = s3.listBuckets();
        System.out.println("Your {S3} buckets are:");
        for (Bucket b : buckets) {
            System.out.println("* " + b.getName());
        }
    }
}
