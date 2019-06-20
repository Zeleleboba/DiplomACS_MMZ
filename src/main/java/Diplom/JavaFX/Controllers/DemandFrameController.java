package Diplom.JavaFX.Controllers;

import Diplom.hibernate.dao.DepartmentsSectionsEntity;
import Diplom.hibernate.dao.ProfessionsEntity;
import Diplom.hibernate.dao.WorkersEntity;
import Diplom.hibernate.dao.WorkingDemandEntity;
import Diplom.hibernate.util.HibernateSessionFactory;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.hibernate.Query;
import org.hibernate.Session;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DemandFrameController {


    @FXML   private TableView<WorkingDemandEntity> tableDemand;
    @FXML   private TableColumn<WorkingDemandEntity, String> colDepartmentName;
    @FXML   private TableColumn<WorkingDemandEntity, String> colAreaName;
    @FXML   private TableColumn<WorkingDemandEntity, String> colProfessionName;
    @FXML   private TableColumn<WorkingDemandEntity, Date> colDate;
    @FXML   private TableColumn<WorkingDemandEntity, Double> colRequiredTime;
    @FXML   private ComboBox cbDepartment;
    @FXML   private ComboBox cbArea;
    @FXML   private ComboBox cbProfession;
    @FXML   private DatePicker dpMonth;
    @FXML   private TextField fieldTime;
    @FXML   private TextField filterDepartment;
    @FXML   private TextField filterArea;
    @FXML   private TextField filterProfession;
    @FXML   private Button btnAdd;
    @FXML   private Button btnSave;
    @FXML   private Button btnDelete;
    @FXML   private Button btnExport;




    private Executor exec;
    @FXML
    private void initialize () throws SQLException, ClassNotFoundException {
        exec = Executors.newCachedThreadPool((runnable) -> {
            Thread t = new Thread (runnable);
            t.setDaemon(true);
            return t;
        });
        tableDemand.setEditable(true);
        tableDemand.getSelectionModel().cellSelectionEnabledProperty().set(false);

        colDepartmentName.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getDepartment().getDepartmentParentName()));
        colAreaName.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getDepartment().getDepartmentName()));
        colProfessionName.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getProfession().getProfessionName()));
        colDate.setCellValueFactory(new PropertyValueFactory<>("DemandDate"));
        colRequiredTime.setCellValueFactory(new PropertyValueFactory<>("RequiredTime"));
        loadOnForm();
    }

    private void loadOnForm() throws SQLException, ClassNotFoundException {
        fillDemandTable();
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        ObservableList<ProfessionsEntity> profData = FXCollections.observableList(session.createQuery("FROM ProfessionsEntity").list());
        ObservableList<DepartmentsSectionsEntity> departmentsData = FXCollections.observableList(session.createQuery("FROM DepartmentsSectionsEntity where Department_Parent_Id = 0").list());
        int parentId = tableDemand.getSelectionModel().getSelectedItem().getDepartment().getDepartmentParentId();
        Query query = session.createQuery("FROM DepartmentsSectionsEntity where Department_Parent_Id = :parent_id");
        query.setParameter("parent_id", parentId);
        ObservableList<DepartmentsSectionsEntity> areasData = FXCollections.observableList(query.list());
        cbProfession.getSelectionModel().selectFirst();

        ObservableList depList = FXCollections.observableArrayList();

        ObservableList areaList = FXCollections.observableArrayList();
        ObservableList profList = FXCollections.observableArrayList();
        for(DepartmentsSectionsEntity item1 : departmentsData){
            depList.add(item1.getDepartmentName());
        }

        for(DepartmentsSectionsEntity item2 : areasData){
            areaList.add(item2.getDepartmentName());
        }

        for(ProfessionsEntity item3 : profData){
            profList.add(item3.getProfessionName());
        }
        cbProfession.setItems(profList);
        cbDepartment.setItems(depList);
        cbArea.setItems(areaList);
    }


    private void fillDemandTable()throws SQLException, ClassNotFoundException {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Query query = session.createQuery("FROM WorkingDemandEntity");
        List<WorkingDemandEntity> demands = query.list();
        tableDemand.setItems(FXCollections.observableList(demands));
        tableDemand.getSelectionModel().selectFirst();
        session.close();
    }
    @FXML   private void getSelectedDemand(){
        WorkingDemandEntity demand = tableDemand.getSelectionModel().getSelectedItem();

        if(demand != null) {
            cbDepartment.setValue(demand.getDepartment().getDepartmentParentName());
            cbArea.setValue(demand.getDepartment().getDepartmentName());
            cbProfession.setValue(demand.getProfession().getProfessionName());
            fieldTime.setText(demand.getRequiredTime().doubleValue() + "");
            dpMonth.setValue(demand.getDemandDate().toLocalDate());
        }
    }

    @FXML   private void exportData() throws IOException {
        List<WorkingDemandEntity> demands = getData();
        Workbook book = new HSSFWorkbook();
        Sheet sheet = book.createSheet("Потребность в нормочасах");
        int i = 1;
        DataFormat format = book.createDataFormat();
        CellStyle dateStyle = book.createCellStyle();
        dateStyle.setDataFormat(format.getFormat("dd.mm.yyyy"));

        Row row = sheet.createRow(0);
        org.apache.poi.ss.usermodel.Cell departmentName = row.createCell(0);
        org.apache.poi.ss.usermodel.Cell areaName = row.createCell(1);
        org.apache.poi.ss.usermodel.Cell professionName = row.createCell(2);
        org.apache.poi.ss.usermodel.Cell demandDate = row.createCell(3);
        org.apache.poi.ss.usermodel.Cell requiredTime = row.createCell(4);
        departmentName.setCellValue("Цех");
        areaName.setCellValue("Участок");
        professionName.setCellValue("Профессия");
        demandDate.setCellValue("Дата");
        requiredTime.setCellValue("Требуемое время, нормочасов");
        for(WorkingDemandEntity demand : demands){
            row = sheet.createRow(i);
            departmentName = row.createCell(0);
            areaName = row.createCell(1);
            professionName = row.createCell(2);
            demandDate = row.createCell(3);
            requiredTime = row.createCell(4);
            departmentName.setCellValue(demand.getDepartment().getDepartmentParentName());
            areaName.setCellValue(demand.getDepartment().getDepartmentName());
            professionName.setCellValue(demand.getProfession().getProfessionName());
            demandDate.setCellValue(demand.getDemandDate());
            requiredTime.setCellValue(demand.getRequiredTime());
            sheet.autoSizeColumn(i);
            i++;
        }
        Stage secondStage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранить данные");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Книга xls","*.xls"));
        File file = fileChooser.showSaveDialog(secondStage);
        if (file != null) {
            book.write(new FileOutputStream(file.getPath()));
            book.close();
        }
    }

    @FXML   private List<WorkingDemandEntity> getData(){
        String department = filterDepartment.getText();
        String area = filterArea.getText();
        String profession = filterArea.getText();

        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Query query = session.createQuery("FROM WorkingDemandEntity");
        List<WorkingDemandEntity> demands = query.list();
        session.close();
        return demands;
    }
}
