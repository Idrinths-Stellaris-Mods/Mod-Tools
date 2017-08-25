/*
 * Copyright (C) 2017 Idrinth
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
package de.idrinth.stellaris.modtools.access;

import de.idrinth.stellaris.modtools.MainApp;
import de.idrinth.stellaris.modtools.entity.ModKey;
import de.idrinth.stellaris.modtools.entity.Modification;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;

public class DatabaseRetriever {

    public static Object get(Class classname, Object id) {
        EntityManager manager = MainApp.entityManager.createEntityManager();
        Object o = manager.find(classname, id, LockModeType.WRITE);
        if (null != o) {
            return o;
        }
        for (Constructor con : classname.getConstructors()) {
            if (con.getParameterCount() == 1) {
                try {
                    if (!manager.getTransaction().isActive()) {
                        manager.getTransaction().begin();
                    }
                    o = con.newInstance(id);
                    manager.persist(o);
                    manager.getTransaction().commit();
                    return o;
                } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | InvocationTargetException ex) {
                    System.out.println(ex.getLocalizedMessage());
                }
            }
        }
        return null;
    }

    public static Modification getModification(String id) {
        EntityManager manager = MainApp.entityManager.createEntityManager();
        ModKey o = (ModKey) manager.find(ModKey.class, id);
        if (null != o) {
            return o.getModification();
        }
        if (!manager.getTransaction().isActive()) {
            manager.getTransaction().begin();
        }
        o = new ModKey(id);
        o.setModification(new Modification(id));
        manager.persist(o);
        manager.getTransaction().commit();
        return o.getModification();
    }
}
