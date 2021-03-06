/*
    Copyright 2020 Stephan Geberl

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
package com.geberl.gcodesender.uielements.components;

import javax.swing.*;

import static com.geberl.gcodesender.utils.GUIHelpers.displayErrorDialog;
import com.geberl.gcodesender.listeners.ControllerListener;
import com.geberl.gcodesender.listeners.ControllerStatus;
import com.geberl.gcodesender.listeners.UGSEventListener;
import com.geberl.gcodesender.model.Alarm;
import com.geberl.gcodesender.model.BackendAPI;
import com.geberl.gcodesender.model.Position;
import com.geberl.gcodesender.model.UGSEvent.ControlState;
import com.geberl.gcodesender.types.GcodeCommand;
import com.geberl.gcodesender.utils.Settings;
import com.geberl.gcodesender.utils.SettingsFactory;

import java.awt.Font;

public class ShowCommandPanel extends JPanel implements UGSEventListener, ControllerListener {
	
    private Settings settings;
	private BackendAPI backend;
	private JCheckBox scrollWindowCheckBox;
	private JCheckBox showVerboseOutputCheckBox;
	private GcodeTable commandTable;
	private CommandPanel commandPanel;
	private JScrollPane commandTableScrollPane;
	private JTabbedPane bottomTabbedPane;
	private JButton btnClearOutput;
    
	public ShowCommandPanel(BackendAPI backend) {
        
        this.backend = backend;
        this.settings = SettingsFactory.loadSettings();
        
        this.initializeComponents();
        loadSettings();

        if (this.backend != null) {
        	
            this.backend.addUGSEventListener(this);
            this.backend.addControllerListener(this);
             
        }
        
    }


	private void initializeComponents() {
        setLayout(null);

        // Checkboxen
        scrollWindowCheckBox = new JCheckBox("Scroll output window");
        scrollWindowCheckBox.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
        scrollWindowCheckBox.setBounds(6, 2, 220, 40);
        add(scrollWindowCheckBox);
        scrollWindowCheckBox.addActionListener(this::scrollWindowCheckBoxActionPerformed);
        
        showVerboseOutputCheckBox = new JCheckBox("Show verbose output");
        showVerboseOutputCheckBox.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
        showVerboseOutputCheckBox.setBounds(226, 2, 220, 40);
        add(showVerboseOutputCheckBox);
        showVerboseOutputCheckBox.addActionListener(this::showVerboseCheckBoxActionPerformed);
        
      
        btnClearOutput = new JButton("Clear");
        btnClearOutput.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
        btnClearOutput.setBounds(450, 2, 150, 40);
        add(btnClearOutput);
        btnClearOutput.addActionListener(this::clearOutputButtonPerformed);
        
/*
        btnExitProgramm = new JButton("Exit Programm");
        btnExitProgramm.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		Container frame = btnExitProgramm.getParent();
                do 
                    frame = frame.getParent(); 
                while (!(frame instanceof JFrame));                                      
                ((JFrame) frame).dispose();
        	}
        });
        btnExitProgramm.setBounds(485, 3, 120, 27);
        add(btnExitProgramm);
 */       
        
        // Inhalt (Tabs)
        bottomTabbedPane = new JTabbedPane();
        bottomTabbedPane.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
        bottomTabbedPane.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        // bottomTabbedPane.setBorder(javax.swing.BorderFactory.createLineBorder(Color.blue));
        bottomTabbedPane.setBounds(2, 50, 596, 390);
        add(bottomTabbedPane);
        
        commandTable = new GcodeTable();
        commandTable.getTableHeader().setReorderingAllowed(false);
        commandTableScrollPane = new JScrollPane();
        commandTableScrollPane.setViewportView(commandTable);
        
        commandPanel = new CommandPanel(backend);
        
        bottomTabbedPane.addTab("Commands", commandPanel);
        bottomTabbedPane.addTab("Command Table", commandTableScrollPane);        
        
 /*       this.addComponentListener(new ComponentAdapter() {
    		public void componentResized(ComponentEvent e) { 
    		    bottomTabbedPane.setBounds(2, 30, e.getComponent().getWidth() - 4, e.getComponent().getHeight() - 32);
    		}
    	    });
*/

	}
	
    public void loadSettings() {
        scrollWindowCheckBox.setSelected(settings.isScrollWindowEnabled());
        showVerboseOutputCheckBox.setSelected(settings.isVerboseOutputEnabled());
     }

    public void saveSettings() {
    	settings.setScrollWindowEnabled(scrollWindowCheckBox.isSelected());
    	settings.setVerboseOutputEnabled(showVerboseOutputCheckBox.isSelected());
    }
	
    private void scrollWindowCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {
        backend.getSettings().setScrollWindowEnabled(scrollWindowCheckBox.isSelected());
    }

    private void showVerboseCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {
        backend.getSettings().setVerboseOutputEnabled(showVerboseOutputCheckBox.isSelected());
    }
 
    private void clearOutputButtonPerformed(java.awt.event.ActionEvent evt) {
    	
        if (backend.isConnected() && !backend.isIdle()) {
            displayErrorDialog("Cannot delete output while commands are active.");
            return;
        }
        this.commandTable.clear();
        this.commandTable.repaint();
        this.commandPanel.clearCommandList();
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
        commandSent(command);
	}

	@Override
	public void commandSent(GcodeCommand command) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                // sent
                if (commandTableScrollPane.isEnabled()) {
                    commandTable.addRow(command);
                }
                commandTable.updateRow(command);
            }});
	}

	@Override
    public void commandComplete(final GcodeCommand command) {
        //String gcodeString = command.getCommandString().toLowerCase();
        
        // update gui
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (commandTableScrollPane.isEnabled()) {
                    commandTable.updateRow(command);
                }

                if (backend.isSendingFile()) {
                	
                }
            }});
    }

	@Override
	public void commandComment(String comment) {
		// TODO Auto-generated method stub
	}

	@Override
	public void probeCoordinates(Position p) {
		// TODO Auto-generated method stub
	}

	@Override
	public void statusStringListener(ControllerStatus status) {
		// TODO Auto-generated method stub
	}

	@Override
	public void UGSEvent(com.geberl.gcodesender.model.UGSEvent evt) {
        if (evt.isSettingChangeEvent()) {
        	scrollWindowCheckBox.setSelected(backend.getSettings().isScrollWindowEnabled());
        	showVerboseOutputCheckBox.setSelected(backend.getSettings().isVerboseOutputEnabled());
        	commandTable.setAutoWindowScroll(backend.getSettings().isScrollWindowEnabled());
        }
        
        if (evt.isStateChangeEvent()) {
        	scrollWindowCheckBox.setEnabled(backend.isIdle());
        	showVerboseOutputCheckBox.setEnabled(backend.isIdle());
        	btnClearOutput.setEnabled(backend.isIdle());
          }
		
	}
}
