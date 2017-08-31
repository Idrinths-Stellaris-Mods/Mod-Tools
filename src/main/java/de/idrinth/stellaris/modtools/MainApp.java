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

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.apache.commons.io.IOUtils;

public class MainApp extends Application {

    private static EntityManagerFactory entityManager;
    public static String version;

    @Override
    public void start(Stage stage) throws Exception {
        try{
            version = IOUtils.toString(getClass().getResourceAsStream("/version"),"utf-8");
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Scene.fxml"));
            stage.setTitle("Idrinth's Stellaris Mod-Tools");
            stage.setScene(new Scene(root));
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/logo.png")));
            stage.show();
            stage.setOnCloseRequest((WindowEvent e) -> {
                Platform.exit();
            });
            entityManager = Persistence.createEntityManagerFactory("de.idrinth_Stellaris.ModTools");
        } catch(Exception e) {
            System.out.println(e.getLocalizedMessage());
            throw e;
        }
    }
    @Override
    public void stop() {
        System.exit(0);
    }
    private static int getRandom() {
        return (int)(Math.random()*900)+100;
    }
    public static EntityManager getEntityManager() {
        try {
            try {
                Thread.sleep(getRandom());
            } catch (InterruptedException ex) {
                Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
            }
            return entityManager.createEntityManager();
        } catch(javax.persistence.PersistenceException exe) {
            try {
                Thread.sleep(getRandom());
            } catch (InterruptedException ex) {
                Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
            }
            return getEntityManager();
        }
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
