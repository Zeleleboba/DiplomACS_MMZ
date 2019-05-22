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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLOutput;
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
    @FXML private CheckBox chbOverworking;
    @FXML private CheckBox chbWorkingSaturdays;
    @FXML private CheckBox chbDepartmentMove;
    @FXML private CheckBox chbAreaMove;
    @FXML private CheckBox chbReplace;
    @FXML private TableView tableAdditionalInformation;
    @FXML private TableColumn colDepartment;
    @FXML private TableColumn colArea;
    @FXML private TableColumn colProfession;
    @FXML private TableColumn colDate;
    @FXML private TableColumn colCountWorkers;
    @FXML private TableColumn colCountWorkersRequired;
    @FXML private TableColumn colCountWorkersOptim;
    @FXML private TableColumn colWorkersDiff;
    @FXML private TableColumn colFondBegin;
    @FXML private TableColumn colFondOptim;
    @FXML private TableColumn colFondRequired;
    @FXML private TableColumn colFondDiff;
    @FXML private TableColumn colOverworking;
    @FXML private TableColumn colWorkingSaturdays;
    @FXML private TableColumn colDepartmentMove;
    @FXML private TableColumn colAreaMove;
    @FXML private TableColumn colProfessionMove;
    @FXML private TableColumn colWorkersToMove;
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
        String selectStmt = "SELECT DISTINCT Department_Id FROM StaffCalculation order by Department_Id";
        try {
            ResultSet rsEmps = DBUtil.dbExecuteQuery(selectStmt);
            ObservableList<String> depaertmentList = FXCollections.observableArrayList();
            while (rsEmps.next()) {
                depaertmentList.add(rsEmps.getString("Department_Id"));
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
                String selectArea = "SELECT DISTINCT Area_Id FROM StaffCalculation where Department_Id = "+cbDepartments.getValue().toString()+" order by AREA_ID";
                try {
                    ResultSet rsEmps = DBUtil.dbExecuteQuery(selectArea);
                    ObservableList<String> areaList = FXCollections.observableArrayList();
                    while (rsEmps.next()) {
                        areaList.add(rsEmps.getString("Area_Id"));
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
                String selectProfession = "SELECT DISTINCT Profession_Code FROM StaffCalculation where Department_Id = '"+cbDepartments.getValue().toString()+"' and Area_Id = "+cbAreas.getValue().toString()+" order by Profession_COde";
                try {
                    ResultSet rsEmps = DBUtil.dbExecuteQuery(selectProfession);
                    ObservableList<String> professionList = FXCollections.observableArrayList();
                    professionList.add("Все");
                    while (rsEmps.next()) {
                        professionList.add(rsEmps.getString("Profession_Code"));
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
        int param_overtime = chbOverworking.isSelected() ? 1 : 0;
        int param_work_sat = chbWorkingSaturdays.isSelected() ? 1 : 0;
        int param_department_move = chbDepartmentMove.isSelected() ? 1 : 0;
        int param_area_move = chbAreaMove.isSelected() ? 1 : 0;
        int param_replace = chbReplace.isSelected() ? 1 : 0;
        String profession = cbProfessions.getValue().toString();
        if(profession =="Все") profession = "-1";

        String StmtExec = "exec CalculateWorkers "+cbDepartments.getValue()+", "+cbAreas.getValue()+", "+profession+",'"
                +dpOpenDate.getValue().toString()+"','"+dpCloseDate.getValue().toString()+"'," +
                param_overtime +", "+param_work_sat + ", " + param_area_move + ", " + param_department_move + ", " + param_replace;

        tableCalculation.getSelectionModel().selectFirst();

        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
        tableCalculation.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                OnSelectionPivot();
            }
        });
        tableCalculation.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                OnSelectionPivot();
            }
        });


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

                for(int i=1 ; i<=SelectRS.getMetaData().getColumnCount(); i++){

                    row.add(SelectRS.getString(i));
                }

                data.add(row);
            }
            tableCalculation.setItems(data);
            tableCalculation.setLayoutX(0);

            tableCalculation.setEditable(true);

            tableCalculation.setPrefWidth(1700);
            tableCalculation.isTableMenuButtonVisible();


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
    @FXML private void OnSelectionPivot(){
        ObservableList list_row = tableCalculation.getSelectionModel().getSelectedItem();

        chartStaffCalc.getData().clear();
        XYChart.Series series_start_working = new XYChart.Series();
        XYChart.Series series_working = new XYChart.Series();
        XYChart.Series series_required = new XYChart.Series();


        for(int i=3 ; i<list_row.size(); i++){
            if((i - 2) % 4 ==0){

                series_start_working.getData().add(new XYChart.Data(list_row.get(i), Double.parseDouble(list_row.get(i-3).toString())));
                series_working.getData().add(new XYChart.Data(list_row.get(i), Double.parseDouble(list_row.get(i-2).toString())));
                series_required.getData().add(new XYChart.Data(list_row.get(i), Double.parseDouble(list_row.get(i-1).toString())));

            }

        }
        series_start_working.setName("Нормочасов начальное");
        series_working.setName("Нормочасов оптимизированное");
        series_required.setName("Нормочасов необходимо.");
        chartStaffCalc.getData().addAll(series_start_working, series_working, series_required);
    }

}
