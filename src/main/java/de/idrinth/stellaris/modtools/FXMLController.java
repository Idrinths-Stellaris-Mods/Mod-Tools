package de.idrinth.stellaris.modtools;

import com.github.sarxos.winreg.RegistryException;
import de.idrinth.stellaris.modtools.fx.ObservableItemList;
import de.idrinth.stellaris.modtools.model.ModCollection;
import de.idrinth.stellaris.modtools.parser.ModFiles;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class FXMLController implements Initializable {
    
    @FXML
    private Label label;
    
    @FXML
    private ListView<String> mods;
        
    @FXML
    private ListView<String> collisions;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        try {
            ModCollection col = new ModCollection();
            new ModFiles().get(col);
            collisions.setItems(ObservableItemList.getCollection(col.getFiles().values()));
            mods.setItems(ObservableItemList.getMod(col.getMods().values()));
            label.setText("Done");
        } catch (RegistryException | IOException exception) {
            label.setText("Error: "+exception.getLocalizedMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
}
