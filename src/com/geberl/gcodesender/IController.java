/*
    Copyright 2015-2018 Will Winder

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

import java.util.Optional;

import com.geberl.gcodesender.connection.ConnectionDriver;
import com.geberl.gcodesender.firmware.IFirmwareSettings;
import com.geberl.gcodesender.gcode.GcodeState;
import com.geberl.gcodesender.listeners.ControllerListener;
import com.geberl.gcodesender.listeners.ControllerStatus;
import com.geberl.gcodesender.model.Axis;
import com.geberl.gcodesender.model.Overrides;
import com.geberl.gcodesender.model.PartialPosition;
import com.geberl.gcodesender.model.UnitUtils;
import com.geberl.gcodesender.model.UGSEvent.ControlState;
import com.geberl.gcodesender.model.UnitUtils.Units;
import com.geberl.gcodesender.services.MessageService;
import com.geberl.gcodesender.types.GcodeCommand;
import com.geberl.gcodesender.utils.IGcodeStreamReader;

/**
 *
 * @author will
 */
public interface IController {

    /**
     * Adds listener for observing controller events such as connection status,
     * file send status and machine status
     *
     * @param listener the listener to observe the controller
     */
    void addListener(ControllerListener listener);

    /**
     * Removes a listener for this controller
     *
     * @param listener to be removed
     */
    void removeListener(ControllerListener listener);

    /**
     * Assigns a message service to be used for writing messages to the console
     *
     * @param messageService the central message service
     */
    void setMessageService(MessageService messageService);

    /*
    Actions
    */
    void performHomingCycle() throws Exception;
    void returnToHome() throws Exception;
    void resetCoordinatesToZero() throws Exception;
    void resetCoordinatesToZeroMill() throws Exception;
    void resetCoordinatesToZeroHotWire() throws Exception;
    void resetZaxisCoordinatesToZero() throws Exception;
	void resetCoordinateToZero(final Axis coord) throws Exception;

    /**
     * Sets the work position for any given axis to the position
     *
     * @param axisPosition the axis and the positions to change
     * @throws Exception if assigning the new position gave an error
     */
    void setWorkPosition(PartialPosition axisPosition) throws Exception;



    void killAlarmLock() throws Exception;
    // ======================================
    void switchToHotWire() throws Exception;
    void switchToMill() throws Exception;
    void switchOnSpindle() throws Exception;
    void switchOffSpindle() throws Exception;
    // ======================================
    void toggleCheckMode() throws Exception;
    void viewParserState() throws Exception;
    void issueSoftReset() throws Exception;

    /**
     * Jogs the machine in the direction specified by vector dirX,
     * dirY, dirZ given the direction as 1, 0 or -1. The distance is specified by stepSize in the given units.
     *
     * @param dirX if the jogging should happen in X-direction, possible values are 1, 0 or -1
     * @param dirY if the jogging should happen in Y-direction, possible values are 1, 0 or -1
     * @param dirZ if the jogging should happen in Z-direction, possible values are 1, 0 or -1
     * @param stepSize how long should we jog and is given in mm or inches
     * @param feedRate how fast should we jog in the direction
     * @param units the units of the stepSize and feed rate
     * @throws Exception if something went wrong when jogging
     */

    void jogMachine(int dirX, int dirY, int dirZ, int dirA,
            double stepSize, double feedRate, Units units) throws Exception;

    void jogMachine(int dirX, int dirY, int dirZ, int dirA, int dirB,
            double stepSize, double feedRate, Units units) throws Exception;


    /**
     * Jogs the machine to the given position. The feed rate is given in the same units / minute.
     *
     * @param position the position to move to
     * @param feedRate the feed rate using the units in the position.
     */
    void jogMachineTo(PartialPosition position, double feedRate) throws Exception;

    /**
     * Probe control
     */
    void probe(String axis, double feedRate, double distance, UnitUtils.Units units) throws Exception;
    void offsetTool(String axis, double offset, UnitUtils.Units units) throws Exception;

    /*
    Overrides
    */
    void sendOverrideCommand(Overrides command) throws Exception;

    /*
    Behavior
    */
    void setSingleStepMode(boolean enabled);
    boolean getSingleStepMode();

    void setStatusUpdatesEnabled(boolean enabled);
    boolean getStatusUpdatesEnabled();
    
    void setStatusUpdateRate(int rate);
    int getStatusUpdateRate();

    /*
    Serial
    */
    Boolean openCommPort(ConnectionDriver connectionDriver, String port, int portRate) throws Exception;
    Boolean closeCommPort() throws Exception;
    Boolean isCommOpen();
    
    /*
    Stream information
    */
    Boolean isReadyToReceiveCommands() throws Exception;
    Boolean isReadyToStreamFile() throws Exception;
    Boolean isStreaming();
    long getSendDuration();
    int rowsInSend();
    int rowsSent();
    int rowsCompleted();
    int rowsRemaining();
    Optional<GcodeCommand> getActiveCommand();
    GcodeState getCurrentGcodeState();
    
    /*
    Stream control
    */
    void beginStreaming() throws Exception;
    void pauseStreaming() throws Exception;
    void resumeStreaming() throws Exception;
    Boolean isPaused();
    Boolean isIdle();
    void cancelSend() throws Exception;
    ControlState getControlState();

    /**
     * In case a controller reset is detected.
     */
    void resetBuffers();

    /**
     * Indicator to abstract GUIBackend implementation that the contract class
     * will handle ALL state change events. When this returns true it means
     * things like completing the final command in a stream will not
     * automatically re-enable buttons.
     */
    Boolean handlesAllStateChangeEvents();
    
    /*
    Stream content
    */
    GcodeCommand createCommand(String gcode) throws Exception;
    void sendCommandImmediately(GcodeCommand cmd) throws Exception;
    void queueStream(IGcodeStreamReader r);

    /**
     * Cancel the running command and clear the command queue.
     */
    void cancelCommands();

    void restoreParserModalState();
    void updateParserModalState(GcodeCommand command);

    ICommunicator getCommunicator();

    /**
     * Returns the capabilities that is supported by the controller
     *
     * @return the supported capabilities for the controller.
     */
    Capabilities getCapabilities();

    /**
     * Fetches the firmware settings for the controller that can be used for
     * both querying and changing its settings.
     *
     * @return the firmware settings for the controller.
     */
    IFirmwareSettings getFirmwareSettings();

    /**
     * When connected this will return the controller firmware version.
     *
     * @return a version string
     */
    String getFirmwareVersion();

    /**
     * Returns the controller status. This method may never return null.
     *
     * @return the current controller status
     */
    ControllerStatus getControllerStatus();
}
