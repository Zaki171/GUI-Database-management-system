import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class JSONReader {
    public DataFrame readFile(String fn) throws FileNotFoundException {
        Scanner sc = new Scanner(new File(fn));
        DataFrame df = getColumns(sc);
        sc.close();
        Scanner sc2 = new Scanner(new File(fn));
        sc2.nextLine();
        while(sc2.hasNextLine()){
            if(sc2.nextLine().replaceAll(" ", "").equals("{")){
                for(int i = 0; i < 20; i++){
                    StringBuilder value = new StringBuilder(sc2.nextLine().split(":")[1]);
                    while(!(value.charAt(value.length()-1)=='"')){
                        value.deleteCharAt(value.length()-1);
                    }
                    value.deleteCharAt(value.length()-1);
                    value.deleteCharAt(0);
                    value.deleteCharAt(0);
                    String colName = df.getColumnNames().get(i);
                    df.addValue(colName,value.toString());
                }
            }
        }
        sc2.close();
        return df;

    }

    private DataFrame getColumns(Scanner sc){
        DataFrame df = new DataFrame();
        sc.nextLine();
        sc.nextLine();
        while(true){
            String line = sc.nextLine().replaceAll(" ","");
            if(!(line.equals("{"))){
                StringBuilder colName = new StringBuilder(line.split(":")[0]);
                colName.deleteCharAt(0);
                colName.deleteCharAt(colName.length()-1);
                Column col = new Column(colName.toString());
                df.addColumn(col);
            }
            else{
                break;
            }
        }
        return df;
    }
}
