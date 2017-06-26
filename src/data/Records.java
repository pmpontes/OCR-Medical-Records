package data;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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

    public JSONObject toJSON() {
        JSONObject records = new JSONObject();
        JSONArray recordList = new JSONArray();

        for (Entry recordEntry : recordTable) {
            recordList.add(recordEntry.toJSON());
        }

        records.put("patientName", patientName);
        records.put("recordTable", recordList);

        return records;
    }
}
