package Diplom.JavaFX.Controllers;

import Diplom.hibernate.dao.ProfessionsEntity;
import Diplom.hibernate.util.HibernateSessionFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ContextMenuEvent;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;



import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ProfessionsFrameController {
    @FXML
    private TableView<ProfessionsEntity> tableProfessions;
    @FXML
    private TableColumn<ProfessionsEntity, Integer> colProfCode;
    @FXML
    private TableColumn<ProfessionsEntity, String> colProfName;
    @FXML
    private TextField fieldProfCode;
    @FXML
    private TextField fieldProfName;
    @FXML
    private Button btnAddProf;
    @FXML
    private Button btnDeleteProf;
    @FXML
    private Button btnSaveProf;

    ContextMenu contextMenu = new ContextMenu();
    MenuItem itemNew = new MenuItem("Добавить...");
    MenuItem itemDelete = new MenuItem("Удалить...");
    MenuItem itemEdit = new MenuItem("Редактировать...");
    SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();

    private Executor exec;
    @FXML
    private void initialize () throws SQLException, ClassNotFoundException {
        exec = Executors.newCachedThreadPool((runnable) -> {
            Thread t = new Thread (runnable);
            t.setDaemon(true);
            return t;
        });
        tableProfessions.setEditable(true);
        tableProfessions.getSelectionModel().cellSelectionEnabledProperty().set(true);

        colProfName.setEditable(true);
        colProfCode.setCellValueFactory(new PropertyValueFactory<>("ProfessionCode"));
        colProfName.setCellValueFactory(new PropertyValueFactory<>("ProfessionName"));
        tableProfessions.getSelectionModel().selectFirst();
        loadOnForm();
        loadContextMenu();
    }

    private void loadOnForm() throws SQLException, ClassNotFoundException {
     fillProfessionsTable();

    }

    private void fillProfessionsTable()throws SQLException, ClassNotFoundException {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Query query = session.createQuery("FROM ProfessionsEntity");
        List<ProfessionsEntity> professions = query.list();
        tableProfessions.setItems(FXCollections.observableList(professions));
        tableProfessions.getSelectionModel().selectFirst();
        session.close();
        getSelectedProfession();

    }


    private void loadContextMenu(){
        contextMenu.getItems().addAll(itemNew, separatorMenuItem,itemDelete, itemEdit);
        itemDelete.setOnAction(event ->{
            ProfessionsEntity professionsEntity = tableProfessions.getSelectionModel().getSelectedItem();

            Session session = HibernateSessionFactory.getSessionFactory().openSession();
            session.beginTransaction();
            session.delete(professionsEntity);
            session.getTransaction().commit();
            session.close();

            getSelectedProfession();
            try {
                fillProfessionsTable();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
        itemNew.setOnAction(event ->{
            ProfessionsEntity professionsEntity = new ProfessionsEntity();
            professionsEntity.setProfessionName("Новая профессия");
            professionsEntity.setProfessionCode(-1);
            Session session = HibernateSessionFactory.getSessionFactory().openSession();
            session.beginTransaction();
            session.save(professionsEntity);
            session.getTransaction().commit();
            session.close();


            try {
                fillProfessionsTable();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
        itemEdit.setOnAction(event->{

        });
        tableProfessions.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
            @Override
            public void handle(ContextMenuEvent event) {
                contextMenu.show(tableProfessions, event.getScreenX(), event.getScreenY());
            }
        });
    }

    @FXML
    private void getSelectedProfession(){
        ProfessionsEntity profession;
        if(tableProfessions.getSelectionModel().getSelectedItem()!= null){
            profession = tableProfessions.getSelectionModel().getSelectedItem();
            fieldProfCode.setText(profession.getProfessionCode()+"");
            fieldProfName.setText(profession.getProfessionName());
        }
    }



    @FXML
    private void saveProfession() throws SQLException, ClassNotFoundException {
        String newProfessionName = fieldProfName.getText();
        int newProfCode = Integer.parseInt(fieldProfCode.getText());
        ProfessionsEntity professionsEntity = tableProfessions.getSelectionModel().getSelectedItem();
        professionsEntity.setProfessionName(newProfessionName);
        professionsEntity.setProfessionCode(newProfCode);
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        session.beginTransaction();
        session.update(professionsEntity);
        session.getTransaction().commit();
        session.close();


        fillProfessionsTable();
    }
    @FXML
    private void newProfession() throws SQLException, ClassNotFoundException {

        ProfessionsEntity professionsEntity = new ProfessionsEntity();
        professionsEntity.setProfessionName("Новая профессия");
        professionsEntity.setProfessionCode(-1);
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(professionsEntity);
        session.getTransaction().commit();
        session.close();


        fillProfessionsTable();
    }
    @FXML
    private void deleteProfession() throws SQLException, ClassNotFoundException {
        ProfessionsEntity professionsEntity = tableProfessions.getSelectionModel().getSelectedItem();
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        session.beginTransaction();
        session.delete(professionsEntity);
        session.getTransaction().commit();
        session.close();


        fillProfessionsTable();
    }



}
