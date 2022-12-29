import index.AllDocuments;

import java.io.*;
import java.util.Map;

public class Engine {
    public static void main(String[] args) throws IOException {
        // index time
        Indexing indexing = new Indexing();
        indexing.fileRead("corpus.txt");
        indexing.morphAnalysis();
        indexing.tf_idf();
        indexing.normalization();

        // query time
        Searching searching = new Searching();
        String query = searching.inputQuery();
        Map<String, Integer> queryTerm = searching.morphAnalysis(query);
        int[] result = searching.search(queryTerm);

        // result
        AllDocuments allDocuments = AllDocuments.getAllDocuments();
        for (Integer i : result){
            System.out.println(i + ". " + allDocuments.getDocument(i).get("title"));
        }
    }
}
