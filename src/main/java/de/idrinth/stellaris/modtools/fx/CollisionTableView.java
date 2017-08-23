package de.idrinth.stellaris.modtools.fx;

import de.idrinth.stellaris.modtools.model.Collision;
import de.idrinth.stellaris.modtools.model.PatchedFile;
import java.util.Collection;

public class CollisionTableView extends ClickableTableView<PatchedFile,Collision> {
    public CollisionTableView() {
        super("File,Mods".split(","));
    }
    @Override
    public final void addItems(Collection<Collision> collisions) {
        super.getItems().clear();
        collisions.forEach((collision) -> {
            PatchedFile file = collision.get();
            if(file.isPatched()) {
                super.getItems().add(file);
            }
        });
    }
}
