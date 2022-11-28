public interface IDataObjectHelper {
    String createObject(String objectUrl, String filePath);
    String deleteObject(String objectUrl);
    Boolean doesObjectExist(String objectUrl);
    byte[] downloadObject(String objectUrl);

}
