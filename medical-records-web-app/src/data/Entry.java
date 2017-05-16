package data;

import org.json.simple.JSONObject;

public class Entry {
    private String date;
    private String age;
    private float weight;
    private float height;
    private float cephalicPerimeter;

    public Entry(String date, String age, float weight, float height, float cephalicPerimeter) {
        this.date = date;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.cephalicPerimeter = cephalicPerimeter;
    }

    public String getDate() {
        return date;
    }

    public String getAge() {
        return age;
    }

    public float getWeight() {
        return weight;
    }

    public float getCephalicPerimeter() {
        return cephalicPerimeter;
    }

    public JSONObject toJSON() {
        JSONObject entry = new JSONObject();
        entry.put("date", date);
        entry.put("age", age);
        entry.put("height", height);
        entry.put("weight", weight);
        entry.put("cephalicPerimeter", cephalicPerimeter);
        return entry;
    }
}
