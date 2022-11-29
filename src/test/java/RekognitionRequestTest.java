import com.amazonaws.util.json.Jackson;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonArrayFormatVisitor;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Map;

public class RekognitionRequestTest {
    static AwsCloudClient client = AwsCloudClient.getInstance();

    static String objectUri = "test";

    static String dlPath = "src/test/resources/";

    static String imageTestPath = "src/test/resources/test_image.png";

    @BeforeAll
    public static void refreshHelpers() {
        client.refreshHelpers();
        client.selectBucket("amt.team11.diduno.education");
    }

    @AfterAll
    public static void cleanup() {
        new File(dlPath + "download.json").delete();
        client.deleteObject(objectUri);
        client.deleteObject(objectUri + "RekognitionAnalysis.json");
    }

    @Test
    public void makeAnalysisRequest_Should_CreateJSONAnalysis() {
        client.createObject(objectUri, imageTestPath);
        client.makeAnalysisRequest(objectUri, 10, 0.5f);
        assert(client.doesObjectExist(objectUri + "RekognitionAnalysis.json"));
    }

    @Test
    public void MakeAnalysisRequestWithImage64_Should_CreateJSONAnalysis() {
        byte[] image64 = {};
        try {
            image64 = Base64.getDecoder().decode(Base64.getEncoder()
                    .encodeToString(FileUtils.readFileToByteArray(new File(imageTestPath))));
        } catch (IOException e) {
            System.err.println(e.getLocalizedMessage());
        }
        client.makeAnalysisRequestWithImage64(objectUri, image64,10, 0.5f);
        assert(client.doesObjectExist(objectUri + "RekognitionAnalysis.json"));
    }

    @Test
    public void makeAnalysisRequest_Should_CreateJSONFileWithCoherentValues() {
        //TODO REVIEW Purpose of this test method ?
        int maxLabels = 10;
        float minConfidence = 0.5f;
        client.makeAnalysisRequest(objectUri, maxLabels, minConfidence);
        client.downloadObject(objectUri + "RekognitionAnalysis.json", dlPath + "download.json");
        try {
            String json = Files.readString(Paths.get(dlPath + "download.json"));
            ObjectNode[] v = Jackson.fromJsonString(json, ObjectNode[].class);
            assert(v.length <= maxLabels);
            for (ObjectNode o : v) {
                assert(o.get("confidence").floatValue() >= minConfidence);
            }
        } catch (IOException e) {
            System.err.println(e.getLocalizedMessage());
        }

    }
}
