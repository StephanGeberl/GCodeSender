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

package com.geberl.gcodesender.model;

import java.io.File;
import java.util.List;

import com.geberl.gcodesender.IController;
import com.geberl.gcodesender.gcode.GcodeParser;
import com.geberl.gcodesender.listeners.MessageListener;
import com.geberl.gcodesender.listeners.MessageType;
import com.geberl.gcodesender.model.UnitUtils.Units;
import com.geberl.gcodesender.types.GcodeCommand;
import com.geberl.gcodesender.utils.Settings;

/**
 * API used by front ends to interface with the model.
 */
public interface BackendAPI extends BackendAPIReadOnly {
    // Config options
    void setGcodeFile(File file) throws Exception;

    /**
     * Returns a list of files from the configured workspace directory
     *
     * @return a list of files in the workspace
     */
    List<String> getWorkspaceFileList();

    /**
     * Opens a gcode file from the workspace directory. Just supply one of the files from
     * the method {@link #getWorkspaceFileList()}.
     *
     * @param file the file to open
     */
    void openWorkspaceFile(String file) throws Exception;

    void applySettings(Settings settings) throws Exception;

    /**
     * Modify the currently processed gcode with a provided gcode parser.
     * This can be used for post-processing tasks like rotating a gcode file.
     * @param gcp externally configured gcode parser.
     * @throws Exception 
     */
    void applyGcodeParser(GcodeParser gcp) throws Exception;

    /**
     * Process the currently loaded gcode file and export it to a file.
     * Intended primarily as "save and export" style preprocessor option.
     * @param f location to export processed gcode
     */
    void preprocessAndExportToFile(File f) throws Exception;
    
    // Control options
    void connect(String firmware, String port, int baudRate) throws Exception;
    void disconnect() throws Exception;
    void sendGcodeCommand(String commandText) throws Exception;
    void sendGcodeCommand(boolean restoreParserState, String commandText) throws Exception;
    void sendGcodeCommand(GcodeCommand command) throws Exception;
    void adjustManualLocation(int dirX, int dirY, int dirZ, int dirA, double stepSize, double feedRate, Units units) throws Exception;
    void adjustManualLocation(int dirX, int dirY, int dirZ, int dirA, int dirB, double stepSize, double feedRate, Units units) throws Exception;

    void probe(String axis, double feedRate, double distance, UnitUtils.Units units) throws Exception;
    void offsetTool(String axis, double offset, UnitUtils.Units units) throws Exception;

    void send() throws Exception;
    void pauseResume() throws Exception;
    void cancel() throws Exception;
    void returnToZero() throws Exception;
    void resetCoordinatesToZero() throws Exception;
    void resetCoordinatesToZeroMill() throws Exception;
    void resetCoordinatesToZeroHotWire() throws Exception;
    void resetZaxisCoordinatesToZero() throws Exception;
    void resetCoordinateToZero(Axis coordinate) throws Exception;

    /**
     * Sets the work position for a given axis to a position.
     *
     * @param position the position to set the one or more axis to set
     * @throws Exception
     */
    void setWorkPosition(PartialPosition position) throws Exception;


    /**
     * Sets the work position for a given axis to a position defined by an mathematical
     * expression. The character '#' will be replaced by the current work position. If the
     * expression starts with '/' or '*' the expression will be prepended with the current
     * work position.
     *
     * Examples:
     *   20 * 2 + 0.05
     *   # / 2
     *   * 2
     *
     * @param axis the axis to set
     * @param expression the mathimatical expression to use
     */
    void setWorkPositionUsingExpression(Axis axis, String expression) throws Exception;

    void killAlarmLock() throws Exception;
    // -------------------------------------
    void switchToHotWire() throws Exception;
    void switchToMill() throws Exception;
    void switchOnSpindle() throws Exception;
    void switchOffSpindle() throws Exception;
    // -------------------------------------
    void performHomingCycle() throws Exception;
    void toggleCheckMode() throws Exception;
    void issueSoftReset() throws Exception;
    void requestParserState() throws Exception;

    // Programatically call an override.
    void sendOverrideCommand(Overrides override) throws Exception;
           
    // Shouldn't be needed often.
    IController getController();
    void applySettingsToController(Settings settings, IController controller) throws Exception;

    /**
     * Dispatch a message with the given type to all registered message listeners using {@link MessageListener#onMessage(MessageType, String)}
     *
     * @param messageType the type of message to be printed
     * @param message the message to be written
     */
    void dispatchMessage(MessageType messageType, String message);
    
    Boolean isMillMode();
}