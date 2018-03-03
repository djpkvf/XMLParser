/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dugout;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author mpill
 */
public class DugoutController implements Initializable {
    
    private Stage stage;
    
    @FXML
    private VBox sceneRoot;
    
    @FXML
    private void handleOpenFile(ActionEvent event) {        
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try {
                Team team = DugoutXMLLoader.load(file);
                ArrayList<Player> players = team.getPlayers();
                
                buildTree(players);
                
            } catch(Exception ex) {
                displayAlert("Error parsing XML file", ex);
            }
        }
    }
    
    // All credit here goes to Dale
    public void displayAlert(String message, Exception ex) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Message");
        alert.setHeaderText("Exception!");
        alert.setContentText(message);

        // Create expandable Exception.
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("The exception stacktrace was:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        // Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(expContent);

        alert.showAndWait();
    }
    
    private void buildTree(ArrayList<Player> players) {
        TreeItem<String> root = new TreeItem<>("Roster");
        root.setExpanded(true);
        
        for( Player player : players ) {
            TreeItem<String> playerFirstname = new TreeItem<>(player.getFirstname());
            TreeItem<String> playerLastname = new TreeItem<>(player.getLastname());
            TreeItem<String> playerPosition = new TreeItem<>(player.getPosition());
            TreeItem<String> playerBattingAverage = new TreeItem<>(Double.toString(player.getBattingAverage()));
            
            TreeItem<String> playerID = new TreeItem<>(Integer.toString(player.getId()));
            
            playerID.getChildren().addAll(playerFirstname, playerLastname, playerPosition, playerBattingAverage);
            
            root.getChildren().add(playerID);
        }
        
        TreeTableColumn<String, String> column = new TreeTableColumn<>("Roster");
        column.setPrefWidth(150);
        
        column.setCellValueFactory((CellDataFeatures<String, String> p) -> new ReadOnlyStringWrapper(
                p.getValue().getValue()));
        
        TreeTableView<String> treeTableView = new TreeTableView<>(root);
        treeTableView.getColumns().add(column);
        
        sceneRoot.getChildren().add(treeTableView);
    }
    
    public void ready(Stage stage) {
        this.stage = stage;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
