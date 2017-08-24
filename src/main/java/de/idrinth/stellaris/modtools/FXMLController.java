/* 
 * Copyright (C) 2017 Björn Büttner
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.idrinth.stellaris.modtools;

import com.github.sarxos.winreg.RegistryException;
import de.idrinth.stellaris.modtools.fx.CollisionTableView;
import de.idrinth.stellaris.modtools.fx.ModTableView;
import de.idrinth.stellaris.modtools.model.Mod;
import de.idrinth.stellaris.modtools.model.ModCollection;
import de.idrinth.stellaris.modtools.model.PatchedFile;
import de.idrinth.stellaris.modtools.parser.ModFiles;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkEvent.EventType;
import org.codefx.libfx.control.webview.WebViews;

public class FXMLController implements Initializable {

    @FXML
    private WebView description;

    @FXML
    private ModTableView mods;

    @FXML
    private CollisionTableView collisions;

    private Stage popup;

    private Stage getPopup() throws IOException {
        if (popup == null) {
            popup = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setController(this);
            popup.setScene(new Scene(loader.load(getClass().getResource("/fxml/TextPopup.fxml").openStream())));
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.initOwner(mods.getScene().getWindow());
            description.setContextMenuEnabled(false);
            description.getEngine().setJavaScriptEnabled(false);
            description.getEngine().setUserAgent("Idrinth's Stellaris Mod Tools/" + getClass().getPackage().getImplementationVersion() + " (https://github.com/Idrinths-Stellaris-Mods/Mod-Tools)");
            WebViews.addHyperlinkListener(
                    description,
                    (HyperlinkEvent event) -> {
                        if (event.getEventType() == EventType.ACTIVATED) {
                            try {
                                Desktop.getDesktop().browse(new URI(event.getURL().toString()));
                            } catch (URISyntaxException | IOException ex) {
                                Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        return true;//do NOT navigate
                    },
                    EventType.ACTIVATED
            );
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
        Mod current = mods.getCurrent();
        if (current == null) {
            return;
        }
        getPopup().setTitle("Description: " + current.getName());
        description.getEngine().loadContent(current.getDescription());
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
        PatchedFile current = collisions.getCurrent();
        if (current == null) {
            return;
        }
        getPopup().setTitle("Patched " + current.getFile());
        description.getEngine().loadContent(current.getContent());
        getPopup().showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
}
