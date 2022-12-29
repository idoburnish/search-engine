import index.*;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;

import java.util.*;
import java.io.*;

public class Indexing {

    // corpus 입력
    public void fileRead(String fileName) throws IOException {
        String path = new File("").getAbsolutePath() + "/src/main/java/corpus/";
        BufferedReader br = new BufferedReader(new FileReader(path + fileName));

        String str, body = "";
        Document curDoc = new Document();
        AllDocuments allDocuments = AllDocuments.getAllDocuments();
        while ((str = br.readLine()) != null) {
            if (str.isEmpty()) continue;

            // docId 추출
            if (str.contains("<title>")) {
                if (!body.equals("")) {
                    curDoc.addField(new Field("body", body));
                    body = "";
                }

                // <title> 태그 제거
                str = str.substring(7);

                // document id 추출
                int idIdx = str.indexOf('.');
                int id = Integer.parseInt(str.substring(0, idIdx));
                curDoc = new Document(id);
                allDocuments.addDocument(curDoc);
                str = str.substring(idIdx + 2);

                // </title> 태그 제거
                str = str.substring(0, str.length() - 8);
                curDoc.addField(new Field("title", str));
            }
           body += str + " ";
        }
        curDoc.addField(new Field("body", body));

        br.close();
    }

    // 형태소 분석
    public void morphAnalysis(){
        AllDocuments allDocuments = AllDocuments.getAllDocuments();
        IndexFile indexFile = IndexFile.getIndexFileInstance();
        Komoran komoran = new Komoran(DEFAULT_MODEL.FULL);
        KomoranResult analysis;

        IndexedField indexedBodyField = new IndexedField("body");
        for (int i=1; i<= allDocuments.getSize(); i++) {
            Document doc = allDocuments.getDocument(i);

            String body = doc.get("body");
            analysis = komoran.analyze(body);
            for (Token token : analysis.getTokenList()) {
                doc.addTermFrequency(token.getMorph());
                indexedBodyField.indexing(token.getMorph(), doc.getId());
            }
        }

        indexFile.addIndexedField(indexedBodyField);
    }

    // 가중치 계산
    public void tf_idf(){
        AllDocuments allDocuments = AllDocuments.getAllDocuments();
        IndexFile indexFile = IndexFile.getIndexFileInstance();
        Map<String, List<Integer>> indexedBody = indexFile.getIndexedField("body").getDocIdByKeywork();

        for (int i=1; i<=allDocuments.getSize(); i++) {
            Document doc = allDocuments.getDocument(i);
            Map<String, Integer> termFreq = doc.getTermFrequency();
            Map<String, Double> weight = new HashMap<>();

            for (String term : termFreq.keySet()) {
                double tf = 1 + Math.log10(termFreq.get(term));
                double idf = Math.log10(allDocuments.getSize() / (double) indexedBody.getOrDefault(term, new ArrayList<>()).size());
                weight.put(term, tf * idf);
            }
            doc.setTfIdf(weight);
        }
    }

    // doc 길이 계산
    public void normalization(){
        AllDocuments allDocuments = AllDocuments.getAllDocuments();
        for (int i=1; i<= allDocuments.getSize(); i++) {
            Document doc = allDocuments.getDocument(i);
            Map<String, Double> tfIdf = doc.getTfIdf();

            double length = 0D;
            for (String term : tfIdf.keySet()) {
                length += Math.pow(tfIdf.get(term), 2);
            }
            length = Math.sqrt(length);
            doc.setLength(length);
        }
    }
}
