import java.util.*;
import java.util.stream.*;

public class Searcher {

    public List<Map.Entry<String, Double>> rank(Map<String, Double> scores) {
        return scores.entrySet()
                .stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .collect(Collectors.toList());
    }
}
