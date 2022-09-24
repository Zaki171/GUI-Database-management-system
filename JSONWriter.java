import java.io.*;
import java.util.ArrayList;

public class JSONWriter {

    public void writeToJson(DataFrame df, String title) throws IOException {
        String newTitle = createTitle(title);
        BufferedWriter writer = new BufferedWriter(new FileWriter(newTitle));
        int patientNum = df.getRowCount();
        ArrayList<String> colNames = df.getColumnNames();
        StringBuilder patients = new StringBuilder();
        for (int i = 0; i < patientNum; i++) {
            StringBuilder patient = new StringBuilder();
            for (String name : colNames) {
                String value = df.getValue(name, i);
                patient.append("\n\"%s\": \"%s\" ,".formatted(name, value));
            }
            patient.deleteCharAt(patient.length() - 1);
            patients.append("\n{ %s },".formatted(patient.toString()));
        }
        patients.insert(0, "[");
        patients.insert(0, "{\"Patients\":");
        patients.deleteCharAt(patients.length() - 1);
        patients.append("]");
        patients.append("}");
        writer.write(patients.toString());
        writer.close();

    }

    private String createTitle(String title){
        StringBuilder newTitle = new StringBuilder();
        for(int i = 0; i < title.length(); i++){
            char c = title.charAt(i);
            if(c == '.'){
                break;
            }
            newTitle.append(c);
        }
        newTitle.append("JSON.json");
        return newTitle.toString();
    }

}
