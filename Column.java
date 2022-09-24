import java.util.ArrayList;

public class Column {
    private String name;
    private ArrayList <String> rows = new ArrayList<>();
    private int rowNum;

    public Column(String name){
        this.name = name;
        this.rowNum = 0;
    }

    public String getName(){
        return this.name;
    }

    public int getSize(){
        return rowNum;
    }

    public String getRowValue(int index){
        return this.rows.get(index);
    }

    public void setRowValue(int index, String value) {
        this.rows.set(index, value);
    }

    public void addRowValue(String value){
        this.rows.add(value);
        rowNum++;
    }
    public int getRowIndex(String value){
        return rows.indexOf(value);
    }
}

