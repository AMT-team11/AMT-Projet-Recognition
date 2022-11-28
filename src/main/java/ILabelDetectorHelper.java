public interface ILabelDetectorHelper {
    String MakeAnalysisRequest(String imageUri, int maxLabels, float minConfidence);

    // TODO ajoutez le Label detection pour une image en base64
    String MakeAnalysisRequestWithImage64(byte[] image64, String imageUri, int maxLabels, float minConfidence);
}
