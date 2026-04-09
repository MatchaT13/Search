import java.io.*;
import java.util.*;

public class ResultWriter {
    //writes ranked search results to a file in standard format
    public static void write(String fileName, List<Result> results, String groupId) throws IOException {
        //create writer to output results to file
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        //loop through each result object
        for (Result r : results) {
            //write result in format:
            //queryId Q0 docId rank score groupId
            //Q0 is a standard placeholder used in evaluation system
            writer.write(r.queryId + " Q0 " +
                    r.docId + " " +
                    r.rank + " " +
                    r.score + " " +
                    groupId);
            //move to next line after each result
            writer.newLine();
        }
        //close writer to save and release file resources
        writer.close();
    }
}
