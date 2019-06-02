package Diplom.JavaFX.Controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.SwipeEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.Stack;

public class ParentFrameController {
    @FXML   private TreeView<String> treeViewACS;
    @FXML   private BorderPane bpParent;

    @FXML   private ToggleButton tbtnShowTree;
    private TreeItem<String> workers = new TreeItem<String>("Персонал");
    private TreeItem<String> departments = new TreeItem<String>("Цеха");

    private TreeItem<String> details = new TreeItem<String>("Номенклатура деталей");

    private TreeItem<String> professions = new TreeItem<String>("Профессии");
    private TreeItem<String> analyze = new TreeItem<String>("Анализ");
    private TreeItem<String> data_form = new TreeItem<String>("Данные");
    private TreeItem<String> calendar = new TreeItem<String>("Производственный календарь");

    TreeItem<String> demand = new TreeItem<String>("Потребность в нормо-часах");
    TreeItem<String> ACS_MMZ = new TreeItem<String>("Система управления персоналом");
    private  AnchorPane anchor_pane_get;

    double iconWidth = 20;
    double iconHeight = 20;
    public void initialize() throws IOException {
        tbtnShowTree.setSelected(true);
        try{
            data_form.getChildren().addAll(workers, departments, professions, demand);
            data_form.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/plan.png"), iconWidth, iconHeight, false, true)));
            workers.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/worker_id.png"), iconWidth, iconHeight, false, true)));
            departments.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/department.png"),iconWidth,iconHeight, false, true)));
            professions.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/profession.png"), iconWidth, iconHeight, false, true)));
            analyze.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/calculate.png"), iconWidth, iconHeight, false, true)));

            ACS_MMZ.getChildren().addAll(data_form, analyze);
            ACS_MMZ.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/snail.png"), iconWidth, iconHeight, false, true)));

            treeViewACS.setRoot(ACS_MMZ);
            treeViewACS.setShowRoot(true);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        FXMLLoader loader_main = new FXMLLoader();
        loader_main.setLocation(ParentFrameController.class.getResource("/Frames/MainFrame.fxml"));


        bpParent.setCenter(loader_main.load());
        treeViewACS.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                FXMLLoader loader = new FXMLLoader();
                if (newValue == professions) {
                    loader.setLocation(ParentFrameController.class.getResource("/Frames/ProfessionsFrame.fxml"));
                }
                else if(newValue == workers) {
                    loader.setLocation(ParentFrameController.class.getResource("/Frames/WorkersFrame.fxml"));
                }
                else if(newValue == departments) {
                    loader.setLocation(ParentFrameController.class.getResource("/Frames/DepartmentsFrame.fxml"));
                }
                else if(newValue == details) {
                    loader.setLocation(ParentFrameController.class.getResource("/Frames/DetailsFrame.fxml"));
                }
                else if(newValue == analyze) {
                    loader.setLocation(ParentFrameController.class.getResource("/Frames/CalculationFrame.fxml"));
                }
                else if(newValue == demand) {
                    loader.setLocation(ParentFrameController.class.getResource("/Frames/DemandFrame.fxml"));
                }
                else if(newValue == ACS_MMZ) {
                    loader.setLocation(ParentFrameController.class.getResource("/Frames/MainFrame.fxml"));
                }
                anchor_pane_get = loader.load();
                bpParent.setCenter(anchor_pane_get);

            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });



    }

    public void hideTree(){
        if(tbtnShowTree.isSelected() == true) {
            treeViewACS.setVisible(true);
            AnchorPane anc = (AnchorPane) bpParent.getCenter();
            bpParent.setCenter(null);
            bpParent.setLeft(treeViewACS);
            bpParent.setCenter(anc);
        }
        else {
            treeViewACS.setVisible(false);
            AnchorPane anc = (AnchorPane) bpParent.getCenter();

            bpParent.setCenter(null);
            bpParent.setLeft(null);
            bpParent.setCenter(anc);


        }
    }


}
