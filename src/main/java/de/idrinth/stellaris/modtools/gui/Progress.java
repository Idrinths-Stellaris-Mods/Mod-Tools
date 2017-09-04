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
package de.idrinth.stellaris.modtools.gui;

import java.util.ArrayList;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;

public class Progress extends AnchorPane implements ProgressElementGroup {

    private final ArrayList<ProgressSet> sets = new ArrayList<>();
    private int setNum = 0;
    private final ArrayList<String> stepLabels;

    public Progress() {
        this(new ArrayList<String>());
    }

    public Progress(ArrayList<String> stepLabels) {
        super();
        this.stepLabels = stepLabels;
        this.setVisible(true);
    }

    @Override
    public void addToStepLabels(String text) {
        stepLabels.add(text);
    }

    @Override
    public void update(int current, int maximum) {
        Platform.runLater(() -> {
            if (sets.size() <= setNum || null == sets.get(setNum)) {
                sets.add(new ProgressSet(sets.size()));
                sets.get(sets.size() - 1).setLayoutY(20 * (sets.size() - 1));
                getChildren().add(sets.get(sets.size() - 1));
                this.setHeight(20 * sets.size());
                this.setWidth(350);
            }
            sets.get(sets.size() - 1).update(current, maximum);
            if (current == maximum && maximum > 0) {
                setNum++;
            }
        });
    }

    private class ProgressSet extends AnchorPane {

        private final ProgressBar bar = new ProgressBar();
        private final Label text = new Label();
        private final Label prefix;

        public ProgressSet(int pos) {
            super();
            this.setVisible(true);
            prefix = new Label(null == stepLabels.get(pos) ? "Step " + (pos + 1) : stepLabels.get(pos));
            super.getChildren().add(bar);
            super.getChildren().add(text);
            super.getChildren().add(prefix);
            this.bar.setVisible(true);
            this.text.setVisible(true);
            this.prefix.setVisible(true);
            this.bar.setLayoutX(160);
            this.text.setLayoutX(275);
            this.text.setText("Not started yet");
            this.bar.setTooltip(new Tooltip("Not started yet"));
        }

        public void update(int current, int maximum) {
            String status = current == maximum ? maximum + " finished" : current + " of " + maximum + " done";
            double value = (double) ((double) current) / ((double) maximum);
            System.out.println(status);
            Platform.runLater(() -> {
                if (!bar.isVisible()) {
                    bar.setVisible(true);
                }
                if (!text.isVisible()) {
                    text.setVisible(true);
                }
                bar.getTooltip().setText(status);
                bar.setProgress(value);
                text.setText(String.format("%5.2f", value * 100) + "%");
            });
        }
    }
}
