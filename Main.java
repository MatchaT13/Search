import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {
        //root directory containing dataset (folders with .gz files)
        String dataDir = "data";   // ROOT directory
        //file containing queries (for later searching)
        String queryFile = "topics.txt";
        //create indexer instance to build inverted index
        Indexer indexer = new Indexer();
        
        // Traverse all folders + gz files
        List<File> gzFiles = getAllGZFiles(new File(dataDir));
        //loop through each compressed file
        for (File gzFile : gzFiles) {
            System.out.println("Processing: " + gzFile.getPath());
            //open .gz file using GZReader (decompression handled inside)
            BufferedReader reader = GZReader.getReader(gzFile.getPath());
            //buffer to accumulate lines of a single document
            StringBuilder docBuffer = new StringBuilder();
            String line;
            //read file line by line
            while ((line = reader.readLine()) != null) {
                //start of a document
                if (line.contains("<DOC>")) {
                    docBuffer = new StringBuilder(); //reset buffer
                }
                //append current line to document buffer
                docBuffer.append(line).append("\n");
                //end of a document
                if (line.contains("</DOC>")) {
                    //convert accumulated lines into full document string
                    String doc = docBuffer.toString();
                    //extract document ID (eg, DOCNO)
                    String docId = Parser.extractDocId(doc);
                    //extract main text content from document
                    String text = Parser.extractText(doc);
                    //index the document (build inverted index)
                    indexer.indexDocument(docId, text);
                }
            }
            //close the reader afte processing the file
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
        //loop through all files
        for (File file : files) {
            //if directory, recursively search inside
            if (file.isDirectory()) {
                gzFiles.addAll(getAllGZFiles(file)); // recursion
            }
            //if file ends with .gz, add it to the list
            else if (file.getName().endsWith(".GZ")) {
                gzFiles.add(file);
            }
        }

        return gzFiles;
    }

    
}
