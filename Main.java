import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {

        String dataDir = "data";   // ROOT directory
        
        String queryFile = "topics.txt";

        Indexer indexer = new Indexer();

        // Traverse all folders + gz files
        List<File> gzFiles = getAllGZFiles(new File(dataDir));

        for (File gzFile : gzFiles) {
            System.out.println("Processing: " + gzFile.getPath());

            BufferedReader reader = GZReader.getReader(gzFile.getPath());
            StringBuilder docBuffer = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {

                if (line.contains("<DOC>")) {
                    docBuffer = new StringBuilder();
                }

                docBuffer.append(line).append("\n");

                if (line.contains("</DOC>")) {
                    String doc = docBuffer.toString();

                    String docId = Parser.extractDocId(doc);
                    String text = Parser.extractText(doc);

                    indexer.indexDocument(docId, text);
                }
            }

            reader.close();
        }

        System.out.println("total gz found" + gzFiles.size());

        System.out.println("Indexed Docs: " + indexer.totalDocs);

        // SEARCH PART (same as before)
        Map<String, String> queries = QueryParser.parseQueries(queryFile);

        BM25 bm25 = new BM25(indexer);
        Searcher searcher = new Searcher();

        List<Result> results = new ArrayList<>();

        for (String qid : queries.keySet()) {

            Map<String, Double> scores = bm25.score(queries.get(qid));
            List<Map.Entry<String, Double>> ranked = searcher.rank(scores);

            int rank = 1;
            for (Map.Entry<String, Double> entry : ranked) {

                if (rank > 1000) break;

                results.add(new Result(qid, entry.getKey(), rank, entry.getValue()));
                rank++;
            }
        }

        ResultWriter.write("results.txt", results, "g3");

        System.out.println("Done.");
    }

    // RECURSIVE FILE SEARCH
    public static List<File> getAllGZFiles(File dir) {

        List<File> gzFiles = new ArrayList<>();

        File[] files = dir.listFiles();
        if (files == null) return gzFiles;

        for (File file : files) {

            if (file.isDirectory()) {
                gzFiles.addAll(getAllGZFiles(file)); // recursion
            }

            else if (file.getName().endsWith(".GZ")) {
                gzFiles.add(file);
            }
        }

        return gzFiles;
    }

    
}