package de.idrinth.stellaris.modtools.fx;

import de.idrinth.stellaris.modtools.model.Mod;
import javafx.beans.property.SimpleStringProperty;

public class ModFx {
    protected SimpleStringProperty description;
    protected SimpleStringProperty id;
    protected SimpleStringProperty name;
    protected SimpleStringProperty version;

    public ModFx(Mod mod) {
        id = new SimpleStringProperty(String.valueOf(mod.getId()));
        name = new SimpleStringProperty(mod.getName());
        version = new SimpleStringProperty(mod.getVersion());
        description = new SimpleStringProperty(mod.getDescription());
    }

    public String getId() {
        return id.get();
    }

    public String getName() {
        return name.get();
    }

    public String getVersion() {
        return version.get();
    }

    public String getDescription() {
        return description.get();
    }
    
}
