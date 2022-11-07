public interface ILabelDetectorHelper {
    String MakeAnalysisRequest(String imageUri, int maxLabels, float minConfidence);
}
