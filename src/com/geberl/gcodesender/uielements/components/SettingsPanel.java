/*
    Copyright 2020 Stephan Geberl

    This file is part of WireCutter/Mill GCode - Sender (WGS) (5 Axis-Version).
    WGS is derived from UGS by Will Winder (2012 - 2018)

    WGS is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    WGS is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with WGS.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.geberl.gcodesender.uielements.components;

import javax.swing.*;

import com.geberl.gcodesender.utils.FirmwareUtils;
import com.geberl.gcodesender.listeners.ControllerListener;
import com.geberl.gcodesender.listeners.ControllerStatus;
import com.geberl.gcodesender.listeners.UGSEventListener;
import com.geberl.gcodesender.model.Alarm;
import com.geberl.gcodesender.model.BackendAPI;
import com.geberl.gcodesender.model.Position;
import com.geberl.gcodesender.model.UGSEvent;
import com.geberl.gcodesender.model.UnitUtils.Units;
import com.geberl.gcodesender.services.JogService;
import com.geberl.gcodesender.types.GcodeCommand;
import com.geberl.gcodesender.uielements.dialog.ProgramSettingsPanel;
import com.geberl.gcodesender.uielements.dialog.ControllerProcessorSettingsPanel;
import com.geberl.gcodesender.uielements.dialog.FirmwareSettingsDialog;
import com.geberl.gcodesender.uielements.dialog.StandardDialogTemplate;
import com.geberl.gcodesender.utils.Settings;

import java.awt.Container;
import java.awt.Font;

import static com.geberl.gcodesender.utils.GUIHelpers.displayErrorDialog;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SettingsPanel extends JPanel implements UGSEventListener, ControllerListener {
	private static final long serialVersionUID = 1L;

	private BackendAPI backend;
	private JogService jogService;
    private Settings settings;

	private JButton helpButtonMachineControl = new JButton("Help ...");
	private JButton softResetMachineControl = new JButton("Soft Reset");
	private JButton btnGeneralSettings = new JButton("General Settings");
	private JButton btnGcodeSettings = new JButton("GCode Settings");
	private JButton btnFirmwareSettings = new JButton("GRBL Settings");
	private JRadioButton rdbtnMm = new JRadioButton("mm");
	private JRadioButton rdbtnInch = new JRadioButton("inch");

	// Mill Mode -> false
    // Hot Wire Mode -> true
    private J2StateButton toggleModeButton = new J2StateButton(true, "Switch to Hot Wire Mode", "Switch to Mill Mode");
	
	private JButton btnExitProgramm;

	private ButtonGroup unitButtonGroup = new ButtonGroup();
//    private Units unitsSavedValue = Units.MM;
//    private Units unitsActualValue = Units.MM;


    
	public SettingsPanel(BackendAPI backend, JogService jogService, boolean showKeyboardToggle, Settings settings) {

		this.backend = backend;
        this.settings = settings;
        this.jogService = jogService;
 
        initComponents();

        
        if (this.backend != null) {
        	
            this.backend.addUGSEventListener(this);
            this.backend.addControllerListener(this);

    		updateUnitButton();
             
        }
        
        this.toggleModeButton.setEnabled(false);

    }
	
    private void initComponents() {
    	
        setLayout(null);

        
        JLabel measureLabel = new JLabel("Dimensional unit");
        measureLabel.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
        measureLabel.setOpaque(true);
        measureLabel.setBounds(300, 16, 200, 16);
        add(measureLabel);

        
        rdbtnMm.setBounds(300, 40, 200, 16);
        rdbtnMm.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
        add(rdbtnMm);
        rdbtnMm.addActionListener(e -> toggleUnits());
        rdbtnMm.setEnabled(false);
        rdbtnInch.setBounds(300, 62, 200, 16);
        rdbtnInch.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
        add(rdbtnInch);
        rdbtnInch.addActionListener(e -> toggleUnits());
        rdbtnInch.setEnabled(false);

        unitButtonGroup.add(rdbtnMm);
        unitButtonGroup.add(rdbtnInch);

        btnExitProgramm = new JButton("Exit Programm");
        btnExitProgramm.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
        btnExitProgramm.setBounds(6, 6, 250, 40);
        add(btnExitProgramm);
        btnExitProgramm.addActionListener(new ActionListener() {
        	public void actionPerformed(java.awt.event.ActionEvent evt) {
        		exitControlActionPerformed(evt);
        	}
        });
        btnExitProgramm.setEnabled(true);
        
        
        softResetMachineControl.setBounds(6, 52, 250, 40);
        softResetMachineControl.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
        add(softResetMachineControl);

        softResetMachineControl.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                softResetMachineControlActionPerformed(evt);
            }
        });
        softResetMachineControl.setEnabled(false);
        
       
        btnGeneralSettings.setBounds(6, 98, 250, 40);
        btnGeneralSettings.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
        add(btnGeneralSettings);
        btnGeneralSettings.addActionListener(new ActionListener() {
        	public void actionPerformed(java.awt.event.ActionEvent evt) {
        		grblConnectionSettingsMenuItemActionPerformed(evt);
        		
        	}
        });
        btnGeneralSettings.setEnabled(true);
        
        
        btnGcodeSettings.setBounds(6, 144, 250, 40);
        btnGcodeSettings.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
        add(btnGcodeSettings);
        btnGcodeSettings.addActionListener(new ActionListener() {
        	public void actionPerformed(java.awt.event.ActionEvent evt) {
        		gcodeProcessorSettingsActionPerformed(evt);
        	}
        });
        btnGcodeSettings.setEnabled(true);

        
        btnFirmwareSettings.setBounds(6, 190, 250, 40);
        btnFirmwareSettings.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
        add(btnFirmwareSettings);
        btnFirmwareSettings.addActionListener(new ActionListener() {
        	public void actionPerformed(java.awt.event.ActionEvent evt) {
        		configureFirmwareActionPerformed(evt);
        	}
        });
        btnFirmwareSettings.setEnabled(false);

        

        helpButtonMachineControl.setBounds(6, 236, 250, 40);
        helpButtonMachineControl.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
        add(helpButtonMachineControl);
        helpButtonMachineControl.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpButtonMachineControlActionPerformed(evt);
            }
        });
        helpButtonMachineControl.setEnabled(true);

        // Button to change Machine - Mode
        
        toggleModeButton.setBounds(300, 98, 294, 40);
        toggleModeButton.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
        toggleModeButton.setEnabled(false);

	    switch (settings.getMachineMode()) {
    	case "HotWire":
    		this.toggleModeButton.setState(false);
    		break;
    	case "Mill":
       		this.toggleModeButton.setState(true);
       		break;
        default:
            break;      
	    }
        add(toggleModeButton);
        
        toggleModeButton.addActionListener(new ActionListener() {
        	public void actionPerformed(java.awt.event.ActionEvent evt) {
        		toggleModeButton.setState(!toggleModeButton.getState());

        		if(toggleModeButton.getState() == true) { 
        			settings.setMachineMode("HotWire");
        		};
        		if(toggleModeButton.getState() == false) {
        			settings.setMachineMode("Mill");
        		};
        		
        	}
        });
    }
    
    @Override
    public void UGSEvent(UGSEvent evt) {
    	
    	if (evt.isStateChangeEvent() || evt.isSettingChangeEvent()) {
         	updateControls();
        }
    }

	private void updateControls() {
		
		updateUnitButton();
		
		switch (backend.getControlState()) {
			case COMM_DISCONNECTED:
				this.toggleModeButton.setEnabled(false);
			case COMM_SENDING_PAUSED:
			case COMM_CHECK:
			default:
				this.updateManualControls(false);
				this.toggleModeButton.setEnabled(false);
				break;
			case COMM_IDLE:
			case COMM_SENDING:
				this.updateManualControls(!backend.isSendingFile());
				this.toggleModeButton.setEnabled(!backend.isSendingFile());
				break;
		}

	}

	
	// Handle Units
	
	private void toggleUnits() {
		if (getUnits() == Units.MM) {
			jogService.setUnits(Units.INCH);
		} else {
			jogService.setUnits(Units.MM);
		}
		updateUnitButton();
	}

	private void updateUnitButton() {
		if (getUnits() == Units.INCH){
			rdbtnInch.setSelected(true);
		} else {
			rdbtnMm.setSelected(true);
		}
	}

	private Units getUnits() {
		return jogService.getUnits();
	}

	// ENDE - Handle Units
	
	

    @Override
    public void controlStateChange(UGSEvent.ControlState state) {
    }

    @Override
    public void fileStreamComplete(String filename, boolean success) {

    }

    @Override
    public void receivedAlarm(Alarm alarm) {

    }

    @Override
    public void commandSkipped(GcodeCommand command) {

    }

    @Override
    public void commandSent(GcodeCommand command) {

    }

    @Override
    public void commandComment(String comment) {
    }

    @Override
    public void probeCoordinates(Position p) {
    }

    @Override
    public void commandComplete(GcodeCommand command) {

    }

	@Override
	public void statusStringListener(ControllerStatus status) {
	    
	    if (status == null) { return; }
	    
	    String machineMode = settings.getMachineMode();
	    
	    if (!(machineMode.equals(status.getMachineMode()))) {

		    switch (machineMode) {
	    	case "HotWire":
		        try {
		             backend.killAlarmLock();
		             backend.switchToHotWire();
		             this.toggleModeButton.setState(false);
		        } catch (Exception ex) {
		        	displayErrorDialog("Error switching machine mode to HotWire.");
		        }
	    		break;
	    	case "Mill":
		        try {
		             backend.killAlarmLock();
		             backend.switchToMill();
		             this.toggleModeButton.setState(true);
		        } catch (Exception ex) {
		        	displayErrorDialog("Error switching machine mode to Mill.");
		        }
	    		break;
	        default:
	            break;      
		    }
	    }
	    
	}

	
	// ***** Buttons Actions *****
	// ***** Steuerung *****
	
    private void softResetMachineControlActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            this.backend.issueSoftReset();
        } catch (Exception ex) {
            displayErrorDialog(ex.getMessage());
        }
    }

	private void exitControlActionPerformed(java.awt.event.ActionEvent evt) {
        try {
        	
    		this.backend.disconnect();
    		Container frame = btnExitProgramm.getParent();
            do 
                frame = frame.getParent(); 
            while (!(frame instanceof JFrame));                                      
            ((JFrame) frame).dispose();
            
        } catch (Exception ex) {
            displayErrorDialog(ex.getMessage());
        }
    }
   
    
    // ***** Enable/Disable Actions *****

	public void updateManualControls(boolean enabled) {
		
		// always enabled
		this.helpButtonMachineControl.setEnabled(true);
		this.btnExitProgramm.setEnabled(true);
		
		
		// Enabled when connected
		this.btnGeneralSettings.setEnabled(!enabled);
		this.btnGcodeSettings.setEnabled(!enabled);
		this.btnFirmwareSettings.setEnabled(enabled);
		this.softResetMachineControl.setEnabled(enabled);
		this.rdbtnMm.setEnabled(enabled);
		this.rdbtnInch.setEnabled(enabled);
	
	}
	
	// Dialoge
	private void grblConnectionSettingsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {

		JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
		
		StandardDialogTemplate gcsd = new StandardDialogTemplate(
				"General Settings",
				new ProgramSettingsPanel(settings),
				topFrame, true);
  
		gcsd.setBounds(topFrame.getX() + 10, topFrame.getY() + 10, gcsd.getDialogWidth(), gcsd.getDialogHeight());
		gcsd.toggleRestoreButton(true);
		gcsd.setVisible(true);
  
		if (gcsd.saveChanges()) {
			try {
				backend.applySettings(settings);
			} catch (Exception e) {
				displayErrorDialog(e.getMessage());
			}
		}
		
	}
   
	private void gcodeProcessorSettingsActionPerformed(java.awt.event.ActionEvent evt) {

		JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
		
		StandardDialogTemplate gcsd = new StandardDialogTemplate(
				"GRBL Settings",
				new ControllerProcessorSettingsPanel(settings, FirmwareUtils.getConfigFiles()),
				topFrame, true);

		gcsd.setBounds(topFrame.getX() + 10, topFrame.getY() + 10, gcsd.getDialogWidth(), gcsd.getDialogHeight());
		gcsd.toggleRestoreButton(false);
		gcsd.setVisible(true);

		if (gcsd.saveChanges()) {
			try {
				backend.applySettings(settings);
			} catch (Exception e) {
				displayErrorDialog(e.getMessage());
			}
		}
	}

	private void configureFirmwareActionPerformed(java.awt.event.ActionEvent evt) {

		JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);

        try {
            if (!this.backend.isConnected()) {
                displayErrorDialog("Not connected");
            } else if (this.backend.getController().getCapabilities().hasFirmwareSettings()) {
                FirmwareSettingsDialog gfsd =
                        new FirmwareSettingsDialog(new JFrame(), true, this.backend);
                gfsd.setBounds(topFrame.getX() + 10, topFrame.getY() + 10, 520, 300);
                gfsd.setVisible(true);
            }
            // Add additional firmware setting windows here.
            else {
                displayErrorDialog("No firmwares found.");
            }
        } catch (Exception ex) {
            displayErrorDialog(ex.getMessage());
        }
		
		
	}

	
    private void helpButtonMachineControlActionPerformed(java.awt.event.ActionEvent evt) {
        StringBuilder message = new StringBuilder()
        .append("Reset zero: Changes the current coordinates to zero without moving the machine.").append("\n")
        .append("Return to zero: Moves machine to 0, 0, 0, 0, 0 location.").append("\n")
        .append("Soft Reset: Reset the controller state without updating position.").append("\n")
        .append("Home Machine: Begin homing cycle.").append("\n")
        .append("Unlock: Disables the controller alarm lock.").append("\n")
        .append("Get State: Requests state information from the controller (result output to console).").append("\n")
        .append("- Keyboard Control:").append("\n")
        .append("--> XZ: Arrow: Left/Right; KeyPad: Left/Right; Numpad: 4/6").append("\n")
        .append("--> YA: Arrow: Up/Down; KeyPad: Left/Right; Numpad: 8/2").append("\n")
        .append("--> B : KeyPad: End/PgDn; Numpad: 1/3").append("\n")
        ;

        JOptionPane.showMessageDialog(new JFrame(),
            message,
            "Machine Control Help",
            JOptionPane.INFORMATION_MESSAGE);
    }
}
