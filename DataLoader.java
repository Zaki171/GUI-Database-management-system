import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class DataLoader {

    public DataFrame readFile(String fn) throws IOException {
        Scanner sc = new Scanner(new File(fn));
        DataFrame df = getColumns(sc);
        while (sc.hasNextLine()){
            String[] row = sc.nextLine().split(",");
            String[] checkedRow = checkForAll(row,df.getColumnCount());
            ArrayList<String> colNames = df.getColumnNames();
            for (int i = 0; i<checkedRow.length;i++){
                String item = checkedRow[i];
                df.addValue(colNames.get(i),item);
            }
        }
        sc.close();
        return df;
    }
    private String[] checkForAll(String[] row, int len){//used in case the ZIP column is blank
        if(row.length!=len){
            String[] newRow = new String[row.length+1];
            for(int i = 0; i < row.length; i++){ newRow[i] = row[i]; }
            newRow[row.length] = "";
            return newRow;
        }
        else{
            return row;
        }
    }
    private DataFrame getColumns(Scanner sc){//retrieve column names first
        DataFrame df = new DataFrame();
        String[] headers = sc.nextLine().split(",");
        for (String header : headers) {
            Column col = new Column(header);
            df.addColumn(col);
        }
        return df;

    }





}
