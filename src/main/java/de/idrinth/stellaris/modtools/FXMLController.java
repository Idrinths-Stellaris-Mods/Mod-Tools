package de.idrinth.stellaris.modtools;

import com.github.sarxos.winreg.RegistryException;
import de.idrinth.stellaris.modtools.fx.CollisionTableView;
import de.idrinth.stellaris.modtools.fx.ModTableView;
import de.idrinth.stellaris.modtools.model.ModCollection;
import de.idrinth.stellaris.modtools.parser.ModFiles;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FXMLController implements Initializable {
    @FXML
    private Label description;
    
    @FXML
    private ModTableView mods;
        
    @FXML
    private CollisionTableView collisions;
    
    private Stage popup;

    public Stage getPopup() throws IOException {
        if(popup == null) {
            popup = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setController(this);
            popup.setScene(new Scene(loader.load(getClass().getResource("/fxml/TextPopup.fxml").openStream())));
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.initOwner(mods.getScene().getWindow());
        }
        return popup;
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        try {
            ModCollection col = new ModCollection();
            new ModFiles().get(col);
            collisions.addItems(col.getFiles().values());
            mods.addItems(col.getMods().values());
        } catch (RegistryException | IOException exception) {
            showExceptionPopup(exception);
        }
    }

    @FXML
    private void showModPopup(Event event) throws IOException {
        getPopup().setTitle("Description: "+mods.getCurrent().getName());
        description.setText(mods.getCurrent().getDescription());
        getPopup().showAndWait();
    }


    @FXML
    private void showExceptionPopup(Exception exception) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(exception.getLocalizedMessage());
        alert.setHeaderText("Error");
        alert.showAndWait();
    }

    @FXML
    private void showCollisionPopup(Event event) throws IOException {
        getPopup().setTitle("Patched "+collisions.getCurrent().getFile());
        description.setText(collisions.getCurrent().getContent());
        getPopup().showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
}
