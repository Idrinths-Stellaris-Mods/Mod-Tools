package de.idrinth.stellaris.modtools.fx;

import de.idrinth.stellaris.modtools.model.Mod;
import java.util.Collection;

public class ModTableView extends ClickableTableView<ModFx,Mod> {
    public ModTableView() {
        super("Id,Name,Version".split(","));
    }
    @Override
    public final void addItems(Collection<Mod> mods) {
        super.getItems().clear();
        mods.forEach((mod) -> {
            super.getItems().add(new ModFx(mod));
        });
    }
}
