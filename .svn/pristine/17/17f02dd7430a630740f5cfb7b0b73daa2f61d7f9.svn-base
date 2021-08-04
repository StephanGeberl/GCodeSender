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

import static com.geberl.gcodesender.utils.GUIHelpers.displayErrorDialog;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import com.geberl.gcodesender.listeners.ControllerListener;
import com.geberl.gcodesender.listeners.ControllerState;
import com.geberl.gcodesender.listeners.ControllerStatus;
import com.geberl.gcodesender.listeners.UGSEventListener;
import com.geberl.gcodesender.model.Alarm;
import com.geberl.gcodesender.model.BackendAPI;
import com.geberl.gcodesender.model.Position;
import com.geberl.gcodesender.model.UGSEvent;
import com.geberl.gcodesender.services.JogService;
import com.geberl.gcodesender.types.GcodeCommand;
import com.geberl.gcodesender.utils.Settings;

public class MoveCommandPanel extends JPanelBackground implements UGSEventListener, ControllerListener {
	private static final long serialVersionUID = 1L;
	private static final Color colorSideOff = new Color(204,0,0);	
	private static final Color colorSideOn = new Color(102,255,102);	
	private static final Color colorMotorOff = new Color(204,204,204);	
	private static final Color colorMotorOn = new Color(102,255,102);	

	private BackendAPI backend;
	private JogService jogService;
	private boolean moveLeftSide = true;
	private boolean moveRightSide = true;
	
	private DoubleValueButton stepSizeButton = new DoubleValueButton("Step", 0.0, 200.00, 0.00);
	private DoubleValueButton feedRateButton = new DoubleValueButton("Speed", 0.0, 5000.00, 0.00);
	
	private JButton resetToolCoordinatesButton = new JButton("Reset Zero Tool");
	
	private JButton yaPlusButton = new JButton(new javax.swing.ImageIcon(getClass().getResource("/resources/icons/ButtonRedUp.gif"))); // "+Z (YA)"
	private JButton yaMinusButton = new JButton(new javax.swing.ImageIcon(getClass().getResource("/resources/icons/ButtonRedDown.gif"))); // "-Z (YA)"
	
	private JButton xzPlusButton = new JButton(new javax.swing.ImageIcon(getClass().getResource("/resources/icons/ButtonBlueForeward.gif"))); // "+Y (XZ)"
	private JButton xzMinusButton = new JButton(new javax.swing.ImageIcon(getClass().getResource("/resources/icons/ButtonBlueBack.gif"))); // "-Y (XZ)"

	private JButton bPlusButton = new JButton(new javax.swing.ImageIcon(getClass().getResource("/resources/icons/ButtonGreenRight.gif"))); // "+X (B)"
	private JButton bMinusButton = new JButton(new javax.swing.ImageIcon(getClass().getResource("/resources/icons/ButtonGreenLeft.gif"))); // "-X (B)"

	private J2StateButton toggleLeftSideButton = new J2StateButton(true, "ON (XY)", "OFF (XY)", colorSideOn, colorSideOff);
	private J2StateButton toggleRightSideButton = new J2StateButton(true, "ON (ZA)", "OFF (ZA)", colorSideOn, colorSideOff);

	private J2StateButton toggleMotorButton = new J2StateButton(false, "ON", "OFF", colorMotorOn, colorMotorOff);
	
	
    
	public MoveCommandPanel(BackendAPI backend, JogService jogService, boolean showKeyboardToggle) {

		setBackgroundImagePath("/resources/icons/Fraesenbild.gif");
		this.backend = backend;
        this.jogService = jogService;

		this.moveLeftSide = true;
		this.moveRightSide = true;

        initComponents();
        
        if (this.backend != null) {
        	
            this.backend.addUGSEventListener(this);
            this.backend.addControllerListener(this);

    		syncWithJogService();
        }

    }

	private void syncWithJogService() {
		Settings s = backend.getSettings();
		stepSizeButton.setValue(s.getManualModeStepSize());
		feedRateButton.setValue(s.getJogFeedRate());
	}

	
    private void initComponents() {
    	
        setLayout(null);
        
        stepSizeButton.setBounds(440, 12, 150, 50);
        stepSizeButton.setFont(new Font("DejaVu Sans", Font.BOLD, 12));
        stepSizeButton.setEnabled(false);
        add(stepSizeButton);
        stepSizeButton.addActionListener(e -> setStepSize());
        
        feedRateButton.setBounds(440, 72, 150, 50);
        feedRateButton.setFont(new Font("DejaVu Sans", Font.BOLD, 12));
        feedRateButton.setEnabled(false);
        add(feedRateButton);
        feedRateButton.addActionListener(e -> setFeedRate());
        
        // ===== Side
        toggleLeftSideButton.setBounds(45,365,115,50);
        toggleLeftSideButton.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
        toggleLeftSideButton.setEnabled(true);
        add(toggleLeftSideButton);

        toggleRightSideButton.setBounds(232,365,115,50);
        toggleRightSideButton.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
        toggleRightSideButton.setEnabled(true);
        add(toggleRightSideButton);
        
        toggleLeftSideButton.addActionListener(e -> toggleMoveSide());
        toggleRightSideButton.addActionListener(e -> toggleMoveSide());
        
        // ===== Motor
        toggleMotorButton.setBounds(244,224,80,50);
        toggleMotorButton.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
        toggleMotorButton.setEnabled(true);
        toggleMotorButton.addActionListener(e -> toggleMotor());
        add(toggleMotorButton);
         
        // ===== Move Buttons
       
        bMinusButton.setBounds(172, 12, 80, 50);
        // bMinusButton.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
        add(bMinusButton);
        bMinusButton.setEnabled(false);
        bMinusButton.addActionListener(e -> bMinusButtonActionPerformed());
        
        bPlusButton.setBounds(312, 12, 80, 50);
        // bPlusButton.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
        add(bPlusButton);
        bPlusButton.setEnabled(false);
        bPlusButton.addActionListener(e -> bPlusButtonActionPerformed());
        
        xzPlusButton.setBounds(403, 291, 80, 50);
        // xzPlusButton.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
        add(xzPlusButton);
        xzPlusButton.setEnabled(false);
        xzPlusButton.addActionListener(e -> xzPlusButtonActionPerformed());
        
        xzMinusButton.setBounds(453, 182, 80, 50);
        // xzMinusButton.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
        add(xzMinusButton);
        xzMinusButton.setEnabled(false);
        xzMinusButton.addActionListener(e -> xzMinusButtonActionPerformed());
        
        // Z- Achse
        yaPlusButton.setBounds(45, 99, 80, 50);
        // yaPlusButton.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
        add(yaPlusButton);
        yaPlusButton.setEnabled(false);
        yaPlusButton.addActionListener(e -> yaPlusButtonActionPerformed());
        
        yaMinusButton.setBounds(45, 210, 80, 50);
        // yaMinusButton.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
        add(yaMinusButton);
        yaMinusButton.setEnabled(false);
        yaMinusButton.addActionListener(e -> yaMinusButtonActionPerformed());

        
        // Reset Zero Tool (Tool Change)
        resetToolCoordinatesButton.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
        resetToolCoordinatesButton.setBounds(403,365,190,50);
        add(resetToolCoordinatesButton);

        // reset Functions
        resetToolCoordinatesButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
                try {
                 	backend.resetZaxisCoordinatesToZero();
                 } catch (Exception ex) {
                     displayErrorDialog(ex.getMessage());
                 }
        	}
        });
        resetToolCoordinatesButton.setEnabled(false);        
        
        
        
    }
    
    
    @Override
    public void UGSEvent(UGSEvent evt) {
    	if (evt.isStateChangeEvent() || evt.isSettingChangeEvent()) {
    		updateManualControls(false);
         }
    }

	// Set Step and Speed
    private void setStepSize() {
		Settings s = backend.getSettings();
		s.setManualModeStepSize(this.stepSizeButton.getValue());
    }

	// Set Step and Speed
    private void setFeedRate() {
		Settings s = backend.getSettings();
		s.setJogFeedRate(this.feedRateButton.getValue());
    }

    // Handle Side
	private void toggleMoveSide() {
		this.moveRightSide = this.toggleRightSideButton.getState();
		this.moveLeftSide = this.toggleLeftSideButton.getState();
		
	}

	// Handle Motor
	private void toggleMotor() {
		
		if (toggleMotorButton.getState()) {
			try {
				backend.switchOnSpindle();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			try {
				backend.switchOffSpindle();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		};
		
		
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

        if (status.getState() == ControllerState.IDLE) {
        	this.updateManualControls(!backend.isSendingFile());
        	return;
        };
       
       	this.updateManualControls(false);
	}

    // Bewegung 
	public void doJog(int x, int y, int z, int a) {
		try {
			jogService.adjustManualLocation(x, y, z, a);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void doJog(int x, int y, int z, int a, int b) {
		try {
			jogService.adjustManualLocation(x, y, z, a, b);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ***** Buttons Actions *****
	// ***** Steuerung *****

 	
	// ***** Bewegung *****
	public void xzPlusButtonActionPerformed() {
		if (this.moveRightSide && this.moveLeftSide) { this.doJog(1, 0, 1, 0, 0); }
		else {
			if (this.moveRightSide) { this.doJog(0, 0, 1, 0, 0); };
			if (this.moveLeftSide) { this.doJog(1, 0, 0, 0, 0); };
		};
	}

	public void xzMinusButtonActionPerformed() {
		if (this.moveRightSide && this.moveLeftSide) { this.doJog(-1, 0, -1, 0, 0); }
		else {
			if (this.moveRightSide) { this.doJog(0, 0, -1, 0, 0); };
			if (this.moveLeftSide) { this.doJog(-1, 0, 0, 0, 0); };
		};
	}

	public void yaPlusButtonActionPerformed() {
		if (this.moveRightSide && this.moveLeftSide) { this.doJog(0, 1, 0, 1, 0); }
		else {
			if (this.moveRightSide) { this.doJog(0, 0, 0, 1, 0); };
			if (this.moveLeftSide) { this.doJog(0, 1, 0, 0, 0); };
		};
	}

	public void yaMinusButtonActionPerformed() {
		if (this.moveRightSide && this.moveLeftSide) { this.doJog(0, -1, 0, -1, 0); }
		else {
			if (this.moveRightSide) { this.doJog(0, 0, 0, -1, 0); };
			if (this.moveLeftSide) { this.doJog(0, -1, 0, 0, 0); };
		};
	}

	public void bPlusButtonActionPerformed() {
		if (backend.isMillMode()) { this.doJog(0, 0, 0, 0, 1); };
	}

	public void bMinusButtonActionPerformed() {
		if (backend.isMillMode()) { this.doJog(0, 0, 0, 0, -1); };
	}
	// ***** Ende Buttons Actions *****

	
	// ***** Enable/Disable Actions *****

	public void updateManualControls(boolean enabled) {
		
		if (this.backend.isMillMode()) {
			setBackgroundImagePath("/resources/icons/Fraesenbild.gif");
		}
		else {
			setBackgroundImagePath("/resources/icons/HotWireBild.gif");
		};
		
		
		// Enabled when connected
		this.yaMinusButton.setEnabled(enabled);
		this.yaPlusButton.setEnabled(enabled);
		this.xzMinusButton.setEnabled(enabled);
		this.xzPlusButton.setEnabled(enabled);
		this.stepSizeButton.setEnabled(enabled);
		this.feedRateButton.setEnabled(enabled);
		this.resetToolCoordinatesButton.setEnabled(enabled);        

		
		// Enabled depending Mill or HotWire
		if (enabled == false) {
			this.bPlusButton.setEnabled(enabled);
			this.bMinusButton.setEnabled(enabled);
			
			this.toggleRightSideButton.setState(true);
			this.toggleLeftSideButton.setState(true);
			this.toggleMotorButton.setState(false);

			this.toggleRightSideButton.setEnabled(false);
			this.toggleLeftSideButton.setEnabled(false);
			this.toggleMotorButton.setEnabled(false);
		}
		else {
			if (this.backend.isMillMode()) {

				this.bPlusButton.setEnabled(true);
				this.bMinusButton.setEnabled(true);
				
				this.toggleRightSideButton.setEnabled(false);
				this.toggleLeftSideButton.setEnabled(false);
				this.toggleRightSideButton.setState(true);
				this.toggleLeftSideButton.setState(true);
				
				this.toggleMotorButton.setEnabled(true);
			}
			else {

				this.bPlusButton.setEnabled(false);
				this.bMinusButton.setEnabled(false);
				
				this.toggleRightSideButton.setEnabled(true);
				this.toggleLeftSideButton.setEnabled(true);
				
				this.toggleMotorButton.setEnabled(false);
				this.toggleMotorButton.setState(false);
			};
		};
	
	}


}
