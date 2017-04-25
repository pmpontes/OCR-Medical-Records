package data;

import java.util.Date;

public class Entry {
    private String date;
    private int age;
    private int weight;
    private int cephalicPerimeter;

    public Entry(String date, int age, int weight, int cephalicPerimeter) {
        this.date = date;
        this.age = age;
        this.weight = weight;
        this.cephalicPerimeter = cephalicPerimeter;
    }

    public String getDate() {
        return date;
    }

    public int getAge() {
        return age;
    }

    public int getWeight() {
        return weight;
    }

    public int getCephalicPerimeter() {
        return cephalicPerimeter;
    }
}
