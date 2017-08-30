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
package de.idrinth.stellaris.modtools.fx;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;

public class ProgressElement  {
    private final ProgressBar bar;
    private final Label text;
    private final Tooltip tooltip;

    public ProgressElement(ProgressBar bar, Label text) {
        this.bar = bar;
        this.text = text;
        tooltip = new Tooltip("Not started yet");
        this.text.setText("Not started yet");
        this.bar.setTooltip(tooltip);
    }

    public void update(int current, int maximum) {
        String status = current+" of "+maximum+" done";
        double value = (double) ((double)current) / ((double)maximum);
        System.out.println(status);
        Platform.runLater(() -> {
            if(!bar.isVisible()) {
                bar.setVisible(true);
            }
            tooltip.setText(status);
            bar.setProgress(value);
            text.setText(String.format("%5.2f", value*100)+"%");
        });
    }
}
