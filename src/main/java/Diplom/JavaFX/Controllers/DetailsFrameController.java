package Diplom.JavaFX.Controllers;

import Diplom.hibernate.dao.DetailsEntity;
import Diplom.hibernate.util.HibernateSessionFactory;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import org.hibernate.Query;
import org.hibernate.Session;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DetailsFrameController {

    @FXML
    private TreeTableView<DetailsEntity> ttvDetails;
    @FXML
    private TreeTableColumn<DetailsEntity, String> colDetailName;

    @FXML
    private TreeTableColumn<DetailsEntity, String> colDetailOperation;
    @FXML
    private TreeTableColumn<DetailsEntity, String> colDetailDepartment;
    @FXML
    private TreeTableColumn<DetailsEntity, String> colDetailArea;
    @FXML
    private TreeTableColumn<DetailsEntity, String> colDetailProfession;
    @FXML
    private TreeTableColumn<DetailsEntity, Double> colDetailOperationTime;
    @FXML
    private TextField fieldDetName;
    @FXML
    private TextField fieldDetStage;
    @FXML
    private TextArea areaDetailOperationName;
    @FXML
    private ComboBox cbDetailDepartment;
    @FXML
    private ComboBox cbDetailArea;
    @FXML
    private ComboBox cbDetailProfession;
    @FXML
    private TextField fieldDetailperationTime;
    @FXML
    private Button btnUpdateInfo;



    ContextMenu contextMenu = new ContextMenu();
    MenuItem itemNew = new MenuItem("Добавить...");
    MenuItem itemDelete = new MenuItem("Удалить...");
    MenuItem itemEdit = new MenuItem("Редактировать...");
    MenuItem itemNewDetail= new MenuItem("Деталь");
    MenuItem itemNewOperation = new MenuItem("Процесс");
    SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();
    Menu menuNew = new Menu("Добавить..");

    private Executor exec;
    @FXML
    private void initialize () {
        exec = Executors.newCachedThreadPool((runnable) -> {
            Thread t = new Thread (runnable);
            t.setDaemon(true);
            return t;
        });
        colDetailName.setCellValueFactory(new TreeItemPropertyValueFactory<DetailsEntity, String>("DetailName"));

        ttvDetails.setShowRoot(false);
        ttvDetails.getSelectionModel().setCellSelectionEnabled(true);
        loadDetails();



    }
    DetailsEntity detail = new DetailsEntity();

    TreeItem<DetailsEntity> itemRoot = new TreeItem<DetailsEntity>(detail);


    public void loadDetails(){
        DetailsEntity detailsEntity = new DetailsEntity();
        detailsEntity.setDetailName("КОрень");

        TreeItem<DetailsEntity> det_item;

        TreeItem<DetailsEntity> itemRoot = new TreeItem<>(detailsEntity);
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Query query = session.createQuery("FROM DetailsEntity");
        List<DetailsEntity> detailsEntities = query.list();
        ObservableList<DetailsEntity> detailsData= FXCollections.observableList(detailsEntities);

        for(DetailsEntity item : detailsData){
            det_item =new TreeItem<>(item);

            itemRoot.getChildren().addAll(det_item);
        }
        ttvDetails.setRoot(itemRoot);


    }
}
