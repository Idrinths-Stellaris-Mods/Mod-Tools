package de.idrinth.stellaris.modtools;

import de.idrinth.stellaris.modtools.entity.ModCollection;
import de.idrinth.stellaris.modtools.parser.ModFiles;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class FXMLController implements Initializable {
    
    @FXML
    private Label label;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        try {
            label.setText("Mods found: "+String.valueOf(new ModFiles().get(new ModCollection()).getMods().values().size()));
        } catch (Exception exception) {
            label.setText("Error: "+exception.getLocalizedMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
}
