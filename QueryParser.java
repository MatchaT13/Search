import java.io.*;
import java.util.*;

public class QueryParser {

    public static Map<String, String> parseQueries(String filePath) throws IOException {

        Map<String, String> queries = new LinkedHashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));

        String line;
        String currentId = null;
        StringBuilder titleBuffer = new StringBuilder();
        boolean readingTitle = false;

        while ((line = reader.readLine()) != null) {

            line = line.trim();

            if (line.startsWith("<num>")) {
                currentId = line.replaceAll("[^0-9]", "");
            }

            else if (line.startsWith("<title>")) {
                readingTitle = true;
                titleBuffer = new StringBuilder();
                titleBuffer.append(line.replace("<title>", "").trim()).append(" ");
            }

            else if (line.startsWith("<desc>")) {
                readingTitle = false;
                queries.put(currentId, titleBuffer.toString().trim());
            }

            else if (readingTitle) {
                titleBuffer.append(line).append(" ");
            }
        }

        reader.close();
        return queries;
    }
}
