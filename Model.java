import java.io.IOException;
import java.util.ArrayList;

public class Model {
    private DataFrame df;

    public Model(String fn) throws IOException {
        if(fn.charAt(fn.length()-1)=='v'){//check whether csv or json file
            df = new DataLoader().readFile(fn);
        }
        else{
            df = new JSONReader().readFile(fn);
        }
    }
    public ArrayList<String> getHeaders(){
        return df.getColumnNames();
    }

    public int getNumberHeaders(){
        return df.getColumnCount();
    }

    public ArrayList<String> getColData(String name){
        ArrayList<String> colData = new ArrayList<String>();
        int rowCount = df.getRowCount();
        for(int i = 0; i<rowCount;i++){
            String value = df.getValue(name,i);
            colData.add(value);
        }
        return colData;
    }


    public int rowCount(){
        return df.getRowCount();
    }

    public String[] findOldestPerson() {
        ArrayList<String> rowData = new ArrayList<>();
        int highestAge = 0;
        int highestAgeIndex = 0;
        int[] ages = getAges();
        for(int i = 0; i < ages.length; i++){
            if(ages[i]>highestAge){
                highestAge = ages[i];
                highestAgeIndex = i;
            }
        }
        rowData.add("ID: " + df.getValue("ID",highestAgeIndex));
        rowData.add("BIRTHDATE: " + df.getValue("BIRTHDATE",highestAgeIndex));
        rowData.add("FIRST: " + df.getValue("FIRST",highestAgeIndex));
        rowData.add("LAST: " + df.getValue("LAST",highestAgeIndex));
        return rowData.toArray(String[]::new);


    }

    public boolean checkIfPatients(){
        if(df.getColumnCount()==20){//if number of columns are 20 assume one of the patients files have been selected
            return true;
        }
        else{
            return false;
        }
    }

    public int getNumPeople(String city) {
        ArrayList<String> cityData = getColData("CITY");
        int num = 0;
        for(String value : cityData){
            if(value.toLowerCase().equals(city.toLowerCase())){ num++; }
        }
        return num;
    }

    public int getPatientIndex(String colName, String value){
        int i;
        for(i = 0; i < df.getRowCount(); i++){
            if(df.getValue(colName, i).equals(value)){
                return i;
            }
        }
        return -1;
    }

    public int getFirstLastIndex(String fn, String ln) {//for searching with first and last name
        int i;
        for (i = 0; i < df.getRowCount(); i++) {
            if (df.getValue("FIRST", i).equals(fn) && df.getValue("LAST", i).equals(ln)) {
                return i;
            }
        }
        return -1;
    }

    public  void writeToJSON(String title) throws IOException {
        new JSONWriter().writeToJson(df,title);
    }

    public int[] getAges(){
        ArrayList<Integer> ages = new ArrayList<>();
        for(int i = 0; i < df.getRowCount(); i++){
            String[] bornArray = df.getValue("BIRTHDATE", i).split("-");
            int deathY;
            int bornY = Integer.parseInt(bornArray[0]);
            String death = df.getValue("DEATHDATE",i);
            if(death.equals("")){ deathY = 2021; }
            else{
                String[] deathArray = death.split("-");
                deathY = Integer.parseInt(deathArray[0]);
            }
            int age = deathY - bornY;
            ages.add(age);
        }
        int[] agesArray = new int[ages.size()];
        for(int i = 0; i < ages.size(); i++){
            agesArray[i] = ages.get(i);
        }
        return agesArray;
    }
}
