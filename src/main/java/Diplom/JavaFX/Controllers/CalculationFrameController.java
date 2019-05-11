package Diplom.JavaFX.Controllers;

import Diplom.hibernate.util.DBUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CalculationFrameController {

    @FXML private DatePicker dpOpenDate;
    @FXML private DatePicker dpCloseDate;
    @FXML private Button btnCalculate;
    @FXML private ComboBox cbDepartments;
    @FXML private ComboBox cbAreas;
    @FXML private ComboBox cbProfessions;
    @FXML private AnchorPane upperAnchorPane;

    String openDate, closeDate;

    TableView<ObservableList<String>> tableCalculation = new TableView<ObservableList<String>>();


    private Executor exec;



    @FXML
    private void initialize () throws SQLException, ClassNotFoundException {
        exec = Executors.newCachedThreadPool((runnable) -> {
            Thread t = new Thread (runnable);
            t.setDaemon(true);
            return t;
        });

        dpOpenDate.setValue(LocalDate.of(2018,01,01));
        dpCloseDate.setValue(LocalDate.of(2018,11,01));
        prepareData();
    }




    public void prepareData() throws SQLException, ClassNotFoundException {
        cbAreas.setDisable(true);
        cbProfessions.setDisable(true);
        String selectStmt = "SELECT DISTINCT Department_Name FROM StaffCalculation";
        try {
            ResultSet rsEmps = DBUtil.dbExecuteQuery(selectStmt);
            ObservableList<String> depaertmentList = FXCollections.observableArrayList();
            while (rsEmps.next()) {
                depaertmentList.add(rsEmps.getString("Department_Name"));
            }
            cbDepartments.setItems(depaertmentList);
            cbAreas.setDisable(false);
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("SQL select operation has been failed: " + e);
            throw e;
        }
        cbDepartments.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String selectArea = "SELECT DISTINCT Area_Name FROM StaffCalculation where Department_Name = '"+cbDepartments.getValue().toString()+"'";
                try {
                    ResultSet rsEmps = DBUtil.dbExecuteQuery(selectArea);
                    ObservableList<String> areaList = FXCollections.observableArrayList();
                    while (rsEmps.next()) {
                        areaList.add(rsEmps.getString("Area_Name"));
                    }
                    cbAreas.setItems(areaList);
                    cbAreas.setDisable(false);
                    cbProfessions.setDisable(true);
                } catch (SQLException | ClassNotFoundException e) {
                    System.out.println("SQL select operation has been failed: " + e);
                    try {
                        throw e;
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    } catch (ClassNotFoundException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        cbAreas.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String selectProfession = "SELECT DISTINCT Profession_Name FROM StaffCalculation where Department_Name = '"+cbDepartments.getValue().toString()+"' and Area_Name = '"+cbAreas.getValue().toString()+"'";
                try {
                    ResultSet rsEmps = DBUtil.dbExecuteQuery(selectProfession);
                    ObservableList<String> professionList = FXCollections.observableArrayList();
                    while (rsEmps.next()) {
                        professionList.add(rsEmps.getString("Profession_Name"));
                    }
                    cbProfessions.setItems(professionList);
                    cbProfessions.setDisable(false);
                } catch (SQLException | ClassNotFoundException e) {
                    System.out.println("SQL select operation has been failed: " + e);
                    try {
                        throw e;
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    } catch (ClassNotFoundException ex) {
                        ex.printStackTrace();
                    }
                }




            }
        });
    }


    @FXML private void CalculateAction(){


        tableCalculation.getColumns().clear();
        String StmtExec = "exec ExtractStaff2 0,'"+cbDepartments.getValue().toString()+"','"+cbAreas.getValue().toString()+"','"+cbProfessions.getValue().toString()+"',["+dpOpenDate.getValue()+"],["+dpCloseDate.getValue()+"] \n"+
                "Select * from PivotTable";

        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();


        try {
            ResultSet SelectRS =  DBUtil.dbExecuteQuery(StmtExec);
            int countColumns = SelectRS.getMetaData().getColumnCount();
            int count_iter = (countColumns - 3)/4;
            System.out.println(countColumns);
            for(int i=0 ; i<countColumns; i++){
                final int j = i;
                String column_name = SelectRS.getMetaData().getColumnName(i+1);
                if(!column_name.contains("Date")){
                    TableColumn col = new TableColumn();
                    col.setText(column_name);
                    if(column_name.contains("Department")) col.setText("Цех");
                    else if(column_name.contains("Area")) col.setText("Участок");
                    else if(column_name.contains("Profession")) col.setText("Профессия");
                    col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
                        @Override
                        public ObservableValue call(TableColumn.CellDataFeatures param) {
                            String row_value = param.getValue().toString();
                            int length_row = row_value.length();
                            row_value = row_value.substring(1,length_row-1);

                            String hren = row_value.split(",")[j];

                            return new SimpleStringProperty(hren);
                        }
                    });
                    tableCalculation.getColumns().add(col);
                }
            }

            while(SelectRS.next()){
                ObservableList<String> row = FXCollections.observableArrayList();
                for(int i=1 ; i<=SelectRS.getMetaData().getColumnCount(); i++){
                    row.add(SelectRS.getString(i));
                }

                data.add(row);
            }
            tableCalculation.setItems(data);
            tableCalculation.setLayoutX(0);
            tableCalculation.setLayoutY(66);
            tableCalculation.setEditable(true);
            tableCalculation.setPrefHeight(150);
            tableCalculation.setPrefWidth(1700);
            tableCalculation.isTableMenuButtonVisible();

            upperAnchorPane.getChildren().addAll(tableCalculation);

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("SQL select operation has been failed: " + e);
            try {
                throw e;
            } catch (SQLException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }


    }

}
