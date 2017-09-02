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
package de.idrinth.stellaris.modtools.ziphelpers;

import java.util.ArrayList;
import org.apache.commons.collections4.CollectionUtils;

public class Version {
    private final ArrayList<String> version = new ArrayList<>();

    public Version() {
        version.add("1");
        version.add("0");
        version.add("0");
    }
    
    public void addIfBigger(String compareVersion) {
        if(isBigger(compareVersion.split("\\."))) {
            version.clear();
            CollectionUtils.addAll(version, compareVersion.split("\\."));
            while(version.size()<3) {
                version.add("0");
            }
        }
    }
    private boolean isBigger(String[] compareVersion) {
        int counter = 0;
        for(String part:compareVersion) {
            if("*".equals(part)) {
                return true;
            }
            if(version.size()<=counter) {
                version.add("0");
            }
            if("*".equals(version.get(counter))) {
                return false;
            }
            if(Integer.parseInt(part) < Integer.parseInt(version.get(counter))) {
                return false;
            }
            if(Integer.parseInt(part) > Integer.parseInt(version.get(counter))) {
                return true;
            }
            counter++;
        }
        return version.size()>compareVersion.length;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        version.forEach((part) -> {
            sb.append(".");
            sb.append(part);
        });
        return sb.substring(1);
    }
}
