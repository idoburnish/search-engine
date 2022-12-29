package index;

import java.util.*;

public class IndexedField {

    private String fieldName;
    private Map<String, List<Integer>> docIdByKeywork = new HashMap<>();

    public void indexing(String term, int docId){
        List<Integer> postingList = docIdByKeywork.get(term);
        if (postingList == null) {
            postingList = new ArrayList<>();
            postingList.add(docId);
            docIdByKeywork.put(term, postingList);
        }
        else {
            if (postingList.get(postingList.size() - 1) == docId) return;
            postingList.add(docId);
        }
    }

    public IndexedField(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getTermName(){
        return fieldName;
    }

    public Map<String, List<Integer>> getDocIdByKeywork(){
        return docIdByKeywork;
    }
}
