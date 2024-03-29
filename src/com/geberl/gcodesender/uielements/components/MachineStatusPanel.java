/*
    Copyright 2020 Stephan Geberl

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

import com.geberl.gcodesender.Utils;
import com.geberl.gcodesender.listeners.ControllerListener;
import com.geberl.gcodesender.listeners.ControllerState;
import com.geberl.gcodesender.listeners.ControllerStatus;
import com.geberl.gcodesender.listeners.UGSEventListener;
import com.geberl.gcodesender.model.Alarm;
import com.geberl.gcodesender.model.BackendAPI;
import com.geberl.gcodesender.model.Position;
import com.geberl.gcodesender.model.UGSEvent.ControlState;
import com.geberl.gcodesender.model.UnitUtils;
import com.geberl.gcodesender.types.GcodeCommand;
import com.geberl.gcodesender.utils.Settings;
import com.geberl.gcodesender.utils.SettingsFactory;
import java.awt.Color;
import java.awt.Font;

public class MachineStatusPanel extends JPanel implements ControllerListener, UGSEventListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final BackendAPI backend;

    private Settings settings;

    private JLabel activeStateValueLabel;
    private JLabel workingUnitsValueLabel;

    private JLabel workPositionBValueLabel;
    private JLabel workPositionXValueLabel;
    private JLabel workPositionZValueLabel;
    private JLabel workPositionYValueLabel;
    private JLabel workPositionAValueLabel;
    private JLabel machinePositionBValueLabel;
    private JLabel machinePositionXValueLabel;
    private JLabel machinePositionZValueLabel;
    private JLabel machinePositionYValueLabel;
    private JLabel machinePositionAValueLabel;

    
	public MachineStatusPanel(BackendAPI backend) {
		
		// Breite: 190
		// Höhe: 155

        this.backend = backend;
        this.settings = SettingsFactory.loadSettings();

        initComponents();
        
        if (this.backend != null) {
        	
            this.backend.addUGSEventListener(this);
            this.backend.addControllerListener(this);
             
        }
        
     }
	
    private void initComponents() {
        
        setLayout(null);
        
        JLabel lblNewLabel = new JLabel("Active State");
        lblNewLabel.setFont(new Font("DejaVu Sans", Font.PLAIN, 12));
        lblNewLabel.setBounds(10, 6, 80, 15);
        add(lblNewLabel);
        
        JLabel lblNewLabel_3 = new JLabel("Scale Unit");
        lblNewLabel_3.setFont(new Font("DejaVu Sans", Font.PLAIN, 12));
        lblNewLabel_3.setBounds(10, 26, 80, 15);
        add(lblNewLabel_3);
        
        JLabel lblNewLabel_4_1 = new JLabel("Axis");
        lblNewLabel_4_1.setHorizontalAlignment(SwingConstants.LEFT);
        lblNewLabel_4_1.setFont(new Font("DejaVu Sans", Font.BOLD, 12));
        lblNewLabel_4_1.setBounds(10, 46, 40, 15);
        add(lblNewLabel_4_1);
        
        JLabel lblNewLabel_4 = new JLabel("Work");
        lblNewLabel_4.setHorizontalAlignment(SwingConstants.RIGHT);
        lblNewLabel_4.setFont(new Font("DejaVu Sans", Font.BOLD, 12));
        lblNewLabel_4.setBounds(50, 46, 60, 15);
        add(lblNewLabel_4);
        
        JLabel lblNewLabel_5 = new JLabel("Machine");
        lblNewLabel_5.setHorizontalAlignment(SwingConstants.RIGHT);
        lblNewLabel_5.setFont(new Font("DejaVu Sans", Font.BOLD, 12));
        lblNewLabel_5.setBounds(119, 46, 60, 15);
        add(lblNewLabel_5);
        
        JLabel lblNewLabel_6 = new JLabel("X (Y1)");
        lblNewLabel_6.setFont(new Font("DejaVu Sans", Font.PLAIN, 12));
        lblNewLabel_6.setBounds(10, 73, 40, 15);
        add(lblNewLabel_6);
        
        JLabel lblNewLabel_7 = new JLabel("Z (Y2)");
        lblNewLabel_7.setFont(new Font("DejaVu Sans", Font.PLAIN, 12));
        lblNewLabel_7.setBounds(10, 87, 40, 15);
        add(lblNewLabel_7);
        
        JLabel lblNewLabel_8 = new JLabel("B (X)");
        lblNewLabel_8.setFont(new Font("DejaVu Sans", Font.PLAIN, 12));
        lblNewLabel_8.setBounds(10, 60, 40, 15);
        add(lblNewLabel_8);
        
        JLabel lblNewLabel_9 = new JLabel("Y (Z1)");
        lblNewLabel_9.setFont(new Font("DejaVu Sans", Font.PLAIN, 12));
        lblNewLabel_9.setBounds(10, 100, 40, 15);
        add(lblNewLabel_9);
        
        JLabel lblNewLabel_10 = new JLabel("A (Z2)");
        lblNewLabel_10.setFont(new Font("DejaVu Sans", Font.PLAIN, 12));
        lblNewLabel_10.setBounds(10, 114, 40, 15);
        add(lblNewLabel_10);
        
        activeStateValueLabel = new JLabel("");
        activeStateValueLabel.setFont(new Font("DejaVu Sans", Font.BOLD, 12));
        activeStateValueLabel.setBounds(90, 6, 89, 15);
        add(activeStateValueLabel);
        activeStateValueLabel.setOpaque(true);
 
        workingUnitsValueLabel = new JLabel("");
        workingUnitsValueLabel.setFont(new Font("DejaVu Sans", Font.BOLD, 12));
        workingUnitsValueLabel.setBounds(90, 26, 89, 15);
        add(workingUnitsValueLabel);
        
        workPositionBValueLabel = new JLabel("0");
        workPositionBValueLabel.setFont(new Font("DejaVu Sans", Font.PLAIN, 12));
        workPositionBValueLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        workPositionBValueLabel.setBounds(50, 60, 60, 15);
        add(workPositionBValueLabel);
        
        workPositionXValueLabel = new JLabel("0");
        workPositionXValueLabel.setFont(new Font("DejaVu Sans", Font.PLAIN, 12));
        workPositionXValueLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        workPositionXValueLabel.setBounds(50, 73, 60, 15);
        add(workPositionXValueLabel);
        
        workPositionZValueLabel = new JLabel("0");
        workPositionZValueLabel.setFont(new Font("DejaVu Sans", Font.PLAIN, 12));
        workPositionZValueLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        workPositionZValueLabel.setBounds(50, 87, 60, 15);
        add(workPositionZValueLabel);
        
        workPositionYValueLabel = new JLabel("0");
        workPositionYValueLabel.setFont(new Font("DejaVu Sans", Font.PLAIN, 12));
        workPositionYValueLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        workPositionYValueLabel.setBounds(50, 100, 60, 15);
        add(workPositionYValueLabel);
        
        workPositionAValueLabel = new JLabel("0");
        workPositionAValueLabel.setFont(new Font("DejaVu Sans", Font.PLAIN, 12));
        workPositionAValueLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        workPositionAValueLabel.setBounds(50, 114, 60, 15);
        add(workPositionAValueLabel);
        
        machinePositionBValueLabel = new JLabel("0");
        machinePositionBValueLabel.setFont(new Font("DejaVu Sans", Font.PLAIN, 12));
        machinePositionBValueLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        machinePositionBValueLabel.setBounds(119, 60, 60, 15);
        add(machinePositionBValueLabel);
        
        machinePositionXValueLabel = new JLabel("0");
        machinePositionXValueLabel.setFont(new Font("DejaVu Sans", Font.PLAIN, 12));
        machinePositionXValueLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        machinePositionXValueLabel.setBounds(119, 73, 60, 15);
        add(machinePositionXValueLabel);
        
        machinePositionZValueLabel = new JLabel("0");
        machinePositionZValueLabel.setFont(new Font("DejaVu Sans", Font.PLAIN, 12));
        machinePositionZValueLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        machinePositionZValueLabel.setBounds(119, 87, 60, 15);
        add(machinePositionZValueLabel);
        
        machinePositionYValueLabel = new JLabel("0");
        machinePositionYValueLabel.setFont(new Font("DejaVu Sans", Font.PLAIN, 12));
        machinePositionYValueLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        machinePositionYValueLabel.setBounds(119, 100, 60, 15);
        add(machinePositionYValueLabel);
        
        machinePositionAValueLabel = new JLabel("0");
        machinePositionAValueLabel.setFont(new Font("DejaVu Sans", Font.PLAIN, 12));
        machinePositionAValueLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        machinePositionAValueLabel.setBounds(119, 114, 60, 15);
        add(machinePositionAValueLabel);
        
        
    }

    
    
	@Override
    public void statusStringListener(ControllerStatus status) {
        if (status == null) {
            return;
        }

        this.activeStateValueLabel.setText( Utils.getControllerStateText(status.getState()) );
        this.setStatusColorForState( status.getState() );

        UnitUtils.Units units = settings.getPreferredUnits();
        this.workingUnitsValueLabel.setText(units.abbreviation);
        
        if (status.getMachineCoord() != null) {
            Position machineCoord = status.getMachineCoord().getPositionIn(units);
            this.machinePositionXValueLabel.setText( Utils.formatter.format(machineCoord.x));
            this.machinePositionYValueLabel.setText( Utils.formatter.format(machineCoord.y));
            this.machinePositionZValueLabel.setText( Utils.formatter.format(machineCoord.z));
            this.machinePositionAValueLabel.setText( Utils.formatter.format(machineCoord.a));
            this.machinePositionBValueLabel.setText( Utils.formatter.format(machineCoord.b));
        }
        
        if (status.getWorkCoord() != null) {
            Position workCoord = status.getWorkCoord().getPositionIn(units);
            this.workPositionXValueLabel.setText( Utils.formatter.format(workCoord.x));
            this.workPositionYValueLabel.setText( Utils.formatter.format(workCoord.y));
            this.workPositionZValueLabel.setText( Utils.formatter.format(workCoord.z));
            this.workPositionAValueLabel.setText( Utils.formatter.format(workCoord.a));
            this.workPositionBValueLabel.setText( Utils.formatter.format(workCoord.b));
        }
    }

    private void updateControls() {
        
        switch (backend.getControlState()) {
            case COMM_DISCONNECTED:
                this.setStatusColorForState(ControllerState.UNKNOWN);
                break;
            case COMM_IDLE:
                break;
            case COMM_SENDING:
                break;
            case COMM_SENDING_PAUSED:

                break;
            default:
                
        }
    }
   
    
    private void setStatusColorForState(ControllerState state) {
        Color color = null; // default to a transparent background.
        if (state == ControllerState.ALARM) {
            color = Color.RED;
        } else if (state == ControllerState.HOLD || state == ControllerState.DOOR || state == ControllerState.SLEEP) {
            color = Color.YELLOW;
        } else if (state == ControllerState.RUN || state == ControllerState.JOG || state == ControllerState.HOME) {
            color = Color.GREEN;
        } else {
            color = Color.WHITE;
        }


        this.activeStateValueLabel.setBackground(color);
    }

	@Override
	public void UGSEvent(com.geberl.gcodesender.model.UGSEvent evt) {
		
        if (evt.isFileChangeEvent() || evt.isStateChangeEvent()) {
            this.updateControls();
        }

        if (evt.isSettingChangeEvent() && backend.getController() != null && backend.getController().getControllerStatus() != null) {
            statusStringListener(backend.getController().getControllerStatus());
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
