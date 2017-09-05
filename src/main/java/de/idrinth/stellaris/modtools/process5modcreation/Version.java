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

import java.util.ArrayList;
import org.apache.commons.collections4.CollectionUtils;

class Version {

    private final ArrayList<String> version = new ArrayList<>();

    public Version() {
        version.add("1");
        version.add("0");
        version.add("0");
    }

    public void addIfBigger(String compareVersion) {
        ArrayList<String> cV = new ArrayList();
        CollectionUtils.addAll(cV, compareVersion.split("\\."));
        if (isBigger(cV)) {
            version.clear();
            version.addAll(cV);
            while (version.size() < 3) {
                version.add("0");
            }
        }
    }

    private boolean isBigger(ArrayList<String> compareVersion) {
        int counter = 0;
        int maxCounter = version.size()>compareVersion.size()?version.size():compareVersion.size();
        while(compareVersion.size()< maxCounter) {
            compareVersion.add("0");
        }
        while (version.size() < maxCounter) {
            version.add("0");
        }
        while (counter < maxCounter) {
            if(compareVersion.size()<= counter) {
                compareVersion.add("0");
            }
            if (version.size() <= counter) {
                version.add("0");
            }
            if("*".equals(version.get(counter)) && "*".equals(compareVersion.get(counter))) {
                //this is effectively equal :/
            } else if ("*".equals(compareVersion.get(counter))) {
                return true;
            } else if ("*".equals(version.get(counter))) {
                return false;
            } else if (Integer.parseInt(compareVersion.get(counter)) < Integer.parseInt(version.get(counter))) {
                return false;
            } else if (Integer.parseInt(compareVersion.get(counter)) > Integer.parseInt(version.get(counter))) {
                return true;
            }
            counter++;
        }
        return false;
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
