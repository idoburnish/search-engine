package index;

import java.util.*;

public class IndexFile {
    private final Map<String, IndexedField> indexedDocs = new HashMap<>();
    private static final IndexFile instance = new IndexFile();

    private IndexFile(){}

    public static IndexFile getIndexFileInstance(){
        return instance;
    }

    public void addIndexedField(IndexedField indexedField) {
        indexedDocs.put(indexedField.getTermName(), indexedField);
    }

    public IndexedField getIndexedField(String termName) {
        return indexedDocs.get(termName);
    }

    public Map<String, IndexedField> getIndexedDocs(){
        return indexedDocs;
    }
}
