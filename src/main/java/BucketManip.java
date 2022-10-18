import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;

public class BucketManip {
    public static void main(String[] args){
        BucketManip bm = new BucketManip();
        Bucket bucket = bm.getBucket("amt.team11.diduno.education");
        System.out.println(bucket.getName());
    }

    public Bucket getBucket(String bucketName){
        final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.EU_WEST_2).build();
        return s3.listBuckets().stream().filter(b -> b.getName().equals(bucketName)).findFirst().get();
    }
}
