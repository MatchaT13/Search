import java.util.*;
import java.util.stream.*;

public class Searcher {
    //rank documents based on their bm25 scores.
    public List<Map.Entry<String, Double>> rank(Map<String, Double> scores) {
        return scores.entrySet()
                .stream() //convert map entries into a stream for processing
                //sort entries by score in descending order (highest score first)
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                //collect sorted results into a list
                .collect(Collectors.toList());
    }
}
