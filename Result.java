public class Result {
    //ID of the query 
    String queryId;
    //ID of the document retrieved
    String docId;
    //rank/position of the document in the results list (1=highest)
    int rank;
    //relevance score
    double score;
    //constructor to initialize a result object
    public Result(String q, String d, int r, double s) {
        //assign query ID
        queryId = q;
        //assign document ID
        docId = d;
        //assign rank (position in sorted results)
        rank = r;
        //assign relevance score
        score = s;
    }
}
