import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectListing;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static com.amazonaws.regions.Regions.EU_WEST_2;

public class BucketManipTest {
    AwsCloudClient client = AwsCloudClient.getInstance();
    AmazonS3 s3;
    @BeforeEach
    public void refreshHelpers() {
        client.refreshHelpers();
        s3 = AmazonS3ClientBuilder.standard()
                .withCredentials(new ProfileCredentialsProvider(client.getProfile()))
                .withRegion(client.getRegions()).build();
        client.selectBucket("amt.team11.diduno.education");
    }
    @Test
    public void createObject() {
        String objectUrl = "test";
        client.createObject(objectUrl, "./src/main/resources/1643262960000.jpg");
        ObjectListing objectListing = s3.listObjects(client.getSelectedBucket());
        StringBuilder sb = new StringBuilder();
        objectListing.getObjectSummaries().forEach(o -> sb.append(o.getKey()));
        assert(sb.toString().contains("test"));
        client.deleteObject(objectUrl);
    }

    @Test
    public void createAndDeleteObject() {
        createObject();
        ObjectListing objectListing = s3.listObjects(client.getSelectedBucket());
        StringBuilder sb = new StringBuilder();
        objectListing.getObjectSummaries().forEach(o -> sb.append(o.getKey()));
        assert(!sb.toString().contains("test"));
    }

    @Test
    public void createAndAnalyseObject() {
        String objectUrl = "test";
        client.createObject(objectUrl, "./src/main/resources/1643262960000.jpg");
        client.detectLabels(objectUrl, 10, 0.5F);
        ObjectListing objectListing = s3.listObjects(client.getSelectedBucket());
        StringBuilder sb = new StringBuilder();
        objectListing.getObjectSummaries().forEach(o -> sb.append(o.getKey()));
        assert(sb.toString().contains(objectUrl) && sb.toString().contains(objectUrl + "RekognitionAnalysis.json"));
        client.deleteObject(objectUrl);
        client.deleteObject(objectUrl + "RekognitionAnalysis.json");
    }

    @Test
    public void downloadObject() {
        String objectUrl = "test";
        client.createObject(objectUrl, "./src/main/resources/1643262960000.jpg");
        client.downloadObject(objectUrl, "./src/main/resources/download.jpg");
        assert(new File("./src/main/resources/download.jpg").exists());
        client.deleteObject(objectUrl);
        new File("./src/main/resources/download.jpg").delete();
    }
}
