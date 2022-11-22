public interface ILabelDetectorHelper {
    String MakeAnalysisRequest(String imageUri, int maxLabels, float minConfidence);

    // TODO ajoutez le Label detection pour une image en base64
}
