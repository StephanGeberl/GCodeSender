/*
    Copyright 2024 Stephan Geberl

    This file is part of Universal Gcode Sender (UGS) (5 Axis-Version).

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
package com.geberl.gcodesender.uielements.components;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.util.List;

import com.geberl.gcodesender.connection.ConnectionFactory;
import com.geberl.gcodesender.listeners.ControllerListener;
import com.geberl.gcodesender.listeners.ControllerState;
import com.geberl.gcodesender.listeners.ControllerStatus;
import com.geberl.gcodesender.listeners.UGSEventListener;
import com.geberl.gcodesender.model.BaudRateEnum;
import com.geberl.gcodesender.model.Position;
import com.geberl.gcodesender.model.UGSEvent.ControlState;
import com.geberl.gcodesender.types.GcodeCommand;
import com.geberl.gcodesender.model.Alarm;
import com.geberl.gcodesender.model.BackendAPI;
import com.geberl.gcodesender.utils.Settings;
import com.geberl.gcodesender.utils.SettingsFactory;

import static com.geberl.gcodesender.utils.GUIHelpers.displayErrorDialog;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConnectionPanel extends JPanel implements ControllerListener, UGSEventListener {
	
	// Breite: 190
	// Höhe: 160

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final BackendAPI backend;
    private Settings settings;
    private String machineMode = "";
    
    private JLabel lblMachineMode = new JLabel("Mode: unknown");
    private JComboBox commPortComboBox = new JComboBox();
    private JComboBox baudrateSelectionComboBox = new JComboBox();
    private JButton opencloseButton = new JButton("Open");
    private JButton refreshButton = new JButton("");
	private JButton performHomingCycleButton = new JButton("Home ($H)");
	private JButton killAlarmLock = new JButton("Unlock ($X)");
	private JButton resetCoordinatesButton = new JButton("Reset Zero All");
	private JButton returnToZeroButton = new JButton("Return to Zero");
    
    public ConnectionPanel(BackendAPI backend) {

    	this.backend = backend;
        this.settings = SettingsFactory.loadSettings();
        this.loadPortSelector();
        commPortComboBox.setSelectedItem(settings.getPort());
        baudrateSelectionComboBox.setModel(new javax.swing.DefaultComboBoxModel(BaudRateEnum.getAllBaudRates()));
        baudrateSelectionComboBox.setSelectedItem(settings.getPortRate());

        initComponents();
 
        if (this.backend != null) {
        	
            this.backend.addUGSEventListener(this);
            this.backend.addControllerListener(this);
             
        }

    }
	
    private void initComponents() {
        
        setLayout(null);
        
        // ======================

        lblMachineMode.setFont(new Font("DejaVu Sans", Font.BOLD, 18));
        lblMachineMode.setHorizontalAlignment(SwingConstants.CENTER);
        lblMachineMode.setBounds(6, 8, 178, 30);
        add(lblMachineMode);
       
        refreshButton.setToolTipText("Reload Ports");
        refreshButton.setBounds(6, 42, 40, 40);
        refreshButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icons/refresh.gif"))); // NOI18N
        refreshButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
            	loadPortSelector();
            }
        });
        add(refreshButton);
        
        opencloseButton.setBounds(52, 42, 132, 40);
        opencloseButton.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
        opencloseButton.setText("Open");
        opencloseButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                opencloseButtonActionPerformed(evt);
            }
        });
        add(opencloseButton);
        
        JLabel lblPort = new JLabel("Port");
        lblPort.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
        lblPort.setBounds(6, 88, 44, 30);
        add(lblPort);
        
        JLabel lblNewLabel = new JLabel("Baud");
        lblNewLabel.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
        lblNewLabel.setBounds(6, 124, 44, 30);
        add(lblNewLabel);

        commPortComboBox.setBounds(55, 88, 128, 30);
        commPortComboBox.setFont(new Font("DejaVu Sans", Font.BOLD, 14));
        commPortComboBox.setToolTipText("Select serial port.");        
        add(commPortComboBox);
        
        baudrateSelectionComboBox.setBounds(55, 124, 128, 30);
        baudrateSelectionComboBox.setFont(new Font("DejaVu Sans", Font.BOLD, 14));
        baudrateSelectionComboBox.setToolTipText("Select baudrate to use for the serial port.");        
        add(baudrateSelectionComboBox);
        
        // ======================
        
        // performHomingCycleButton.setBackground(Color.ORANGE);
        performHomingCycleButton.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
        performHomingCycleButton.setBounds(6, 162, 177, 40);
        add(performHomingCycleButton);
        
        performHomingCycleButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                performHomingCycleButtonActionPerformed(evt);
            }
        });
        performHomingCycleButton.setEnabled(false);
        
	    // killAlarmLock.setBackground(Color.ORANGE);
        killAlarmLock.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
        killAlarmLock.setBounds(6, 208, 177, 40);
        add(killAlarmLock);
        
        killAlarmLock.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                killAlarmLockActionPerformed(evt);
            }
        });
        killAlarmLock.setEnabled(false);
        

        // resetCoordinatesButton.setBackground(Color.ORANGE);
        resetCoordinatesButton.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
        resetCoordinatesButton.setBounds(6, 254, 177, 40);
        add(resetCoordinatesButton);

        // reset Functions
        resetCoordinatesButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		resetCoordinatesButtonActionPerformed(arg0);
        	}
        });
        resetCoordinatesButton.setEnabled(false);

       
        // returnToZeroButton.setBackground(Color.ORANGE);
        returnToZeroButton.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
        
        returnToZeroButton.setBounds(6, 300, 177, 40);
        add(returnToZeroButton);

        returnToZeroButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                returnToZeroButtonActionPerformed(evt);
            }
        });
        returnToZeroButton.setEnabled(false);
        
    }
    
    
    // Scans for comm ports and puts them in the comm port combo box.
    private void loadPortSelector() {
        commPortComboBox.removeAllItems();

        List<String> portList = ConnectionFactory.getPortNames(backend.getSettings().getConnectionDriver());
        if (portList.size() < 1) {
            if (this.settings.isShowSerialPortWarning()) {
                displayErrorDialog("No serial ports found.");
            }
        } else {
            for (String port : portList) { commPortComboBox.addItem(port); }
            commPortComboBox.setSelectedIndex(0);
        }
    }
    
    private void opencloseButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if( this.opencloseButton.getText().equalsIgnoreCase("Open") ) {

            String firmware = "GRBL";
        	String port = commPortComboBox.getSelectedItem().toString();
            int baudRate = Integer.parseInt(baudrateSelectionComboBox.getSelectedItem().toString());
            
            try {
                this.backend.connect(firmware, port, baudRate);
            } catch (Exception e) {
                e.printStackTrace();
                displayErrorDialog(e.getMessage());
            }
            
        } else {
            try {
                this.backend.disconnect();

            } catch (Exception e) {
            	displayErrorDialog(e.getMessage());
            }
        }
    }
    
    public void updateConnectionControlsStateOpen(boolean isOpen) {

        this.commPortComboBox.setEnabled(!isOpen);
        this.baudrateSelectionComboBox.setEnabled(!isOpen);
        this.refreshButton.setEnabled(!isOpen);

        if (isOpen) {
            this.opencloseButton.setText("Close");
        } else {
            this.opencloseButton.setText("Open");
            this.lblMachineMode.setText("Mode: unknown");
            this.lblMachineMode.setForeground(Color.black);
        }
    }

    public void saveSettings() {
    	settings.setPort(commPortComboBox.getSelectedItem().toString());
    	settings.setPortRate(baudrateSelectionComboBox.getSelectedItem().toString());
    }

     // Funktionen Buttons
     
     private void performHomingCycleButtonActionPerformed(java.awt.event.ActionEvent evt) {
         try {
             this.backend.performHomingCycle();
         } catch (Exception ex) {
             displayErrorDialog(ex.getMessage());
         }
     }
     

     private void killAlarmLockActionPerformed(java.awt.event.ActionEvent evt) {
         try {
             this.backend.killAlarmLock();
         } catch (Exception ex) {
             displayErrorDialog(ex.getMessage());
         }
     }

     private void resetCoordinatesButtonActionPerformed(java.awt.event.ActionEvent evt) {
         try {
         	if (backend.isMillMode()) {
         		this.backend.resetCoordinatesToZeroMill();
         	}
         	else {
         		this.backend.resetCoordinatesToZero();
         	}
         } catch (Exception ex) {
             displayErrorDialog(ex.getMessage());
         }
     }

     
     private void returnToZeroButtonActionPerformed(java.awt.event.ActionEvent evt) {
         try {
             this.backend.returnToZero();
         } catch (Exception ex) {
             displayErrorDialog(ex.getMessage());
         }
     }

     // Update Buttons
     
     
	private void updateControls() {

		switch (backend.getControlState()) {
			case COMM_DISCONNECTED:
			case COMM_SENDING_PAUSED:
			case COMM_CHECK:
			default:
				updateManualControls(false);
				break;
			case COMM_IDLE:
			case COMM_SENDING:
				updateManualControls(!backend.isSendingFile());
				break;
		}
	}
    
	public void updateManualControls(boolean enabled) {
		
		// Always enabled
		
		// Enabled when connected
		this.killAlarmLock.setEnabled(enabled);
		this.returnToZeroButton.setEnabled(enabled);
		this.resetCoordinatesButton.setEnabled(enabled);
		this.performHomingCycleButton.setEnabled(enabled);
	
	}
     
     
     // Overrides
	 
	@Override
	public void UGSEvent(com.geberl.gcodesender.model.UGSEvent evt) {
		
	    if (evt.isFileChangeEvent() || evt.isStateChangeEvent()) {
	         
	    	// opencloseButton.setEnabled(backend.isIdle());

	    	switch (backend.getControlState())
	    	{
	             case COMM_DISCONNECTED:
	                 this.updateConnectionControlsStateOpen(false);
	                 break;
	             case COMM_IDLE:
	            	 this.updateConnectionControlsStateOpen(true);
	                 break;
	             case COMM_SENDING:
	            	 updateManualControls(!backend.isSendingFile());
	                 break;
	             case COMM_SENDING_PAUSED:

	                 break;
	             default:
	            	 
	            	 break;
	                 
	         }
	    	updateControls();
	    
	    }
	
	    if (evt.isSettingChangeEvent() && backend.getController() != null && backend.getController().getControllerStatus() != null)
	    {
	        statusStringListener(backend.getController().getControllerStatus());
	    }
		
	}
	
	@Override
	public void statusStringListener(ControllerStatus status) {
		
		updateControls();
		
		if (status == null) {
	        return;
	    }

		if (!(this.machineMode.equals(status.getMachineMode()))) {
		    
			this.machineMode = status.getMachineMode();
			switch (this.machineMode) {
		    	case "HotWire":
		    		this.lblMachineMode.setText("Mode: HotWire");
		    		this.lblMachineMode.setForeground(Color.red);
		    		break;
		    	case "Mill":
		    		this.lblMachineMode.setText("Mode: Mill");
		    		this.lblMachineMode.setForeground(Color.blue);
		    		break;
		        default:
		            break;      
		    }
			
		}
		
	}
	
	@Override
	public void controlStateChange(ControlState state) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void fileStreamComplete(String filename, boolean success) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void receivedAlarm(Alarm alarm) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void commandSkipped(GcodeCommand command) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void commandSent(GcodeCommand command) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void commandComplete(GcodeCommand command) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void commandComment(String comment) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void probeCoordinates(Position p) {
		// TODO Auto-generated method stub
		
	}

}
