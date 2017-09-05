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
        adjustVersionToMaxSize(compareVersion, version.size());
        adjustVersionToMaxSize(version, compareVersion.size());
        while (counter < version.size()) {
            switch(isSecondPartBiggerThanFirst(version.get(counter),compareVersion.get(counter))) {
                case 1:
                    return true;
                case -1:
                    return false;
                case 0:
                default:
                    counter++;
            }
        }
        return false;
    }

    /**
     * returns -1 if smaller, 1 if bigger and 0 if equal, the same as sorting
     * @param part1
     * @param part2
     * @return 
     */
    private int isSecondPartBiggerThanFirst(String part1, String part2) {
            if(part1.equals(part2)) {
                return 0;
            }
            if ("*".equals(part2)) {
                return 1;
            }
            if ("*".equals(part1)) {
                return -1;
            }
            int int1 = Integer.parseInt(part1);
            int int2 = Integer.parseInt(part2);
            if (int1 > int2) {
                return -1;
            }
            return int2 > int1?1:0;
    }

    private void adjustVersionToMaxSize(ArrayList<String> v, int size) {
        while (v.size() < size) {
            v.add("0");
        }
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
