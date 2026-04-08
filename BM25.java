import java.util.*;

public class BM25 {

    private double k1 = 1.5;
    private double b = 0.75;
    private double avgDocLength;
    private int N;
    private Indexer indexer;

    public BM25(Indexer indexer) {
        this.indexer = indexer;
        this.N = indexer.totalDocs;
        this.avgDocLength = indexer.docLengths.values().stream()
                .mapToInt(i -> i)
                .average()
                .orElse(0.0);
    }

    public Map<String, Double> score(String query) {

        Map<String, Double> scores = new HashMap<>();
        List<String> terms = indexer.preprocess(query);

        for (String term : terms) {

            if (!indexer.index.containsKey(term)) continue;

            Map<String, Integer> postings = indexer.index.get(term);
            int df = postings.size();

            double idf = Math.log((N - df + 0.5) / (df + 0.5) + 1);

            for (String docId : postings.keySet()) {

                int tf = postings.get(docId);
                int dl = indexer.docLengths.get(docId);

                double score = idf * ((tf * (k1 + 1)) /
                        (tf + k1 * (1 - b + b * dl / avgDocLength)));

                scores.put(docId, scores.getOrDefault(docId, 0.0) + score);
            }
        }

        return scores;
    }
}
