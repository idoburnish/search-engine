package index;

import java.util.*;

public class AllDocuments {
    private final Map<Integer, Document> documentById = new HashMap<>();
    private static final AllDocuments instance = new AllDocuments();

    private AllDocuments(){}

    public static AllDocuments getAllDocuments(){
        return instance;
    }

    public void addDocument(Document doc) {
        documentById.put(doc.getId(), doc);
    }

    public Document getDocument(int id) {
        return documentById.get(id);
    }

    public int getSize(){
        return documentById.size();
    }
}
