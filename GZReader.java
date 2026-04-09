import java.io.*;
import java.util.zip.GZIPInputStream;

public class GZReader {
    //returns a BufferedReader for reading a .gz (compressed) file
    public static BufferedReader getReader(String filePath) throws IOException {
        return new BufferedReader(
                //convrts byte stream into character stream
                new InputStreamReader(
                        //decompresses the .gz file while reading
                        //reads raw bytes from the file path
                        new GZIPInputStream(new FileInputStream(filePath))
                )
        );
    }
}

