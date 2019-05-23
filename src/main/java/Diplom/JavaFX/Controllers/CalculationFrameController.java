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
        colDepartment.setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
            @Override
            public ObservableValue call(TableColumn.CellDataFeatures param) {
                String row_value = param.getValue().toString();
                int length_row = row_value.length();
                row_value = row_value.substring(1, length_row - 1);
                return new SimpleStringProperty(row_value.split(",")[0]);
            }
        });
        colArea.setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
            @Override
            public ObservableValue call(TableColumn.CellDataFeatures param) {
                String row_value = param.getValue().toString();
                int length_row = row_value.length();
                row_value = row_value.substring(1, length_row - 1);
                return new SimpleStringProperty(row_value.split(",")[1]);
            }
        });
        colProfession.setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
            @Override
            public ObservableValue call(TableColumn.CellDataFeatures param) {
                String row_value = param.getValue().toString();
                int length_row = row_value.length();
                row_value = row_value.substring(1, length_row - 1);
                return new SimpleStringProperty(row_value.split(",")[2]);
            }
        });
        colDate.setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
            @Override
            public ObservableValue call(TableColumn.CellDataFeatures param) {
                String row_value = param.getValue().toString();
                int length_row = row_value.length();
                row_value = row_value.substring(1, length_row - 1);
                return new SimpleStringProperty(row_value.split(",")[3]);
            }
        });
        colCountWorkers.setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
            @Override
            public ObservableValue call(TableColumn.CellDataFeatures param) {
                String row_value = param.getValue().toString();
                int length_row = row_value.length();
                row_value = row_value.substring(1, length_row - 1);
                return new SimpleStringProperty(row_value.split(",")[4]);
            }
        });
        colCountWorkersOptim.setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
            @Override
            public ObservableValue call(TableColumn.CellDataFeatures param) {
                String row_value = param.getValue().toString();
                int length_row = row_value.length();
                row_value = row_value.substring(1, length_row - 1);
                return new SimpleStringProperty(row_value.split(",")[5]);
            }
        });
        colCountWorkersRequired.setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
            @Override
            public ObservableValue call(TableColumn.CellDataFeatures param) {
                String row_value = param.getValue().toString();
                int length_row = row_value.length();
                row_value = row_value.substring(1, length_row - 1);
                return new SimpleStringProperty(row_value.split(",")[6]);
            }
        });
        colWorkersDiff.setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
            @Override
            public ObservableValue call(TableColumn.CellDataFeatures param) {
                String row_value = param.getValue().toString();
                int length_row = row_value.length();
                row_value = row_value.substring(1, length_row - 1);
                return new SimpleStringProperty(row_value.split(",")[7]);
            }
        });
        colFondBegin.setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
            @Override
            public ObservableValue call(TableColumn.CellDataFeatures param) {
                String row_value = param.getValue().toString();
                int length_row = row_value.length();
                row_value = row_value.substring(1, length_row - 1);
                return new SimpleStringProperty(row_value.split(",")[8]);
            }
        });
        colFondOptim.setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
            @Override
            public ObservableValue call(TableColumn.CellDataFeatures param) {
                String row_value = param.getValue().toString();
                int length_row = row_value.length();
                row_value = row_value.substring(1, length_row - 1);
                return new SimpleStringProperty(row_value.split(",")[9]);
            }
        });
        colFondRequired.setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
            @Override
            public ObservableValue call(TableColumn.CellDataFeatures param) {
                String row_value = param.getValue().toString();
                int length_row = row_value.length();
                row_value = row_value.substring(1, length_row - 1);
                return new SimpleStringProperty(row_value.split(",")[10]);
            }
        });
        colFondDiff.setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
            @Override
            public ObservableValue call(TableColumn.CellDataFeatures param) {
                String row_value = param.getValue().toString();
                int length_row = row_value.length();
                row_value = row_value.substring(1, length_row - 1);
                return new SimpleStringProperty(row_value.split(",")[11]);
            }
        });
        colOverworking.setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
            @Override
            public ObservableValue call(TableColumn.CellDataFeatures param) {
                String row_value = param.getValue().toString();
                int length_row = row_value.length();
                row_value = row_value.substring(1, length_row - 1);
                return new SimpleStringProperty(row_value.split(",")[12]);
            }
        });
        colWorkingSaturdays.setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
            @Override
            public ObservableValue call(TableColumn.CellDataFeatures param) {
                String row_value = param.getValue().toString();
                int length_row = row_value.length();
                row_value = row_value.substring(1, length_row - 1);
                return new SimpleStringProperty(row_value.split(",")[13]);
            }
        });
        colDepartmentMove.setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
            @Override
            public ObservableValue call(TableColumn.CellDataFeatures param) {
                String row_value = param.getValue().toString();
                int length_row = row_value.length();
                row_value = row_value.substring(1, length_row - 1);
                return new SimpleStringProperty(row_value.split(",")[14]);
            }
        });
        colAreaMove.setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
            @Override
            public ObservableValue call(TableColumn.CellDataFeatures param) {
                String row_value = param.getValue().toString();
                int length_row = row_value.length();
                row_value = row_value.substring(1, length_row - 1);
                return new SimpleStringProperty(row_value.split(",")[15]);
            }
        });
        colProfessionMove.setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
            @Override
            public ObservableValue call(TableColumn.CellDataFeatures param) {
                String row_value = param.getValue().toString();
                int length_row = row_value.length();
                row_value = row_value.substring(1, length_row - 1);
                return new SimpleStringProperty(row_value.split(",")[16]);
            }
        });
        colWorkersToMove.setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
            @Override
            public ObservableValue call(TableColumn.CellDataFeatures param) {
                String row_value = param.getValue().toString();
                int length_row = row_value.length();
                row_value = row_value.substring(1, length_row - 1);
                return new SimpleStringProperty(row_value.split(",")[17]);
            }
        });

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
                try {
                    OnSelectionPivot();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        tableCalculation.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                try {
                    OnSelectionPivot();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        try {
            ResultSet SelectRS =  DBUtil.dbExecuteQuery(StmtExec);
            int countColumns = SelectRS.getMetaData().getColumnCount();
            int count_iter = (countColumns - 6)/4;
            for(int i=0; i<countColumns; i++){
                if(i < 6) {
                    final int j = i;
                    String column_name = SelectRS.getMetaData().getColumnName(i +1);
                    TableColumn col = new TableColumn(column_name);
                    if (column_name.contains("Department_Name")) col.setText("Цех");
                    else if (column_name.contains("Area_Name")) col.setText("Участок");
                    else if (column_name.contains("Profession_Name")) col.setText("Профессия");

                    if (column_name.contains("Department_Id")) col.setVisible(false);
                    else if (column_name.contains("Area_Id")) col.setVisible(false);
                    else if (column_name.contains("Profession_Code")) col.setVisible(false);
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
                String column_name_date = SelectRS.getMetaData().getColumnName(4*i + 4+6);
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
                        return new SimpleStringProperty(row_value.split(",")[4* finalI + 0+6]);
                    }
                });
                TableColumn column_working_time = new TableColumn(column_work);
                column_working_time.setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
                    @Override
                    public ObservableValue call(TableColumn.CellDataFeatures param) {
                        String row_value = param.getValue().toString();
                        int length_row = row_value.length();
                        row_value = row_value.substring(1, length_row - 1);
                        return new SimpleStringProperty(row_value.split(",")[4* finalI + 1+6]);
                    }
                });
                TableColumn column_required_time = new TableColumn(column_demand);
                column_required_time.setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
                    @Override
                    public ObservableValue call(TableColumn.CellDataFeatures param) {
                        String row_value = param.getValue().toString();
                        int length_row = row_value.length();
                        row_value = row_value.substring(1, length_row - 1);
                        return new SimpleStringProperty(row_value.split(",")[4* finalI + 2+6]);
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
            tableCalculation.setPrefWidth(2000);
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
    @FXML private void OnSelectionPivot() throws SQLException, ClassNotFoundException {
        ObservableList list_row = tableCalculation.getSelectionModel().getSelectedItem();

        chartStaffCalc.getData().clear();
        XYChart.Series series_start_working = new XYChart.Series();
        XYChart.Series series_working = new XYChart.Series();
        XYChart.Series series_required = new XYChart.Series();
        String department, area, profession, open_date, close_date;
        department = list_row.get(0).toString();
        area = list_row.get(1).toString();
        profession = list_row.get(2).toString();
        open_date = dpOpenDate.getValue().toString();
        close_date = dpCloseDate.getValue().toString();
        String query = "select b.department_parent_name, b.Department_Name, c.Profession_Name, a.CalculationDate, a.Count_Workers, a.Count_Workers_Calculated, round(a.Required_Time/(a.FifthDays*8),0) as WorkersRequired, \n" +
                "round((a.Required_Time - a.WorkingTime)/(a.FifthDays*8), 0) as StaffNeed,\n" +
                "a.StartWorkingTime, a.WorkingTime, a.Required_Time, round(a.WorkingTime - a.Required_Time, 0) as FondDiff,\n" +
                "a.OverWorkingPerMonth, (a.WorkingDays -a.FifthDays)*a.Count_Workers * 8 as WorkingSaturdays, p.department_parent_name, p.Department_Name, p.Profession_Name, p.Count_Workers\n" +
                "from CalculationInformation a\n" +
                "join Departments_Sections b on a.area_id = b.Department_Id\n" +
                "join Professions c on a.Profession_Code = c.Profession_Id\n" +
                "left join (\n" +
                "\tselect a.Id_to as Id, c.department_parent_name, c.Department_Name, d.Profession_Name, b.CalculationDate, a.Count_Workers\n" +
                "\tfrom WorkersMovements a\n" +
                "\tjoin CalculationInformation b on a.Id_to = b.Calc_Id\n" +
                "\tjoin Departments_Sections c on b.area_id = c.Department_Id\n" +
                "\tjoin Professions d on b.Profession_Code = d.Profession_Id\n" +
                "\tunion\n" +
                "\tselect a.Id_from, c.department_parent_name, c.Department_Name, d.Profession_Name, b.CalculationDate, -a.Count_Workers\n" +
                "\tfrom WorkersMovements a\n" +
                "\tjoin CalculationInformation b on a.Id_from = b.Calc_Id\n" +
                "\tjoin Departments_Sections c on b.area_id = c.Department_Id\n" +
                "\tjoin Professions d on b.Profession_Code = d.Profession_Id) as p on a.Calc_Id = p.Id\n" +
                "where a.Department_Id = "+department+" and a.Area_Id = "+area+" and a.Profession_Code = "+profession +"  and a.CalculationDate between '"+open_date +"' and '"+close_date+"'";

        ResultSet resultSet = DBUtil.dbExecuteQuery(query);
        ObservableList<ObservableList<String>> moreInfo = FXCollections.observableArrayList();
        while(resultSet.next()){
            ObservableList<String> row = FXCollections.observableArrayList();
            for(int i=1 ; i<=resultSet.getMetaData().getColumnCount(); i++){
                row.add(resultSet.getString(i));
            }

            moreInfo.add(row);
        }
        tableAdditionalInformation.setItems(moreInfo);
        for(int i=6 ; i<list_row.size(); i++){
            if((i - 5) % 4 ==0){

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
