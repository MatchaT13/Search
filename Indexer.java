import java.util.*;

public class Indexer {

    public Map<String, Map<String, Integer>> index = new HashMap<>();
    public Map<String, Integer> docLengths = new HashMap<>();
    public int totalDocs = 0;

    public void indexDocument(String docId, String text) {
        List<String> tokens = preprocess(text);

        if (tokens.isEmpty()) return;

        docLengths.put(docId, tokens.size());
        totalDocs++;

        for (String token : tokens) {
            index.putIfAbsent(token, new HashMap<>());
            Map<String, Integer> postings = index.get(token);
            postings.put(docId, postings.getOrDefault(docId, 0) + 1);
        }
    }

    public List<String> preprocess(String text) {
        text = text.toLowerCase().replaceAll("[^a-z0-9 ]", " ");
        String[] words = text.split("\\s+");

        List<String> tokens = new ArrayList<>();
        for (String w : words) {
            if (w.length() > 2) tokens.add(w);
        }
        return tokens;
    }
}
