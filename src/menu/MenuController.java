package menu;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.jfoenix.controls.JFXTextField;
import helper.HelperFunctions;
import helper.Ranking;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import com.gembox.spreadsheet.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;

import methods.Promethee.*;

public class MenuController {

    // matrice MP type is string so we can add int and string values
    ObservableList<ObservableList<String>> mp = FXCollections.observableArrayList();
    // matrice PS
    ObservableList<ObservableList<String>> ps = FXCollections.observableArrayList();
    // Matrics pour la methode promethee
    ArrayList<ArrayList<String>> newMp = new ArrayList<>();
    // Le rangement final des actions
    ObservableList<Ranking> rankingList = FXCollections.observableArrayList();


    static {
        SpreadsheetInfo.setLicense("FREE-LIMITED-KEY");
    }

    @FXML
    private TableView mpTable;
    @FXML
    private JFXTextField l;
    @FXML
    private JFXTextField c;

    @FXML
    private TableView ranking;
    @FXML
    private TableColumn rangement;
    @FXML
    private TableColumn action;
    @FXML
    private TableColumn score;

    @FXML
    private void xlsxReader(ActionEvent event) throws IOException {
        // array temporaire
        ArrayList<String> temp = new ArrayList<>();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open file");
        File file = fileChooser.showOpenDialog(mpTable.getScene().getWindow());

        ExcelFile workbook = ExcelFile.load(file.getAbsolutePath());
        ExcelWorksheet worksheet = workbook.getWorksheet(0);
        String[][] sourceData = new String[100][26];
        for (int row = 0; row < sourceData.length; row++) {
            for (int column = 0; column < sourceData[row].length; column++) {
                ExcelCell cell = worksheet.getCell(row, column);
                if (cell.getValueType() != CellValueType.NULL){
                    sourceData[row][column] = cell.getValue().toString();
                    temp.add(cell.getValue().toString());
                }
            }
            newMp.add((ArrayList<String>) temp.clone());
            temp.clear();
        }
        fillTable(sourceData);
    }

    private void fillTable(String[][] dataSource) {
        mpTable.getColumns().clear();

        for (String[] row : dataSource)
            mp.add(FXCollections.observableArrayList(row));

        mpTable.setItems(mp);

        for (int i = 0; i < dataSource[0].length; i++) {
            final int currentColumn = i;
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(ExcelColumnCollection.columnIndexToName(currentColumn));
            column.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().get(currentColumn)));
            column.setEditable(true);
            column.setCellFactory(TextFieldTableCell.forTableColumn());
            column.setOnEditCommit(
                    (TableColumn.CellEditEvent<ObservableList<String>, String> t) -> {
                        t.getTableView().getItems().get(t.getTablePosition().getRow()).set(t.getTablePosition().getColumn(), t.getNewValue());
                    });
            mpTable.getColumns().add(column);
        }
    }

    @FXML
    private void ajouter(ActionEvent event) {
        System.out.println(Integer.valueOf(c.getText()));
        mpTable.getColumns().clear();
        for (int i = 0; i < Integer.valueOf(c.getText()); i++) {
            final int currentColumn = i;
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(ExcelColumnCollection.columnIndexToName(currentColumn));
            column.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().get(currentColumn)));
            column.setEditable(true);
            column.setCellFactory(TextFieldTableCell.forTableColumn());
            column.setOnEditCommit(
                    (TableColumn.CellEditEvent<ObservableList<String>, String> t) -> {
                        t.getTableView().getItems().get(t.getTablePosition().getRow()).set(t.getTablePosition().getColumn(), t.getNewValue());
                    });
            mpTable.getColumns().add(column);
        }
        for (int i = 0; i < 5; i++) {
            mpTable.getItems().add(FXCollections.observableArrayList());
        }
    }

    @FXML
    private void doPromethee(ActionEvent event) throws IOException{
        // fonction principale
        Promethee2 promethee = new Promethee2("C:\\Users\\NGSI\\Desktop\\Mahdi\\Mehdi etudes\\Master 2\\DCTW\\MP2.csv");
        promethee.calculate();
        // filling the table
        for(int i=1; i<promethee.getRanking().size()+1; i++)  {
            Ranking r = new Ranking(i, promethee.getAlternativeByRank(i).getName(), (float)promethee.getAlternativeByRank(i).getMpf());
            rankingList.add(r);
        }

        ranking.setItems(rankingList);

        rangement.setCellValueFactory(new PropertyValueFactory<Ranking, Integer>("rangement"));
        action.setCellValueFactory(new PropertyValueFactory<Ranking, String>("action"));
        score.setCellValueFactory(new PropertyValueFactory<Ranking, Float>("score"));

    }

    @FXML
    private void saveRanking(ActionEvent event) throws IOException{

        ExcelFile workbook = new ExcelFile();
        ExcelWorksheet worksheet = workbook.addWorksheet("ranking");

        // Write and format sample data to Excel cells.
        for (int i = 0; i < rankingList.size(); i++) {
            ExcelCell cell = worksheet.getCell(i, 0);
            cell.setValue(rankingList.get(i).getAction());
            cell = worksheet.getCell(i, 1);
            cell.setValue(rankingList.get(i).getRangement());
            cell = worksheet.getCell(i, 2);
            cell.setValue(rankingList.get(i).getScore());
        }
        HelperFunctions.fileChooserSave(event, workbook);
    }
}

