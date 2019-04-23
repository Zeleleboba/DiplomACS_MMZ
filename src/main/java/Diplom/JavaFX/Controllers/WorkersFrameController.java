package Diplom.JavaFX.Controllers;

import Diplom.hibernate.dao.DepartmentsSectionsEntity;
import Diplom.hibernate.dao.ProfessionsEntity;
import Diplom.hibernate.dao.WorkersEntity;
import Diplom.hibernate.util.HibernateSessionFactory;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.hibernate.Query;
import org.hibernate.Session;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class WorkersFrameController {

    @FXML
    private TableView<WorkersEntity> tableWorkers;
    @FXML
    private TableColumn<WorkersEntity, String> colWorkSurname;
    @FXML
    private TableColumn<WorkersEntity, String> colWorkName;
    @FXML
    private TableColumn<WorkersEntity, String> colWorkPatronymic;
    @FXML
    private TableColumn<WorkersEntity, String> colWorkProf;
    @FXML
    private TableColumn<WorkersEntity, String> colWorkDep;
    @FXML
    private TableColumn<WorkersEntity, String> colWorkArea;
    @FXML
    private TableColumn<WorkersEntity, Double> colWorkTime;
    @FXML
    private TextField fieldWorkSurname;
    @FXML
    private TextField fieldWorkName;
    @FXML
    private TextField fieldWorkPatronymic;
    @FXML
    private TextField fieldWorkProf;
    @FXML
    private ComboBox<DepartmentsSectionsEntity> cbWorkDep;

    @FXML
    private ComboBox<DepartmentsSectionsEntity> cbWorkArea;
    @FXML
    private TextField fieldWorkTime;
    @FXML
    private ComboBox<ProfessionsEntity> cbWorkProf;


    private Executor exec;
    @FXML
    private void initialize () throws SQLException, ClassNotFoundException {
        exec = Executors.newCachedThreadPool((runnable) -> {
            Thread t = new Thread (runnable);
            t.setDaemon(true);
            return t;
        });
        tableWorkers.setEditable(true);
        tableWorkers.getSelectionModel().cellSelectionEnabledProperty().set(false);
        tableWorkers.getSelectionModel().selectFirst();
        colWorkName.setCellValueFactory(new PropertyValueFactory<>("WorkerName"));
        colWorkSurname.setCellValueFactory(new PropertyValueFactory<>("WorkerSurname"));
        colWorkPatronymic.setCellValueFactory(new PropertyValueFactory<>("WorkerPatronymic"));
        colWorkTime.setCellValueFactory(new PropertyValueFactory<>("WorkerCountHours"));
        colWorkProf.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getProfession().getProfessionName()));
        colWorkArea.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getDepartment().getDepartmentName()));
        colWorkDep.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getDepartment().getDepartmentParentName()));

        loadOnForm();
    }

    private void loadOnForm() throws SQLException, ClassNotFoundException {
        fillWorkersTable();



        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        ObservableList<ProfessionsEntity> profData = FXCollections.observableList(session.createQuery("FROM ProfessionsEntity").list());
        ObservableList<DepartmentsSectionsEntity> departmentsData = FXCollections.observableList(session.createQuery("FROM DepartmentsSectionsEntity where Department_Parent_Id = 0").list());
        int parentId = tableWorkers.getSelectionModel().getSelectedItem().getDepartment().getDepartmentParentId();
        Query query = session.createQuery("FROM DepartmentsSectionsEntity where Department_Parent_Id = :parent_id");
        query.setParameter("parent_id", parentId);
        ObservableList<DepartmentsSectionsEntity> areasData = FXCollections.observableList(query.list());

        cbWorkProf.setCellFactory(new Callback<ListView<ProfessionsEntity>, ListCell<ProfessionsEntity>>() {
            @Override
            public ListCell<ProfessionsEntity> call(ListView<ProfessionsEntity> param) {
                final ListCell<ProfessionsEntity> cell = new ListCell<ProfessionsEntity>(){
                    @Override
                    protected void updateItem(ProfessionsEntity professionsEntity, boolean bln){
                        super.updateItem(professionsEntity, bln);
                        if(professionsEntity != null){
                            setText(professionsEntity.getProfessionName());
                        }
                        else{
                            setText(null);

                        }
                    }
                };
                return cell;
            }
        });

        cbWorkArea.setCellFactory(new Callback<ListView<DepartmentsSectionsEntity>, ListCell<DepartmentsSectionsEntity>>() {
            @Override
            public ListCell<DepartmentsSectionsEntity> call(ListView<DepartmentsSectionsEntity> param) {
                final ListCell<DepartmentsSectionsEntity> cell = new ListCell<DepartmentsSectionsEntity>(){
                    @Override
                    protected void updateItem(DepartmentsSectionsEntity departmentsSectionsEntity, boolean bln){
                        super.updateItem(departmentsSectionsEntity, bln);
                        if(departmentsSectionsEntity != null){
                            setText(departmentsSectionsEntity.getDepartmentName());
                        }
                        else{
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        });
        cbWorkDep.setCellFactory(new Callback<ListView<DepartmentsSectionsEntity>, ListCell<DepartmentsSectionsEntity>>() {
            @Override
            public ListCell<DepartmentsSectionsEntity> call(ListView<DepartmentsSectionsEntity> param) {
                final ListCell<DepartmentsSectionsEntity> cell = new ListCell<DepartmentsSectionsEntity>(){
                    @Override
                    protected void updateItem(DepartmentsSectionsEntity departmentsSectionsEntity, boolean bln){
                        super.updateItem(departmentsSectionsEntity, bln);
                        if(departmentsSectionsEntity != null){
                            setText(departmentsSectionsEntity.getDepartmentName());
                        }
                        else{
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        });

        cbWorkProf.setItems(profData);
        cbWorkDep.setItems(departmentsData);
        cbWorkArea.setItems(areasData);


        getSelectedWorker();

    }

    @FXML
    public void getSelectedWorker() {
        WorkersEntity workersEntity;
        if (tableWorkers.getSelectionModel().getSelectedItem() != null) {
            workersEntity = tableWorkers.getSelectionModel().getSelectedItem();

            fieldWorkSurname.setText(workersEntity.getWorkerSurname());
            fieldWorkName.setText(workersEntity.getWorkerName());
            fieldWorkPatronymic.setText(workersEntity.getWorkerPatronymic());
            fieldWorkTime.setText(workersEntity.getWorkerCountHours()+"");
            cbWorkProf.setValue(workersEntity.getProfession());
            cbWorkProf.getSelectionModel().selectFirst();

            if(tableWorkers.getSelectionModel().getSelectedItem().getDepartment() != null){
                cbWorkDep.setValue(tableWorkers.getSelectionModel().getSelectedItem().getDepartment());

            }
            else{
                cbWorkDep.setValue(null);
            }
            if(tableWorkers.getSelectionModel().getSelectedItem().getDepartment().getDepartmentName() != null){
                cbWorkArea.setValue(tableWorkers.getSelectionModel().getSelectedItem().getDepartment());
            }
            else{
                cbWorkArea.setValue(null);
            }
        }
    }

    private void fillWorkersTable()throws SQLException, ClassNotFoundException {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Query query = session.createQuery("FROM WorkersEntity");
        List<WorkersEntity> workers = query.list();
        tableWorkers.setItems(FXCollections.observableList(workers));
        tableWorkers.getSelectionModel().selectFirst();


        session.close();

    }

}
