import java.util.regex.*;

public class Parser {

    public static String extractDocId(String doc) {
        return extractTag(doc, "DOCNO").trim();
    }

    public static String extractText(String doc) {
        doc = doc.replaceAll("<DOCHDR>.*?</DOCHDR>", " ");
        return doc.replaceAll("<.*?>", " ");
    }

    private static String extractTag(String text, String tag) {
        Pattern pattern = Pattern.compile("<" + tag + ">(.*?)</" + tag + ">", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group(1) : "";
    }
}
