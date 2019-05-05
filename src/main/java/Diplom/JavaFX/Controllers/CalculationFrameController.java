package Diplom.JavaFX.Controllers;

import Diplom.hibernate.dao.StaffCalculationEntity;
import Diplom.hibernate.util.HibernateSessionFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import org.hibernate.Session;

import java.sql.SQLException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CalculationFrameController {

    @FXML private DatePicker dpOpenDate;
    @FXML private DatePicker dpCloseDate;
    @FXML private Button btnCalculate;
    @FXML private ComboBox cbDepartments;
    @FXML private ComboBox cbAreas;
    @FXML private ComboBox cbProfessions;


    String openDate, closeDate;


    private Executor exec;
    @FXML
    private void initialize () throws SQLException, ClassNotFoundException {
        exec = Executors.newCachedThreadPool((runnable) -> {
            Thread t = new Thread (runnable);
            t.setDaemon(true);
            return t;
        });
        prepareData();
    }




    public void prepareData(){
        cbAreas.setDisable(true);
        cbProfessions.setDisable(true);
        Session session = HibernateSessionFactory.getSessionFactory().openSession();


        ObservableList<StaffCalculationEntity> calculationData = FXCollections.observableList(session.createQuery("FROM StaffCalculationEntity").list());


        ObservableList depList = FXCollections.observableArrayList();
        ObservableList areaList = FXCollections.observableArrayList();
        ObservableList profList = FXCollections.observableArrayList();

        for(StaffCalculationEntity item : calculationData){

            if(!depList.contains(item.getDepartmentName())){
                depList.add(item.getDepartmentName());
            }

        }

        cbDepartments.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                cbProfessions.setDisable(false);
                String parent_name = cbDepartments.getValue().toString();
                ObservableList<StaffCalculationEntity> calculationDataDep = FXCollections.observableList(session.createQuery("FROM StaffCalculationEntity where Department_Name = '"+parent_name+"'").list());
                for(StaffCalculationEntity item : calculationDataDep){

                    if(!areaList.contains(item.getAreaName())){
                        areaList.add(item.getAreaName());
                    }

                }
                cbAreas.setItems(areaList);
                cbAreas.getSelectionModel().selectFirst();
                cbAreas.setDisable(false);
            }
        });
        cbAreas.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String department_name = cbDepartments.getValue().toString();
                String area_name = cbAreas.getValue().toString();
                ObservableList<StaffCalculationEntity> calculationDataArea = FXCollections.observableList(session.createQuery("FROM StaffCalculationEntity where Department_Name = '"+department_name+"' and Area_Name = '"+area_name+"'").list());
                for(StaffCalculationEntity item : calculationDataArea){

                    if(!profList.contains(item.getProfessionName())){
                       profList.add(item.getProfessionName());
                    }

                }






                cbProfessions.setItems(profList);
                cbProfessions.getSelectionModel().selectFirst();
                cbProfessions.setDisable(false);
            }
        });


        dpOpenDate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                openDate = dpOpenDate.getValue().toString();
            }
        });


        dpCloseDate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                closeDate = dpCloseDate.getValue().toString();
            }
        });
        cbDepartments.setItems(depList);





    }
}
