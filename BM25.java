import java.util.*;

public class BM25 {
    // BM25 parameter: controls term frequency scaling (higher= more influence of tf)//
    private double k1 = 1.5;
    // BM25 parameterL controls document length normalization (0=no normalization, 1=full)//
    private double b = 0.75;
    //Average length of all documents in collection//
    private double avgDocLength;
    //Total number of documenrs//
    private int N;
    //Reference to Indexer (contains inverted index, doc length, preprocessing)//
    private Indexer indexer;
    //Constructor: initializes BM25 with index data//
    public BM25(Indexer indexer) {
        this.indexer = indexer;
        //Total number of documents//
        this.N = indexer.totalDocs;
        //Compute average docuemtn length using Java Streams//
        this.avgDocLength = indexer.docLengths.values().stream()
                .mapToInt(i -> i)  //convert Integer to int//
                .average()         //compute average//
                .orElse(0.0);      //default if no documents//
    }
    //Computes BM25 scores for all docuements given a query
    public Map<String, Double> score(String query) {
        //Map to store final scores: docId -> score
        Map<String, Double> scores = new HashMap<>();
        //Preprocess query (tokenization, normalization, etc.)//
        List<String> terms = indexer.preprocess(query);
        //loop through each term in the query//
        for (String term : terms) {
            //skip term if it does not exist in the index//
            if (!indexer.index.containsKey(term)) continue;
            //get posting list: docId to term frequency (tf)//
            Map<String, Integer> postings = indexer.index.get(term);
            //document frequency: number of documents containing the term//
            int df = postings.size();
            //compute IDF//
            //rare terms get higher weight//
            double idf = Math.log((N - df + 0.5) / (df + 0.5) + 1);
            //loop through each document containing this term//
            for (String docId : postings.keySet()) {
                //tf in this document//
                int tf = postings.get(docId);
                //document length//
                int dl = indexer.docLengths.get(docId);
                //BM25 score formula//
                double score = idf * ((tf * (k1 + 1)) /
                        (tf + k1 * (1 - b + b * dl / avgDocLength)));
                //add score to existing value (for multi term queries)//
                scores.put(docId, scores.getOrDefault(docId, 0.0) + score);
            }
        }
        //return final scores for all documents//
        return scores;
    }
}
