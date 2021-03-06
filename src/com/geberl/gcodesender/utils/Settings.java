/*
    Copyright 2014-2019 Will Winder

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
package com.geberl.gcodesender.utils;

import org.apache.commons.lang3.StringUtils;
import com.geberl.gcodesender.connection.ConnectionDriver;
import com.geberl.gcodesender.model.Position;
import com.geberl.gcodesender.model.UnitUtils;
import com.geberl.gcodesender.model.UnitUtils.Units;
import com.geberl.gcodesender.types.WindowSettings;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.logging.Logger;

public class Settings {
    private static final Logger logger = Logger.getLogger(Settings.class.getName());

    // Transient, don't serialize or deserialize.
    transient private SettingChangeListener listener = null;
    transient public static int HISTORY_SIZE = 20;

    private String firmwareVersion = "GRBL";
    private String fileName = System.getProperty("user.home");
    private Deque<String> fileHistory = new ArrayDeque<>();
    private Deque<String> dirHistory = new ArrayDeque<>();
    private String port = "";
    private String portRate = "115200";
    private boolean manualModeEnabled = false;
    private double manualModeStepSize = 1.0;

    private double zJogStepSize = 1.0;
    private double jogFeedRate = 10;
    private boolean scrollWindowEnabled = true;
    private boolean verboseOutputEnabled = false;
    // Sender Settings
    private WindowSettings mainWindowSettings = new WindowSettings(0,0,640,520);
    private boolean singleStepMode = false;
    private boolean statusUpdatesEnabled = true;
    private int statusUpdateRate = 200;
    private Units preferredUnits = Units.MM;

    private boolean showSerialPortWarning = true;
    private boolean autoStartPendant = false;

    private boolean autoConnect = false;
    private boolean autoReconnect = false;

	private FileStats fileStats = new FileStats();

    private String connectionDriver;
    private String machineMode = "HotWire";

    private double xSavedValue = 0.00;
	private double ySavedValue = 0.00;
    private double zSavedValue = 0.00;
    private double SavedSaveHeight = 0.00;
	private Units unitsSavedValue = Units.MM;

	/**
     * A directory with gcode files for easy access through pendant
     */
    private String workspaceDirectory;

    /**
     * The GSON deserialization doesn't do anything beyond initialize what's in the json document.  Call finalizeInitialization() before using the Settings.
     */
    public Settings() {
        logger.fine("Initializing...");

        // Initialize macros with a default macro
    }


    /**
     * This method should only be called once during setup, a runtime exception
     * will be thrown if that contract is violated.
     */
    public void setSettingChangeListener(SettingChangeListener listener) {
        this.listener = listener;
    }

    private void changed() {
        if (listener != null) {
            listener.settingChanged();
        }
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public void setFirmwareVersion(String firmwareVersion) {
        if (StringUtils.equals(firmwareVersion, this.firmwareVersion)) return;
        this.firmwareVersion = firmwareVersion;
        changed();
    }

    public String getLastOpenedFilename() {
        return fileName;
    }

    public void setLastOpenedFilename(String absolutePath) {
        Path p = Paths.get(absolutePath).toAbsolutePath();
        this.fileName = p.toString();
        updateRecentFiles(p.toString());
        updateRecentDirectory(p.getParent().toString());
        changed();
    }

    public Collection<String> getRecentFiles() {
      return Collections.unmodifiableCollection(fileHistory);
    }

    public void updateRecentFiles(String absolutePath) {
      updateRecent(this.fileHistory, HISTORY_SIZE, absolutePath);
    }

    public Collection<String> getRecentDirectories() {
      return Collections.unmodifiableCollection(dirHistory);
    }

    public void updateRecentDirectory(String absolutePath) {
      updateRecent(this.dirHistory, HISTORY_SIZE, absolutePath);
    }

    private static void updateRecent(Deque<String> stack, int maxSize, String element) {
      stack.remove(element);
      stack.push(element);
      while( stack.size() > maxSize)
        stack.removeLast();
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        if (StringUtils.equals(port, this.port)) return;
        this.port = port;
        changed();
    }
    
    public String getPortRate() {
        return portRate;
    }

    public void setPortRate(String portRate) {
        if (StringUtils.equals(portRate, this.portRate)) return;
        this.portRate = portRate;
        changed();
    }

    public boolean isManualModeEnabled() {
        return manualModeEnabled;
    }

    public void setManualModeEnabled(boolean manualModeEnabled) {
        this.manualModeEnabled = manualModeEnabled;
        changed();
    }

    public double getManualModeStepSize() {
        return manualModeStepSize;
    }

    public void setManualModeStepSize(double manualModeStepSize) {
        this.manualModeStepSize = manualModeStepSize;
        changed();
    }

    public double getzJogStepSize() {
        return zJogStepSize;
    }

    public void setzJogStepSize(double zJogStepSize) {
        this.zJogStepSize = zJogStepSize;
        changed();
    }

    public double getJogFeedRate() {
        return jogFeedRate;
    }

    public void setJogFeedRate(double jogFeedRate) {
        this.jogFeedRate = jogFeedRate;
        changed();
    }

    public boolean isScrollWindowEnabled() {
        return scrollWindowEnabled;
    }

    public void setScrollWindowEnabled(boolean scrollWindowEnabled) {
        this.scrollWindowEnabled = scrollWindowEnabled;
        changed();
    }

    public boolean isVerboseOutputEnabled() {
        return verboseOutputEnabled;
    }

    public void setVerboseOutputEnabled(boolean verboseOutputEnabled) {
        this.verboseOutputEnabled = verboseOutputEnabled;
        changed();
    }

    public WindowSettings getMainWindowSettings() {
        return this.mainWindowSettings;
    }
    
    public void setMainWindowSettings(WindowSettings ws) {
        this.mainWindowSettings = ws;
        changed();
    }


    public boolean isSingleStepMode() {
        return singleStepMode;
    }

    public void setSingleStepMode(boolean singleStepMode) {
        this.singleStepMode = singleStepMode;
        changed();
    }

    public boolean isStatusUpdatesEnabled() {
        return statusUpdatesEnabled;
    }

    public void setStatusUpdatesEnabled(boolean statusUpdatesEnabled) {
        this.statusUpdatesEnabled = statusUpdatesEnabled;
        changed();
    }

    public int getStatusUpdateRate() {
        return statusUpdateRate;
    }

    public void setStatusUpdateRate(int statusUpdateRate) {
        this.statusUpdateRate = statusUpdateRate;
        changed();
    }
        
    public Units getPreferredUnits() {
        return (preferredUnits == null) ? Units.MM : preferredUnits;
    }
        
    public void setPreferredUnits(Units units) {
        if (units != null) {
            double scaleUnits = UnitUtils.scaleUnits(preferredUnits, units);
            preferredUnits = units;
            changed();

            // Change
            setManualModeStepSize(manualModeStepSize * scaleUnits);
            setzJogStepSize(zJogStepSize * scaleUnits);
            setJogFeedRate(Math.round(jogFeedRate * scaleUnits));
        }
    }


    public boolean isShowSerialPortWarning() {
        return showSerialPortWarning;
    }

    public void setShowSerialPortWarning(boolean showSerialPortWarning) {
        this.showSerialPortWarning = showSerialPortWarning;
        changed();
    }

   public boolean isAutoConnectEnabled() {
        return autoConnect;
    }

    public boolean isAutoReconnect() {
        return autoReconnect;
    }

    public void setFileStats(FileStats settings) {
        this.fileStats = settings;
        changed();
    }

    public FileStats getFileStats() {
        return this.fileStats;
    }

    public ConnectionDriver getConnectionDriver() {
        ConnectionDriver connectionDriver = ConnectionDriver.JSERIALCOMM;
        if (StringUtils.isNotEmpty(this.connectionDriver)) {
            try {
                connectionDriver = ConnectionDriver.valueOf(this.connectionDriver);
            } catch (IllegalArgumentException | NullPointerException ignored) {
                // Never mind, we'll use the default
            }
        }
        return connectionDriver;
    }

    public void setConnectionDriver(ConnectionDriver connectionDriver) {
        this.connectionDriver = connectionDriver.name();
    }

    public void setAutoStartPendant(boolean autoStartPendant) {
        this.autoStartPendant = autoStartPendant;
        changed();
    }

    public boolean isAutoStartPendant() {
        return this.autoStartPendant;
    }

    public void setWorkspaceDirectory(String workspaceDirectory) {
        this.workspaceDirectory = workspaceDirectory;
    }

    public String getWorkspaceDirectory() {
        return this.workspaceDirectory;
    }

    public static class FileStats {
        public Position minCoordinate;
        public Position maxCoordinate;
        public long numCommands;

        public FileStats() {
            this.minCoordinate = new Position(0, 0, 0, 0, 0, Units.MM);
            this.maxCoordinate = new Position(0, 0, 0, 0, 0, Units.MM);
            this.numCommands = 0;
        }

        public FileStats(Position min, Position max, long num) {
            this.minCoordinate = min;
            this.maxCoordinate = max;
            this.numCommands = num;
        }
    }

    public String getMachineMode() {
    	return this.machineMode;
    }
 
    public void setMachineMode(String aNewMachineMode) {
    	this.machineMode = aNewMachineMode;
    }
    
    public double getXSavedValue() {
		return xSavedValue;
	}

	public void setXSavedValue(double xSavedValue) {
		this.xSavedValue = xSavedValue;
	}

	public double getYSavedValue() {
		return ySavedValue;
	}

	public void setYSavedValue(double ySavedValue) {
		this.ySavedValue = ySavedValue;
	}

	public double getZSavedValue() {
		return zSavedValue;
	}

	public void setZSavedValue(double zSavedValue) {
		this.zSavedValue = zSavedValue;
	}
    
    public double getSavedSaveHeight() {
		return SavedSaveHeight;
	}

	public void setSavedSaveHeight(double savedSaveHeight) {
		SavedSaveHeight = savedSaveHeight;
	}
	
	public Units getUnitsSavedValue() {
		return unitsSavedValue;
	}

	public void setUnitsSavedValue(Units unitsSavedValue) {
		this.unitsSavedValue = unitsSavedValue;
	}


}
