import com.amazonaws.regions.Regions;

import static java.lang.System.exit;

public class App {
    public static void main(String[] args){
        AwsCloudClient client = AwsCloudClient.getInstance();
        if (args.length < 4) {
            System.out.println("At least four arguments needed to run this application.\n<profile> <region> <bucket>" +
                    " <operation> <operation arguments>");
            exit(1);
        } else {
            String profile = args[0];
            String region = args[1];
            String bucket = args[2];
            String operation = args[3];
            client.setProfile(profile);
            try {
                Regions regionEnum = Regions.valueOf(region);
                client.setRegions(regionEnum);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid region. Please use a valid region.");
                exit(1);
            }
            client.refreshHelpers();
            client.selectBucket(bucket);
            switch (operation) {
                case "bucket-content":
                    client.listBucketContent();
                    break;
                case "create":
                    if (args.length < 6) {
                        System.out.println("Please provide a file path to upload and a key on the bucket.");
                        exit(1);
                    } else {
                        String filePath = args[4];
                        String key = args[5];
                        client.createObject(key, filePath);
                    }
                    break;
                case "delete":
                    if (args.length < 5) {
                        System.out.println("Please provide a key on the bucket.");
                        exit(1);
                    } else {
                        String key = args[4];
                        client.deleteObject(key);
                    }
                    break;
                case "detect-labels":
                    if (args.length < 6) {
                        System.out.println("Please provide a key on the bucket, the number of labels wanted and a" +
                                " minimum confidence.");
                        exit(1);
                    } else {
                        String key = args[4];
                        int maxLabels = Integer.parseInt(args[5]);
                        float minConfidence = Float.parseFloat(args[6]);
                        client.detectLabels(key, maxLabels, minConfidence);
                    }
                    break;
                case "download":
                    if (args.length < 6) {
                        System.out.println("Please provide a file path to download to and the key of the object to" +
                                "download.");
                        exit(1);
                    } else {
                        String key = args[4];
                        String filePath = args[5];
                        client.downloadObject(key, filePath);
                    }
                    break;
                default:
                    System.out.println("Invalid operation. Please use a valid operation.");
                    exit(1);
            }
        }
        exit(0);
    }
}
