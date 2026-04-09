import java.util.regex.*;

public class Parser {
    //extracts the document ID
    public static String extractDocId(String doc) {
        //calls helper method to extract content inside <DOCNO>..</DOCNO>
        //.trim() removes extra spaces or newline characters
        return extractTag(doc, "DOCNO").trim();
    }
    //extracts clean text content from the document
    public static String extractText(String doc) {
        //remove DOCHDR section (metadata/header not needed for indexing)
        doc = doc.replaceAll("<DOCHDR>.*?</DOCHDR>", " ");
        //remove all remaining xml/html-like tags
        //keeps only plain text for indexing
        return doc.replaceAll("<.*?>", " ");
    }
    //helper method to extract content inside a specific tag
    private static String extractTag(String text, String tag) {
        //create regex pattern to match content between <tag>..</tag>
        //(.*?), non greedy match (captures only what's inside the tag)
        //DOTALL allows '.' to match characters as well.
        Pattern pattern = Pattern.compile("<" + tag + ">(.*?)</" + tag + ">", Pattern.DOTALL);
        //apply pattern to the text
        Matcher matcher = pattern.matcher(text);
        //if match is found, return captured content
        //otherwise, return empty string
        return matcher.find() ? matcher.group(1) : "";
    }
}
