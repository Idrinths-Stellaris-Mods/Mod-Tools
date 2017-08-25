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
package de.idrinth.stellaris.modtools.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class ModFile implements Serializable {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    protected int id;
    @ManyToOne
    protected Modification mod;
    @ManyToOne
    protected StellarisFile file;
    protected String diff;

    public ModFile(Modification mod, StellarisFile file) {
        this.mod = mod;
        this.file = file;
    }

    public ModFile(Modification mod) {
        this.mod = mod;
    }

    public ModFile(StellarisFile file) {
        this.file = file;
    }

    public ModFile() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Modification getMod() {
        return mod;
    }

    public void setMod(Modification mod) {
        this.mod = mod;
    }

    public StellarisFile getFile() {
        return file;
    }

    public void setFile(StellarisFile file) {
        this.file = file;
    }

    public String getDiff() {
        return diff;
    }

    public void setDiff(String diff) {
        this.diff = diff;
    }

}
