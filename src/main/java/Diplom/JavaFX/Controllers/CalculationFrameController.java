package Diplom.JavaFX.Controllers;

import Diplom.hibernate.util.DBUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
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
    @FXML private AnchorPane lowerAnchorPane;
    final CategoryAxis xAxis = new CategoryAxis();
    final NumberAxis yAxis = new NumberAxis();

    private LineChart chartStaffCalc = new LineChart(xAxis, yAxis);
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
        chartStaffCalc.setLayoutX(0);
        chartStaffCalc.setLayoutY(0);

        lowerAnchorPane.getChildren().addAll(chartStaffCalc);
        AnchorPane.setRightAnchor(chartStaffCalc,0.0);
        AnchorPane.setTopAnchor(chartStaffCalc, 0.0);
        AnchorPane.setLeftAnchor(chartStaffCalc,0.0);
        AnchorPane.setBottomAnchor(chartStaffCalc, 0.0);
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

        chartStaffCalc.getData().clear();
        XYChart.Series series_start_working = new XYChart.Series();
        XYChart.Series series_working = new XYChart.Series();
        XYChart.Series series_required = new XYChart.Series();

        series_start_working.setName("Норма-часов начальное");
        series_working.setName("Норма-часов оптимизированное");
        series_required.setName("Норма-часов необходимо.");
        xAxis.setLabel("Даты");
        chartStaffCalc.setTitle("Анализ численности персонала");
        tableCalculation.getColumns().clear();
        String StmtExec = "exec ExtractStaff2 0,'"+cbDepartments.getValue().toString()+"','"+cbAreas.getValue().toString()+"','"+cbProfessions.getValue().toString()+"',["+dpOpenDate.getValue()+"],["+dpCloseDate.getValue()+"] \n"+
                "Select * from PivotTable";

        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();


        try {
            ResultSet SelectRS =  DBUtil.dbExecuteQuery(StmtExec);
            int countColumns = SelectRS.getMetaData().getColumnCount();
            int count_iter = (countColumns - 3)/4;
            for(int i=0; i<countColumns; i++){
                if(i < 3) {
                    final int j = i;
                    String column_name = SelectRS.getMetaData().getColumnName(i + 1);
                    TableColumn col = new TableColumn(column_name);

                    if (column_name.contains("Department")) col.setText("Цех");
                    else if (column_name.contains("Area")) col.setText("Участок");
                    else if (column_name.contains("Profession")) col.setText("Профессия");
                    col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
                        @Override
                        public ObservableValue call(TableColumn.CellDataFeatures param) {
                            String row_value = param.getValue().toString();
                            int length_row = row_value.length();
                            row_value = row_value.substring(1, length_row - 1);


                            return new SimpleStringProperty(row_value.split(",")[j]);
                        }
                    });
                    tableCalculation.getColumns().add(col);

                }
            }
            for(int i=0; i<count_iter; i++){
                String column_name_date = SelectRS.getMetaData().getColumnName(4*i + 4+3);
                TableColumn column_date = new TableColumn(column_name_date);
                String column_start = "Start";//SelectRS.getMetaData().getColumnName(4*i + 1+3);
                String column_work = "Opt";//SelectRS.getMetaData().getColumnName(4*i + 2+3);
                String column_demand = "Req";//SelectRS.getMetaData().getColumnName(4*i + 3+3);
                TableColumn column_start_working_time = new TableColumn(column_start);
                int finalI = i;
                column_start_working_time.setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
                    @Override
                    public ObservableValue call(TableColumn.CellDataFeatures param) {
                        String row_value = param.getValue().toString();
                        int length_row = row_value.length();
                        row_value = row_value.substring(1, length_row - 1);
                        return new SimpleStringProperty(row_value.split(",")[4* finalI + 0+3]);
                    }
                });
                TableColumn column_working_time = new TableColumn(column_work);
                column_working_time.setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
                    @Override
                    public ObservableValue call(TableColumn.CellDataFeatures param) {
                        String row_value = param.getValue().toString();
                        int length_row = row_value.length();
                        row_value = row_value.substring(1, length_row - 1);
                        return new SimpleStringProperty(row_value.split(",")[4* finalI + 1+3]);
                    }
                });
                TableColumn column_required_time = new TableColumn(column_demand);
                column_required_time.setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
                    @Override
                    public ObservableValue call(TableColumn.CellDataFeatures param) {
                        String row_value = param.getValue().toString();
                        int length_row = row_value.length();
                        row_value = row_value.substring(1, length_row - 1);
                        return new SimpleStringProperty(row_value.split(",")[4* finalI + 2+3]);
                    }
                });
                column_date.getColumns().addAll(column_start_working_time,column_working_time, column_required_time);
                tableCalculation.getColumns().addAll(column_date);


            }

            while(SelectRS.next()){
                ObservableList<String> row = FXCollections.observableArrayList();
                int iter = 1;
                for(int i=1 ; i<=SelectRS.getMetaData().getColumnCount(); i++){
                    if((i - 3) % 4 ==1){
                        iter++;
                        series_start_working.getData().add(new XYChart.Data(SelectRS.getString(i+3), Double.parseDouble(SelectRS.getString(i))));
                        series_working.getData().add(new XYChart.Data(SelectRS.getString(i+3), Double.parseDouble(SelectRS.getString(i+1))));
                        series_required.getData().add(new XYChart.Data(SelectRS.getString(i+3), Double.parseDouble(SelectRS.getString(i+2))));
                    }
                    row.add(SelectRS.getString(i));
                }

                data.add(row);
            }
            tableCalculation.setItems(data);
            tableCalculation.setLayoutX(0);

            tableCalculation.setEditable(true);

            tableCalculation.setPrefWidth(1700);
            tableCalculation.isTableMenuButtonVisible();

            chartStaffCalc.getData().addAll(series_start_working, series_working, series_required);
            upperAnchorPane.getChildren().addAll(tableCalculation);
            AnchorPane.setBottomAnchor(tableCalculation,0.0);
            AnchorPane.setTopAnchor(tableCalculation,70.0);

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
