//TODO REVIEW Remove unused import statement
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static com.amazonaws.regions.Regions.EU_WEST_2;

//TODO REVIEW Purpose of this class ?
public class BucketManip {
    public static void main(String[] args) throws IOException {

        AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(EU_WEST_2).build();
        List<Bucket> buckets = s3.listBuckets();

        //System.out.println("Your {S3} buckets are:");
        for (Bucket b : buckets) {
            //System.out.println("* " + b.getName());
        }
        ObjectListing objectListing = s3.listObjects("amt.team11.diduno.education");
        objectListing.getObjectSummaries().forEach(o -> System.out.println(o.getKey()));
    }
}
