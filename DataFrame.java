import java.util.ArrayList;

public class DataFrame {
    private ArrayList <Column> columns = new ArrayList<>();

    public void addColumn(Column column){
        this.columns.add(column);
    }

    public ArrayList<String> getColumnNames(){
        ArrayList<String> columnNames = new ArrayList<>();
        for (Column column : columns){
            String name = column.getName();
            columnNames.add(name);
        }
        return columnNames;
    }
    public int getRowCount(){
        Column col1 = columns.get(0);
        return col1.getSize();
    }

    public String getValue(String columnName, int row){
        for (Column column : columns){
            String name = column.getName();
            if (name.equals(columnName)){
                return column.getRowValue(row);
            }
        }
        return "Could not find column";

    }

    public void putValue(String columnName, int row, String value){
        for(Column column: columns){
            String name = column.getName();
            if(name.equals(columnName)){
                column.setRowValue(row, value);
            }
        }
    }

    public void addValue(String columnName, String value){
        for(Column column: columns){
            String name = column.getName();
            if(name.equals(columnName)){
                column.addRowValue(value);
            }
        }
    }
    public int getColumnCount() {
        return columns.size();
    }



}

