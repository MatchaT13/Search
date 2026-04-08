public class Result {
    String queryId;
    String docId;
    int rank;
    double score;

    public Result(String q, String d, int r, double s) {
        queryId = q;
        docId = d;
        rank = r;
        score = s;
    }
}
