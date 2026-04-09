import java.util.*;

public class Indexer {
    //inverted index
    //term is (docId ->tf)
    public Map<String, Map<String, Integer>> index = new HashMap<>();
    //stores document length
    //docId -> number of tokens in the document
    public Map<String, Integer> docLengths = new HashMap<>();
    //total number of indexed documents
    public int totalDocs = 0;
    // index a single document
    public void indexDocument(String docId, String text) {
        //preprocess the text into tokens (cleaning + splitting)
        List<String> tokens = preprocess(text);
        //if no valid tokens, skip this document
        if (tokens.isEmpty()) return;
        //store docuement length (used later for bm25 normalization)
        docLengths.put(docId, tokens.size());
        //increment total document count
        totalDocs++;
        //loop through each token word
        for (String token : tokens) {
            //if term not already in index, create a new postings list
            index.putIfAbsent(token, new HashMap<>());
            //get postings list for this term
            Map<String, Integer> postings = index.get(token);
            //update tf for this document
            //if doc exists, increment count, else start at 1
            postings.put(docId, postings.getOrDefault(docId, 0) + 1);
        }
    }
    //preprocess text: normalize and tokenize
    public List<String> preprocess(String text) {
        //convert to lowercase and remove special characters
        //keeps only letters and numbers
        text = text.toLowerCase().replaceAll("[^a-z0-9 ]", " ");
        //split text into words using whitespace
        String[] words = text.split("\\s+");
        //list to store valid tokens
        List<String> tokens = new ArrayList<>();
        //filter words
        for (String w : words) {
            //ignore very short words (length <=2)
            //helps remove noise like "is", "at", "on"
            if (w.length() > 2) tokens.add(w);
        }
        //return cleaned tokens
        return tokens;
    }
}
