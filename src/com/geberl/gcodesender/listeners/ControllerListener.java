/*
    Copywrite 2013-2018 Will Winder

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
package com.geberl.gcodesender.listeners;

import com.geberl.gcodesender.model.Alarm;
import com.geberl.gcodesender.model.Position;
import com.geberl.gcodesender.model.UGSEvent.ControlState;
import com.geberl.gcodesender.types.GcodeCommand;

/**
 * Controller listener event interface
 *
 * @author wwinder
 */
public interface ControllerListener {
    /**
     * The controller has modified the state by itself, such as pausing a job on
     * an error.
     */
    void controlStateChange(ControlState state);

    /**
     * The file streaming has completed.
     */
    void fileStreamComplete(String filename, boolean success);

    /**
     * If an alarm is received from the controller
     *
     * @param alarm the alarm received from the controller
     */
    void receivedAlarm(Alarm alarm);

    /**
     * A command in the stream has been skipped.
     */
    void commandSkipped(GcodeCommand command);
    
    /**
     * A command has successfully been sent to the controller.
     */
    void commandSent(GcodeCommand command);
    
    /**
     * A command has been processed by the the controller.
     */
    void commandComplete(GcodeCommand command);
    
    /**
     * A comment has been processed.
     */
    void commandComment(String comment);

    /**
     * Probe coordinates received.
     */
    void probeCoordinates(Position p);

    /**
     * Controller status information.
     */
    void statusStringListener(ControllerStatus status);
}