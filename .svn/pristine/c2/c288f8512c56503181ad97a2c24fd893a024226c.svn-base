/*
    Copyright 2012-2018 Will Winder

    This file is part of Universal Gcode Sender (UGS).

    UGS is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    UGS is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with UGS.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.geberl.gcodesender;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.geberl.gcodesender.listeners.ControllerState;
import com.geberl.gcodesender.utils.Settings;

import java.awt.EventQueue;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * A collection of utilities that don't relate to anything in particular.
 *
 * @author wwinder
 */
public class Utils {

    // public static NumberFormat formatter = new DecimalFormat("#.###", Localization.dfs);
	// Hier sind Zehntel Millimeter genug
	public static NumberFormat formatter = new DecimalFormat("#.#");
	
	public static String formattedMillis(long millis) {
        String format = String.format("%%0%dd", 2);  
        long elapsedTime = millis / 1000;  
        String hours = String.format(format, elapsedTime / 3600);
        elapsedTime %= 3600;
        
        String minutes = String.format(format, elapsedTime / 60);
        elapsedTime %= 60;
        
        String seconds = String.format(format, elapsedTime);


        return hours + ":" + minutes + ":" + seconds;
    }

    /**
     * Converts the controller state to a localized string to be shown to the user
     *
     * @param state the state to translate
     * @return a translated string of the state
     */
    public static String getControllerStateText(ControllerState state) {
        String text = state.name();
        if (state == ControllerState.ALARM) {
            text = "Alarm";
        } else if (state == ControllerState.HOLD) {
            text = "Hold";
        } else if (state == ControllerState.DOOR) {
            text = "Door";
        } else if (state == ControllerState.RUN) {
            text = "Run";
        } else if (state == ControllerState.JOG) {
            text = "Jog";
        } else if (state == ControllerState.CHECK) {
            text = "Check";
        } else if (state == ControllerState.IDLE) {
            text = "Idle";
        }
        return text;
    }



}
