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
package de.idrinth.stellaris.modtools.process5modcreation;

import java.util.HashSet;

class MultiValue implements ModLine {

    private final HashSet<String> values = new HashSet<>();

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        values.stream().forEach((value) -> {
            sb.append("\n\t\"");
            sb.append(value);
            sb.append("\"");
        });
        sb.append("\n}");
        return sb.toString();
    }

    @Override
    public void addValue(String value) {
        this.values.add(value);
    }
}
