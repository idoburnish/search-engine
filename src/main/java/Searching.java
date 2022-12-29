import index.AllDocuments;
import index.Document;
import index.IndexFile;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;

import java.util.*;
import java.io.*;

public class Searching {

    public String inputQuery() throws IOException {
        System.out.print("input query: ");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        return br.readLine();
    }

    public Map<String, Integer> morphAnalysis(String query) {
        Komoran komoran = new Komoran(DEFAULT_MODEL.FULL);
        KomoranResult analysis = komoran.analyze(query);

        Map<String, Integer> queryTermFreq = new HashMap<>();
        for (Token token: analysis.getTokenList()) {
            queryTermFreq.put(token.getMorph(), queryTermFreq.getOrDefault(token.getMorph(), 0) + 1);
        }
        return queryTermFreq;
    }

    public int[] search(Map<String, Integer> queryTermFreq) {
        AllDocuments allDocuments = AllDocuments.getAllDocuments();
        IndexFile indexFile = IndexFile.getIndexFileInstance();
        Map<String, List<Integer>> indexedBody = indexFile.getIndexedField("body").getDocIdByKeywork();

        Map<Integer, Double> scores = new HashMap<>();
        double[] length = new double[allDocuments.getSize() + 1];
        for (int i=1; i<= allDocuments.getSize(); i++) {
            scores.put(i, 0D);
            length[i] = allDocuments.getDocument(i).getLength();
        }

        // term-at-the-time
        for (String term : queryTermFreq.keySet()) {
            if (indexedBody.get(term) == null) continue;
            double queryWeight = 1 + Math.log10(queryTermFreq.get(term));
            for (Integer docId : indexedBody.get(term)) {
                Document doc = allDocuments.getDocument(docId);
                double weight = doc.getTfIdf().get(term) * queryWeight;
                scores.put(doc.getId(), scores.getOrDefault(doc.getId(), 0D) + weight);
            }
        }

        // normalization
        for (Integer docId : scores.keySet()) scores.put(docId, scores.get(docId) / length[docId]);

        // ranking -> select top 5
        List<Map.Entry<Integer, Double>> ranking = new ArrayList<>(scores.entrySet());
        ranking.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));

        int[] result = new int[5];
        for (int i=0; i<5; i++) result[i] = ranking.get(i).getKey();

        return result;
    }
}
