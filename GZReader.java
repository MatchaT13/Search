import java.io.*;
import java.util.zip.GZIPInputStream;

public class GZReader {

    public static BufferedReader getReader(String filePath) throws IOException {
        return new BufferedReader(
                new InputStreamReader(
                        new GZIPInputStream(new FileInputStream(filePath))
                )
        );
    }
}

