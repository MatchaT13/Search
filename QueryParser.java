import java.io.*;
import java.util.*;

public class QueryParser {
    //parses queries from a file
    //returns queryId -> quert text (title)
    public static Map<String, String> parseQueries(String filePath) throws IOException {
        //LinkedHashMap preserves insertion order
        Map<String, String> queries = new LinkedHashMap<>();
        //reader ro read query file line by line
        BufferedReader reader = new BufferedReader(new FileReader(filePath));

        String line;
        //stores current query ID
        String currentId = null;
        //buffer to accumultae title text
        StringBuilder titleBuffer = new StringBuilder();
        //flag to indicate if we are currently reading the <title> section
        boolean readingTitle = false;
        //read file line by line
        while ((line = reader.readLine()) != null) {
            //remove leading/trailing whitespace
            line = line.trim();
            //extract query ID from <num> tag
            if (line.startsWith("<num>")) 
                //keep only digits (remoces text like"Number: "
                currentId = line.replaceAll("[^0-9]", "");
            }
            //start reading query title
            else if (line.startsWith("<title>")) {
                readingTitle = true;
                //reset title buffer for new query
                titleBuffer = new StringBuilder();
                //add first line of title (remove <title>tag)
                titleBuffer.append(line.replace("<title>", "").trim()).append(" ");
            }
            //stop reading title when description section starts
            else if (line.startsWith("<desc>")) {
                readingTitle = false;
                //save completed quey (ID, title text)
                queries.put(currentId, titleBuffer.toString().trim());
            }
            //continue collecting title lines if still in title section
            else if (readingTitle) {
                //append additional lines of title
                titleBuffer.append(line).append(" ");
            }
        }
        //close file reader
        reader.close();
        //return all parsed queries
        return queries;
    }
}
