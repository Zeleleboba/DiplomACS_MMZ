package Diplom.JavaFX.Controllers;

import Diplom.hibernate.dao.DepartmentsSectionsEntity;
import Diplom.hibernate.util.HibernateSessionFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.input.ContextMenuEvent;
import org.hibernate.Query;
import org.hibernate.Session;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DepartmentsFrameController {


    @FXML
    private TreeTableView<DepartmentsSectionsEntity> ttvDepartments;
    @FXML
    private TreeTableColumn<DepartmentsSectionsEntity, Integer> colDepCode;
    @FXML
    private TreeTableColumn<DepartmentsSectionsEntity, String> colDepName;
    @FXML
    private Label lbDepCode;
    @FXML
    private Label lbDepName;
    @FXML
    private TextField fieldDepCode;
    @FXML
    private TextField fieldDepName;

    @FXML
    private Button btnAddDep;
    @FXML
    private Button btnSaveDep;
    @FXML
    private Button btnDeleteDep;

    ContextMenu contextMenu = new ContextMenu();
    MenuItem itemNew = new MenuItem("Добавить...");
    MenuItem itemDelete = new MenuItem("Удалить...");
    MenuItem itemEdit = new MenuItem("Редактировать...");
    MenuItem itemNewDepartment = new MenuItem("Цех");
    MenuItem itemNewArea = new MenuItem("Участок");
    SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();
    Menu menuNew = new Menu("Добавить..");

    private Executor exec;
    @FXML
    private void initialize () throws SQLException, ClassNotFoundException {
        exec = Executors.newCachedThreadPool((runnable) -> {
            Thread t = new Thread (runnable);
            t.setDaemon(true);
            return t;
        });
        ttvDepartments.setEditable(true);
        ttvDepartments.getSelectionModel().cellSelectionEnabledProperty().set(true);
        colDepCode.setCellValueFactory(new TreeItemPropertyValueFactory<DepartmentsSectionsEntity, Integer>("DepartmentCode"));
        colDepName.setCellValueFactory(new TreeItemPropertyValueFactory<DepartmentsSectionsEntity, String>("DepartmentName"));

        loadContextMenu();
        loadDepartmets();
    }
    private void loadContextMenu(){
        menuNew.getItems().addAll(itemNewDepartment, itemNewArea);
        contextMenu.getItems().addAll(menuNew, separatorMenuItem,itemDelete, itemEdit);
        itemDelete.setOnAction(event ->{
            DepartmentsSectionsEntity departmentsEntity = ttvDepartments.getSelectionModel().getSelectedItem().getValue();
            Session session = HibernateSessionFactory.getSessionFactory().openSession();
            session.beginTransaction();
            session.delete(departmentsEntity);
            session.getTransaction().commit();
            session.close();

        });
        itemNewDepartment.setOnAction(event ->{
            DepartmentsSectionsEntity departmentsEntity = new DepartmentsSectionsEntity();
            departmentsEntity.setDepartmentCode(-1);
            departmentsEntity.setDepartmentName("Новый цех");
            departmentsEntity.setDepartmentParentId(0);
            Session session = HibernateSessionFactory.getSessionFactory().openSession();
            session.beginTransaction();
            session.save(departmentsEntity);
            session.getTransaction().commit();
            session.close();

            loadDepartmets();

        });
        itemNewArea.setOnAction(event ->{
            int parentId = ttvDepartments.getSelectionModel().getSelectedItem().getValue().getDepartmentId();
            String parentName = ttvDepartments.getSelectionModel().getSelectedItem().getValue().getDepartmentName();
            DepartmentsSectionsEntity departmentsEntity = new DepartmentsSectionsEntity();
            departmentsEntity.setDepartmentCode(-1);
            departmentsEntity.setDepartmentName("Новый участок");
            departmentsEntity.setDepartmentParentName(parentName);
            departmentsEntity.setDepartmentParentId(parentId);

            Session session = HibernateSessionFactory.getSessionFactory().openSession();
            session.beginTransaction();
            session.save(departmentsEntity);
            session.getTransaction().commit();
            session.close();

            loadDepartmets();

        });
        itemEdit.setOnAction(event->{

        });
        ttvDepartments.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
            @Override
            public void handle(ContextMenuEvent event) {
                contextMenu.show(ttvDepartments, event.getScreenX(), event.getScreenY());
            }
        });
    }
    @FXML
    private void getSelectedDepartment(){
        DepartmentsSectionsEntity departmentsSectionsEntity;
        if(ttvDepartments.getSelectionModel().getSelectedItem()!= null){
            departmentsSectionsEntity = ttvDepartments.getSelectionModel().getSelectedItem().getValue();
            fieldDepCode.setText(departmentsSectionsEntity.getDepartmentCode()+"");
            fieldDepName.setText(departmentsSectionsEntity.getDepartmentName());
        }
    }

    public void loadDepartmets(){
        DepartmentsSectionsEntity departmentsEntity = new DepartmentsSectionsEntity();
        departmentsEntity.setDepartmentName("Корень цехов");
        TreeItem<DepartmentsSectionsEntity> itemRoot = new TreeItem<DepartmentsSectionsEntity>(departmentsEntity);
        TreeItem<DepartmentsSectionsEntity> dep_parent, dep_child;
        try {
            Session session = HibernateSessionFactory.getSessionFactory().openSession();
            Query query = session.createQuery("FROM DepartmentsSectionsEntity where Department_Parent_Id = 0");
            List<DepartmentsSectionsEntity> departmentsEntities = query.list();
            ObservableList<DepartmentsSectionsEntity> depData = FXCollections.observableList(departmentsEntities);
            for (DepartmentsSectionsEntity item :depData){
                dep_parent = new TreeItem<DepartmentsSectionsEntity>(item);
                int Parent_id = item.getDepartmentId();

                Query query1 = session.createQuery("FROM DepartmentsSectionsEntity where Department_Parent_Id = :parent_id");
                query1.setParameter("parent_id", Parent_id);
                List<DepartmentsSectionsEntity> departmentsChildEntities = query1.list();
                ObservableList<DepartmentsSectionsEntity> depChildData = FXCollections.observableList(departmentsChildEntities);
                for(DepartmentsSectionsEntity item1 : depChildData){
                    dep_child = new TreeItem<DepartmentsSectionsEntity>(item1);
                    dep_parent.getChildren().addAll(dep_child);
                }
                itemRoot.getChildren().addAll(dep_parent);
            }
            ttvDepartments.setRoot(itemRoot);
            ttvDepartments.setShowRoot(false);
            ttvDepartments.getSelectionModel().selectFirst();
            getSelectedDepartment();
            session.close();
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    @FXML
    private void updateDepartment(){
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        DepartmentsSectionsEntity departmentsSectionsEntity =  ttvDepartments.getSelectionModel().getSelectedItem().getValue();
        departmentsSectionsEntity.setDepartmentName(fieldDepName.getText());
        departmentsSectionsEntity.setDepartmentCode(Integer.parseInt(fieldDepCode.getText()));
        session.beginTransaction();
        session.update(departmentsSectionsEntity);
        session.getTransaction().commit();
        session.close();
        //HibernateSessionFactory.shutdown();
        loadDepartmets();
    }

    @FXML
    private void deleteDepartment(){
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        DepartmentsSectionsEntity departmentsSectionsEntity =  ttvDepartments.getSelectionModel().getSelectedItem().getValue();
        session.beginTransaction();
        session.delete(departmentsSectionsEntity);
        session.getTransaction().commit();
        session.close();
        //HibernateSessionFactory.shutdown();
        loadDepartmets();
    }
    @FXML
    private void addDepartment(){
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        DepartmentsSectionsEntity departmentsSectionsEntity = new DepartmentsSectionsEntity();
        departmentsSectionsEntity.setDepartmentCode(-1);
        departmentsSectionsEntity.setDepartmentName("Новый цех");
        departmentsSectionsEntity.setDepartmentParentId(0);
        session.beginTransaction();
        session.save(departmentsSectionsEntity);
        session.getTransaction().commit();
        session.close();
        //HibernateSessionFactory.shutdown();
        loadDepartmets();
    }
}
