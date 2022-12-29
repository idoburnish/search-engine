package index;

import java.util.*;

public class Document {
    private int id;
    private Map<String, Field> fieldByName = new HashMap<>();
    private Map<String, Integer> termFrequency = new HashMap<>();
    private Map<String, Double> tfIdf = new HashMap<>();
    private double length;

    public Document(){}

    public Document(int id) {
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public void addField(Field field) {
        fieldByName.put(field.getName(), field);
    }

    public String get(String fieldName) {
        return fieldByName.get(fieldName).getValue();
    }

    public void addTermFrequency(String term) {
        termFrequency.put(term, termFrequency.getOrDefault(term, 0) + 1);
    }

    public Map<String, Integer> getTermFrequency(){
        return termFrequency;
    }

    public void setTfIdf(Map<String, Double> tfIdf){
        this.tfIdf = tfIdf;
    }

    public Map<String, Double> getTfIdf(){
        return this.tfIdf;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getLength(){
        return this.length;
    }
}
