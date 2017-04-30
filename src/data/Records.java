package data;

import java.util.ArrayList;

public class Records {
    private String patientName;
    private ArrayList<Entry> recordTable;

    public Records(){
        this("patient");
    }

    public Records(String patientName){
        this.patientName = patientName;
        recordTable = new ArrayList<>();
    }

    public boolean addRecordEntry(Entry newEntry){
        return recordTable.add(newEntry);
    }

    @Override
    public String toString() {
        return "Records for " + patientName + ":\n" + recordTable;
    }
}
