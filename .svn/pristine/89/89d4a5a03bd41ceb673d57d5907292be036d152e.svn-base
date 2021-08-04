/*
    Copyright 2016-2019 Will Winder

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
package com.geberl.gcodesender.uielements.dialog;

import org.apache.commons.lang3.StringUtils;

import com.geberl.gcodesender.connection.ConnectionDriver;
import com.geberl.gcodesender.utils.Settings;
import com.geberl.gcodesender.utils.SettingsFactory;
import com.geberl.gcodesender.utils.SwingHelpers;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Optional;

/**
 *
 * @author wwinder
 */
public class ProgramSettingsPanel extends AbstractSettingsPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String[] connectionModeString = {
            ConnectionDriver.JSSC.getPrettyName(),
            ConnectionDriver.JSERIALCOMM.getPrettyName(),
            ConnectionDriver.TCP.getPrettyName()
            };
	
	private final Checkbox verboseConsoleOutput = new Checkbox("Show verbose output");
    private final Checkbox singleStepMode = new Checkbox("Enable single step mode");
    private final Checkbox statusPollingEnabled = new Checkbox("Enable status polling");
    private final Spinner statusPollRate = new Spinner("Status poll rate (ms)", new SpinnerNumberModel(1, 1, null, 100));
    private final ComboBox connectionDriver = new ComboBox("Connection driver  ", connectionModeString);

    private final JLabel workspaceDirectoryLabel = new JLabel("");
    private final JButton workspaceDirectoryBrowseButton = new JButton("Workspace");
    
    


    public ProgramSettingsPanel(Settings settings) {
	  this(settings, null);
	}

    public ProgramSettingsPanel(Settings settings, IChanged changer) {
        super(settings, changer);
        initComponents(settings);
        updateComponentsInternal(settings);
    }
 
    @Override
    public int getHeight() { return 230; }
    @Override
	public int getWidth() { return 335; }
    
    
    private void initComponents(Settings s) {
        this.removeAll();

        setLayout(null);
        

        verboseConsoleOutput.setBounds(5,5,320,24);
        add(verboseConsoleOutput);

        singleStepMode.setBounds(5,30,320,24);
        add(singleStepMode);

        statusPollingEnabled.setBounds(5,55,320,24);
        add(statusPollingEnabled);

        statusPollRate.setBounds(28,80,200,26);
        add(statusPollRate);
        
        connectionDriver.setBounds(5,120,300,26);
        add(connectionDriver);

        workspaceDirectoryBrowseButton.setBounds(5,155,330,30);
        workspaceDirectoryBrowseButton.setAction(createBrowseDirectoryAction());
        add(workspaceDirectoryBrowseButton);
        
        workspaceDirectoryLabel.setBounds(5,190,400,26);
        add(workspaceDirectoryLabel);
        
        workspaceDirectoryLabel.setText(settings.getWorkspaceDirectory());
    }
    


    @Override
    public String getHelpMessage() {
        return "Show additional information in the controller console" + "\n" +
                "Enable single step mode: Turns on single step mode, this is very slow." + "\n" +
                "Enable status polling: Turns on status polling for firmware if supported." + "\n" +
                "Status poll rate: The rate in milliseconds that status requests are sent at." + "\n" +
                "Connection driver: Driver for the communication with the arduino." + "\n" +
        		"Saved starting point: x, y , z." + "\n" +
        		"Save height: Z value for savely change x and y." + "\n" +
        		"Units for x,y,z and save height." + "\n";

    }

    @Override
    public void save() {
        settings.setVerboseOutputEnabled(verboseConsoleOutput.getValue());
        settings.setSingleStepMode(singleStepMode.getValue());
        settings.setStatusUpdatesEnabled(statusPollingEnabled.getValue());
        settings.setStatusUpdateRate((int)statusPollRate.getValue());
        
         if (connectionDriver.getSelectedItem().equals(ConnectionDriver.JSERIALCOMM.getPrettyName())) {
            settings.setConnectionDriver(ConnectionDriver.JSERIALCOMM);
        } else if (connectionDriver.getSelectedItem().equals(ConnectionDriver.TCP.getPrettyName())) {
            settings.setConnectionDriver(ConnectionDriver.TCP);
        } else {
            settings.setConnectionDriver(ConnectionDriver.JSSC);
        }
        settings.setWorkspaceDirectory(workspaceDirectoryLabel.getText());
        
        
        SettingsFactory.saveSettings(settings);
    }

    @Override
    public void restoreDefaults() {
        updateComponentsInternal(new Settings());

        save();
    }

    @Override
    protected void updateComponentsInternal(Settings s) {

        verboseConsoleOutput.setSelected(s.isVerboseOutputEnabled());
        singleStepMode.setSelected(s.isSingleStepMode());
        statusPollingEnabled.setSelected(s.isStatusUpdatesEnabled());
        statusPollRate.setValue(s.getStatusUpdateRate());
        connectionDriver.setSelectedItem(s.getConnectionDriver().getPrettyName());
        workspaceDirectoryLabel.setText(settings.getWorkspaceDirectory());
        
    }

    private AbstractAction createBrowseDirectoryAction() {
        return new AbstractAction("Workspace") {
            @Override
            public void actionPerformed(ActionEvent e) {
                File directory = new File(".");
                if(StringUtils.isNotEmpty(workspaceDirectoryLabel.getText())) {
                    directory = new File(workspaceDirectoryLabel.getText());
                }

                Optional<File> optionalFile = SwingHelpers.openDirectory("Workspace directory", directory);
                optionalFile.ifPresent(file -> workspaceDirectoryLabel.setText(file.getPath()));
            }
        };
    }
}
