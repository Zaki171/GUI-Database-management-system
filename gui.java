import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class gui extends JFrame{
    private String title;
    private JPanel backPanel;
    private JPanel buttonPanel;
    private JPanel headPanel;
    private JScrollPane scroller;
    private JPanel searchPanel;
    private JButton loadButton;
    private Model model;
    private JTable table;
    private DefaultTableModel tm;
    private ArrayList<String> currentDisplay = new ArrayList<>();

    public gui (){
        createGUI();
        setPreferredSize(new Dimension(600,400));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
        setVisible(true);
    }

    private void createGUI(){
        createLoadButtonPanel();
        createTable();
        createBackPanel();
    }

    private void createBackPanel(){
        backPanel = new JPanel(new BorderLayout());
        backPanel.add(buttonPanel, BorderLayout.SOUTH);
        backPanel.add(scroller,BorderLayout.CENTER);
        add(backPanel, BorderLayout.CENTER);
    }
    private void createSearchPanel(){
        searchPanel = new JPanel(new GridLayout(0,1,3,10));
        createOldButton();
        createNumInPlaceButton();
        createSearchButton();
        createSearchButtonTwo();
        createSearchButtonThree();
        if(title.charAt(title.length()-1)=='v'){//we don't want the convert to JSON option with a JSON file already loaded
            createJSONButton();
        }
        createDisplayAgeChartButton();

    }
    private void createTable(){
        tm = new DefaultTableModel();
        table = new JTable(tm);
        scroller = new JScrollPane(table);
    }

    private void createLoadButtonPanel(){
        buttonPanel = new JPanel(new BorderLayout());
        createLoadButton();
        buttonPanel.add(loadButton);
    }
    private void createOldButton(){
        JButton oldButton = new JButton("Find oldest person...");
        oldButton.addActionListener((ActionEvent e)->displayOldest());
        searchPanel.add(oldButton);
    }

    private void createLoadButton(){
        loadButton = new JButton("Load file...");
        loadButton.addActionListener((ActionEvent e)->loadFile());
    }

    private void createNumInPlaceButton(){
        JButton numPlaceButton = new JButton("Find number of people living in...");
        numPlaceButton.addActionListener((ActionEvent e)->displayNumPlace());
        searchPanel.add(numPlaceButton);
    }

    private void createSearchButton() {
        JButton searchButton = new JButton("Search for a patient by ID...");
        searchButton.addActionListener((ActionEvent e)->searchForPatient("ID"));
        searchPanel.add(searchButton);
    }

    private void createSearchButtonTwo(){
        JButton searchButtonTwo = new JButton("Search for a patient by first and last name...");
        searchButtonTwo.addActionListener((ActionEvent e)->searchByNames());
        searchPanel.add(searchButtonTwo);
    }

    private void createSearchButtonThree(){
        JButton searchButtonThree = new JButton("Search for a patient by passport...");
        searchButtonThree.addActionListener((ActionEvent e)->searchForPatient("PASSPORT"));
        searchPanel.add(searchButtonThree);
    }

    private void createJSONButton(){
        JButton JSONButton = new JButton("Write to JSON file...");
        JSONButton.addActionListener((ActionEvent e)->convertToJSON());
        searchPanel.add(JSONButton);
    }

    private void createDisplayAgeChartButton(){
        JButton displayAgeChart = new JButton("Display age distribution...");
        displayAgeChart.addActionListener((ActionEvent e)->displayChart());
        searchPanel.add(displayAgeChart);
    }

    private void loadFile(){
        JFileChooser fc = new JFileChooser(".");
        fc.setFileFilter(new FileNameExtensionFilter("CSV files and JSON files","csv","json"));
        fc.setAcceptAllFileFilterUsed(false);//only csv files and json files can be loaded
        int returnVal = fc.showOpenDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION){
            File file = fc.getSelectedFile();
            String fn = file.getAbsolutePath();
            try{
                title = file.getName();
                setModel(fn);
                backPanel.setBorder(BorderFactory.createTitledBorder(title));

            }
            catch(IOException e){ JOptionPane.showMessageDialog(this,"Unable to load the file","File error",JOptionPane.ERROR_MESSAGE); }

        }
    }

    private void convertToJSON(){
        try{
            model.writeToJSON(title);
            JOptionPane.showMessageDialog(this,"Data written to JSON file");
        }catch(IOException e){
            JOptionPane.showMessageDialog(this,"Unable to write to file","File error",JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearTableModel(){
        tm.setColumnCount(0);
        tm.setRowCount(0);
    }
    private void setModel(String fn) throws IOException {
        model = new Model(fn);
        createHeaderPanel();
        addTitleLayout();
        unselectBoxes();
        clearTableModel();
        currentDisplay.clear();
        checkIfPatients();

    }
    private void checkIfPatients() {//checks if the file loaded is the patient file
        if(searchPanel!=null){
            searchPanel.removeAll();
            backPanel.remove(searchPanel);
        }
        if(model.checkIfPatients()){
            createSearchPanel();
            backPanel.add(searchPanel,BorderLayout.EAST);
        }
        backPanel.revalidate();
    }

    private void displayChart(){
        int[] data = model.getAges();
        new ChartPanel(data);
    }

    private void unselectBoxes(){
        Component[] comps = headPanel.getComponents();
        for(Component comp:comps){
            if(comp instanceof JCheckBox){
                ((JCheckBox) comp).setSelected(false);
            }
        }
        headPanel.revalidate();
    }
    private void displayNumPlace(){
        String userInput = JOptionPane.showInputDialog("Enter CITY:");
        if(!(userInput ==null)){
            int numPeople = model.getNumPeople(userInput);
            String output = String.format("Number of people living in %s : %d",userInput,numPeople);
            JOptionPane.showMessageDialog(backPanel,output);
        }

    }

    private void searchForPatient(String colName){
        String message = String.format("Enter %s: ",colName);
        String userInput = JOptionPane.showInputDialog(message);
        if(!(userInput == null || userInput.replaceAll(" ", "").equals(""))){//if the user types nothing the program should not search, as some patients may have blank values
            int row = model.getPatientIndex(colName,userInput);
            if(row == -1){
                table.clearSelection();
                String errorMessage = String.format("Could not find patient with %s: %s",colName,userInput);
                JOptionPane.showMessageDialog(backPanel,errorMessage);
            }
            else{ showSelection(row); }
        }
    }
    private void searchByNames(){//must search with first and last names due to duplicates
        String userFirst = JOptionPane.showInputDialog("Enter first name");
        if(!(userFirst==null)){
            String userLast = JOptionPane.showInputDialog("Enter last name");
            if(!(userLast==null)){//done in this way so that if the user closes box for first name box for last name doesn't reappear
                int row = model.getFirstLastIndex(userFirst,userLast);
                if(row == -1){
                    table.clearSelection();
                    String errorMessage = String.format("Could not find patient with first name: %s, last name: %s",userFirst,userLast);
                    JOptionPane.showMessageDialog(backPanel,errorMessage);
                }
                else{ showSelection(row); }
            }
        }
    }
    private void showSelection(int row){//highlighting the table
        selectAllBoxes();
        table.clearSelection();
        Rectangle rect = table.getCellRect(row,0,true);
        table.scrollRectToVisible(rect);
        table.addRowSelectionInterval(row,row);
    }

    private void selectAllBoxes(){
        Component[] comps = headPanel.getComponents();
        for(Component comp:comps){
            if(comp instanceof JCheckBox){
                ((JCheckBox) comp).setSelected(true);
            }
        }
        headPanel.revalidate();
    }

    private void displayOldest() {
        String[] data = model.findOldestPerson();
        String message = String.join("\n",data);
        JOptionPane.showMessageDialog(backPanel,message);
    }

    private void addCheckBox(String colName){
        JCheckBox checkBox = new JCheckBox(colName);
        checkBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED){
                currentDisplay.add(colName);
                displayCol(colName);
            }
            else{ removeCol(colName); }
        });
        headPanel.add(checkBox);
    }

    private void removeCol(String colName){
        table.clearSelection();
        clearTableModel();
        currentDisplay.remove(colName);
        for(String name: currentDisplay){
            displayCol(name);
        }
        table.setModel(tm);
        table.revalidate();
        repaint();
    }

    private void displayCol(String colName){
        tm.addColumn(colName,model.getColData(colName).toArray());
        table.revalidate();
    }

    private void createHeaderPanel(){
        if(headPanel!=null){
            backPanel.remove(headPanel);
        }
        headPanel = new JPanel(new GridLayout(model.getNumberHeaders(),1,10,3));
        headPanel.setBorder(BorderFactory.createEtchedBorder());
        backPanel.add(headPanel,BorderLayout.WEST);
        backPanel.revalidate();
    }

    private void addTitleLayout(){
        headPanel.removeAll();
        ArrayList<String> headers = model.getHeaders();
        for(String header: headers){
            addCheckBox(header);
        }
        headPanel.revalidate();
    }

    public JLabel createLabel(String word){
        return new JLabel(word);
    }
}
