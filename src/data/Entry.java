package data;

import org.json.simple.JSONObject;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "entry")
public class Entry {
    private String date;
    private String age;
    private float weight;
    private float cephalicPerimeter;

    public Entry(String date, String age, float weight, float cephalicPerimeter) {
        this.date = date;
        this.age = age;
        this.weight = weight;
        this.cephalicPerimeter = cephalicPerimeter;
    }

    @XmlElement
    public void setDate(String date) {
        this.date = date;
    }

    @XmlElement
    public void setAge(String age) {
        this.age = age;
    }

    @XmlElement
    public void setWeight(float weight) {
        this.weight = weight;
    }

    @XmlElement
    public void setCephalicPerimeter(float cephalicPerimeter) {
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
        entry.put("weight", weight);
        entry.put("cephalicPerimeter", cephalicPerimeter);
        return entry;
    }
}
