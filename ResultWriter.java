import java.io.*;
import java.util.*;

public class ResultWriter {

    public static void write(String fileName, List<Result> results, String groupId) throws IOException {

        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

        for (Result r : results) {
            writer.write(r.queryId + " Q0 " +
                    r.docId + " " +
                    r.rank + " " +
                    r.score + " " +
                    groupId);
            writer.newLine();
        }

        writer.close();
    }
}
