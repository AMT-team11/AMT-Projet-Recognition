import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectListing;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

//TODO REVIEW Issue -> Where is the .gitignore (target, .idea is listen by git)
//TODO REVIEW Issue -> Impossible to run test "Error running bucketmanip" -> Shorten the commands and try again"
//TODO REVIEW Remove all AWS dependencies from your test class
import static com.amazonaws.regions.Regions.EU_WEST_2;

public class BucketManipTest {
    AwsCloudClient client = AwsCloudClient.getInstance();
    //TODO REVIEW The test class shouldn't use AWS class directly
    AmazonS3 s3;
    //TODO REVIEW Before each, all or class ?
    @BeforeEach
    public void refreshHelpers() {
        client.refreshHelpers();
        s3 = AmazonS3ClientBuilder.standard()
                .withCredentials(new ProfileCredentialsProvider(client.getProfile()))
                .withRegion(client.getRegions()).build();
        client.selectBucket("amt.team11.diduno.education");
    }
    @Test
    //TODO REVIEW Rename the test signature. method + scenario + expected result
    public void createObject() {
        String objectUrl = "test";
        client.createObject(objectUrl, "./src/main/resources/test_image.png");
        ObjectListing objectListing = s3.listObjects(client.getSelectedBucket());
        StringBuilder sb = new StringBuilder();
        objectListing.getObjectSummaries().forEach(o -> sb.append(o.getKey()));
        assert(sb.toString().contains("test"));
        client.deleteObject(objectUrl);
    }

    //TODO REVIEW On case, on feature. Please split this test cast in two test case.
    @Test
    public void createAndDeleteObject() {
        createObject();
        ObjectListing objectListing = s3.listObjects(client.getSelectedBucket());
        StringBuilder sb = new StringBuilder();
        objectListing.getObjectSummaries().forEach(o -> sb.append(o.getKey()));
        assert(!sb.toString().contains("test"));
    }

    //TODO REVIEW Move test cases for analysis object in its own test class
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

    @Test
    public void checkJSONAnalysis() {
        //TODO REVIEW Purpose of this test method ?
    }
}
