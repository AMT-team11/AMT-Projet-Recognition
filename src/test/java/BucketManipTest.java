import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.io.File;

//TODO REVIEW Issue -> Where is the .gitignore (target, .idea is listen by git)
//TODO REVIEW Issue -> Impossible to run test "Error running bucketmanip" -> Shorten the commands and try again"
//TODO REVIEW Remove all AWS dependencies from your test class

public class BucketManipTest {
    static AwsCloudClient client = AwsCloudClient.getInstance();
    static String objectUri = "test";

    static String dlPath = "src/test/resources/";

    static String imageTestPath = "src/test/resources/test_image.png";

    //TODO REVIEW The test class shouldn't use AWS class directly
    //TODO REVIEW Before each, all or class ?
    @BeforeAll
    public static void refreshHelpers() {
        client.refreshHelpers();
        client.selectBucket("amt.team11.diduno.education");
    }

    @AfterAll
    public static void cleanup() {
        new File( dlPath + "download.jpg").delete();
        client.deleteObject(objectUri);
        client.deleteObject(objectUri + "RekognitionAnalysis.json");
    }

    @Test
    // TODO REVIEW Rename the test signature. method + scenario + expected result
    public void Given_createObject_When_ObjectDoesntExist_Then_ObjectExist() {
        client.createObject(objectUri, imageTestPath);
        assert(client.doesObjectExist(objectUri));
    }

    //TODO REVIEW On case, on feature. Please split this test cast in two test case.
    @Test
    public void Given_deleteObject_When_ObjectDoesntExist_Then_ObjectNotExist() {
        client.deleteObject(objectUri);
        assert(!client.doesObjectExist(objectUri));
    }

    @Test
    public void Given_downloadObject_When_ObjectExist_Then_FileExist() {
        client.createObject(objectUri, objectUri);
        client.downloadObject(objectUri,  dlPath + "download.jpg");
        assert(new File(dlPath + "download.jpg").exists());
    }

    //TODO REVIEW Move test cases for analysis object in its own test class

}
