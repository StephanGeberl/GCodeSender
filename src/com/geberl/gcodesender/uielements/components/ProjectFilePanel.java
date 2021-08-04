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
import com.geberl.gcodesender.IController;
import com.geberl.gcodesender.Utils;
import com.geberl.gcodesender.listeners.ControllerListener;
import com.geberl.gcodesender.listeners.ControllerStatus;
import com.geberl.gcodesender.listeners.UGSEventListener;
import com.geberl.gcodesender.model.Alarm;
import com.geberl.gcodesender.model.BackendAPI;
import com.geberl.gcodesender.model.Position;
import com.geberl.gcodesender.model.ProjectFile;
import com.geberl.gcodesender.model.UGSEvent.ControlState;
import com.geberl.gcodesender.services.JogService;
import com.geberl.gcodesender.types.GcodeCommand;
import com.geberl.gcodesender.utils.FirmwareUtils;
import com.geberl.gcodesender.utils.GUIHelpers;
import com.geberl.gcodesender.utils.GcodeStreamReader;
import com.geberl.gcodesender.utils.IGcodeStreamReader;
import com.geberl.gcodesender.utils.Settings;
import com.geberl.gcodesender.utils.SettingsFactory;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.event.ActionEvent;

import static com.geberl.gcodesender.utils.GUIHelpers.displayErrorDialog;



public class ProjectFilePanel extends JPanel implements UGSEventListener, ControllerListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final BackendAPI backend;
	private Logger logger;
    private javax.swing.JFileChooser fileChooser;
    private Settings settings;
    private Timer timer;
    
    private int actualGCodeFile;
    private int maxGCodeFile;
    
    private ProjectFile actualProject = null;
    
	@SuppressWarnings("unused")
	private String gcodeFile = null;
	@SuppressWarnings("unused")
	private String processedGcodeFile = null;

    private ShowCommandPanelSimple showCommandPanel;

	private JLabel rowsValueLabel;
    private JLabel remainingTimeValueLabel;
    private JLabel sentRowsValueLabel;
    private JLabel remainingRowsValueLabel;
    private JLabel durationValueLabel;
    private JLabel fileNameLabel;
    private JLabel mainFileNameLabel;
    
    private JButton sendButton;
    private JButton cancelButton;
    private JButton pauseButton;
    private JButton fileBackButton;
    private JButton fileForewardButton;

    private JButton browseButton;

    
	public ProjectFilePanel(BackendAPI backend, Logger logger) {

		this.backend = backend;
        this.logger = logger;
        this.settings = SettingsFactory.loadSettings();
        
        fileChooser = new JFileChooser(settings.getLastOpenedFilename());
        initComponents();

        if (this.backend != null) {
        	
            this.backend.addUGSEventListener(this);
            this.backend.addControllerListener(this);
             
        }

     }
	
    private void initComponents() {
        
        setLayout(null);
        
        JLabel lblFileRows = new JLabel("Rows in File");
        lblFileRows.setFont(new Font("DejaVu Sans", Font.PLAIN, 12));
        lblFileRows.setBounds(280, 6, 80, 15);
        add(lblFileRows);
        
        JLabel lblSentRows = new JLabel("Sent Rows");
        lblSentRows.setFont(new Font("DejaVu Sans", Font.PLAIN, 12));
        lblSentRows.setBounds(280, 23, 80, 15);
        add(lblSentRows);
        
        JLabel lblRemRows = new JLabel("Rem. Rows");
        lblRemRows.setFont(new Font("DejaVu Sans", Font.PLAIN, 12));
        lblRemRows.setBounds(280, 40, 80, 15);
        add(lblRemRows);
        
        JLabel lblRemTime = new JLabel("Rem. Time");
        lblRemTime.setFont(new Font("DejaVu Sans", Font.PLAIN, 12));
        lblRemTime.setBounds(280, 59, 80, 15);
        add(lblRemTime);
        
        JLabel lblDuration = new JLabel("Duration");
        lblDuration.setFont(new Font("DejaVu Sans", Font.PLAIN, 12));
        lblDuration.setBounds(280, 77, 80, 15);
        add(lblDuration);

        JLabel lblFilename = new JLabel("Akt. File");
        lblFilename.setFont(new Font("DejaVu Sans", Font.PLAIN, 12));
        lblFilename.setBounds(280, 95, 80, 15);
        add(lblFilename);       

        JLabel lblMainFilename = new JLabel("Load File");
        lblMainFilename.setFont(new Font("DejaVu Sans", Font.PLAIN, 12));
        lblMainFilename.setBounds(280, 113, 80, 15);
        add(lblMainFilename);       
        
        
        rowsValueLabel = new JLabel("0");
        rowsValueLabel.setFont(new Font("DejaVu Sans", Font.PLAIN, 12));
        rowsValueLabel.setBounds(370, 6, 200, 15);
        add(rowsValueLabel);
        rowsValueLabel.setOpaque(true);
        
        sentRowsValueLabel = new JLabel("0");
        sentRowsValueLabel.setFont(new Font("DejaVu Sans", Font.PLAIN, 12));
        sentRowsValueLabel.setOpaque(true);
        sentRowsValueLabel.setBounds(370, 23, 200, 15);
        add(sentRowsValueLabel);
        
        remainingRowsValueLabel = new JLabel("0");
        remainingRowsValueLabel.setFont(new Font("DejaVu Sans", Font.PLAIN, 12));
        remainingRowsValueLabel.setOpaque(true);
        remainingRowsValueLabel.setBounds(370, 40, 200, 15);
        add(remainingRowsValueLabel);
        
        remainingTimeValueLabel = new JLabel("");
        remainingTimeValueLabel.setFont(new Font("DejaVu Sans", Font.PLAIN, 12));
        remainingTimeValueLabel.setBounds(370, 59, 200, 15);
        remainingTimeValueLabel.setText("--:--:--");
        add(remainingTimeValueLabel);
        
        durationValueLabel = new JLabel("");
        durationValueLabel.setFont(new Font("DejaVu Sans", Font.PLAIN, 12));
        durationValueLabel.setOpaque(true);
        durationValueLabel.setBounds(370, 77, 200, 15);
        durationValueLabel.setText("00:00:00");
        add(durationValueLabel);

        
        fileNameLabel = new JLabel("");
        fileNameLabel.setFont(new Font("DejaVu Sans", Font.PLAIN, 12));
        fileNameLabel.setOpaque(true);
        fileNameLabel.setBounds(370, 95, 200, 15);
        add(fileNameLabel);

        mainFileNameLabel = new JLabel("");
        mainFileNameLabel.setFont(new Font("DejaVu Sans", Font.PLAIN, 12));
        mainFileNameLabel.setOpaque(true);
        mainFileNameLabel.setBounds(370, 113, 200, 15);
        add(mainFileNameLabel);
        
        
        
        // -----------------------------------------
 
        browseButton = new JButton("Browse");
        browseButton.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
        browseButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		browseButtonActionPerformed(arg0);
        	}
        });
        browseButton.setBounds(6, 6, 120, 40);
        this.browseButton.setEnabled(false);
        add(browseButton);
        
        fileBackButton = new JButton("<");
        fileBackButton.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
        fileBackButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		if (actualGCodeFile > 1) {
        			actualGCodeFile = actualGCodeFile - 1;
        			setGcodePartFileByIndex(actualGCodeFile);
        		};
        	};
        });
        fileBackButton.setEnabled(false);
        fileBackButton.setBounds(137, 6, 55, 40);
        add(fileBackButton);

        fileForewardButton = new JButton(">");
        fileForewardButton.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
        fileForewardButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		if( actualGCodeFile < maxGCodeFile) {
        			actualGCodeFile = actualGCodeFile + 1;
        			setGcodePartFileByIndex(actualGCodeFile);
        		};
        	};
        });
        fileForewardButton.setEnabled(false);
        fileForewardButton.setBounds(197, 6, 55, 40);
        add(fileForewardButton);
        
        sendButton = new JButton("Send");
        sendButton.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
        sendButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		sendButtonActionPerformed(arg0);
        	}
        });
        sendButton.setEnabled(false);
        sendButton.setBounds(6, 52, 246, 40);
        add(sendButton);
        
        pauseButton = new JButton("Pause");
        pauseButton.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
        pauseButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
                try {
                    backend.pauseResume();
                } catch (Exception e) {
                    displayErrorDialog(e.getMessage());
                }
        	}
        });
        pauseButton.setEnabled(false);
        pauseButton.setBounds(6, 98, 120, 40);
        add(pauseButton);
        
        cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
        cancelButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
                try {
                    backend.cancel();
                } catch (Exception e) {
                    displayErrorDialog(e.getMessage());
                }
        	}
        });
        cancelButton.setEnabled(false);
        cancelButton.setBounds(132, 98, 120, 40);
        add(cancelButton);
        
        // -------------------------------
        
        this.showCommandPanel = new ShowCommandPanelSimple(this.backend);
        showCommandPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        showCommandPanel.setBounds(6,150,590,285);
        add(showCommandPanel);

        
    }
    
    private void browseButtonActionPerformed(java.awt.event.ActionEvent evt) {
        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                File gcodeFile = fileChooser.getSelectedFile();
                
                this.mainFileNameLabel.setText(gcodeFile.getName());
                
                Settings settings = backend.getSettings();
                settings.setLastOpenedFilename(gcodeFile.getAbsolutePath());
                SettingsFactory.saveSettings(settings);
                
                actualProject = new ProjectFile(gcodeFile);
                maxGCodeFile = actualProject.getNumGcodeFiles();
                
                actualGCodeFile = 1;
                this.setGcodePartFileByIndex(actualGCodeFile);
                
                if (backend.isConnected() && !backend.isIdle()) {
                    displayErrorDialog("Cannot delete output while commands are active.");
                    return;
                }
                
                this.showCommandPanel.clearContent();
                
            } catch (Exception ex) {
                logger.log(Level.SEVERE, "Problem while browsing.", ex);
                displayErrorDialog(ex.getMessage());
            }
        } else {
            // Canceled file open.
        }
    }

    
    private void setGcodePartFileByIndex(int aActualGcodeFileNum) {
    	
        int aIndex = aActualGcodeFileNum - 1;
    	
    	File partGcodeFile = ((actualProject.gCodePartList).get(aIndex)).gcodePart;
        GUIHelpers.openGcodeFile(partGcodeFile, backend);
 
        this.sendButton.setText("Send Nr." + Integer.toString(aActualGcodeFileNum) + 
        						" of " + Integer.toString(actualProject.getNumGcodeFiles()) + " File(s).");
    	
    }
    
    
    
    
    private void sendButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // Timer for updating duration labels.
    	
    	// GUIHelpers.openGcodeFile(actualProject.getGcodeFileAll(), backend);
    	
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                java.awt.EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            durationValueLabel.setText(Utils.formattedMillis(backend.getSendDuration()));
                            remainingTimeValueLabel.setText(Utils.formattedMillis(backend.getSendRemainingDuration()));
                            sentRowsValueLabel.setText(""+backend.getNumCompletedRows());
                            remainingRowsValueLabel.setText("" + backend.getNumRemainingRows());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        };

        this.resetTimerLabels();

        if (timer != null){ timer.stop(); }
        timer = new Timer(1000, actionListener);

        // Note: there is a divide by zero error in the timer because it uses
        //       the rowsValueLabel that was just reset.

        try {
            this.backend.send();
            this.resetSentRowLabels(backend.getNumRows());
            timer.start();
        } catch (Exception e) {
            timer.stop();
            logger.log(Level.INFO, "Exception in sendButtonActionPerformed.", e);
            displayErrorDialog(e.getMessage());
        }
        
    }

    private void resetTimerLabels() {
        // Reset labels
        this.durationValueLabel.setText("00:00:00");
        if (this.backend.isConnected()) {
            if (this.backend.getSendDuration() < 0) {
                this.remainingTimeValueLabel.setText("estimating...");
            } else if (this.backend.getSendDuration() == 0) {
                this.remainingTimeValueLabel.setText("--:--:--");
            } else {
                this.remainingTimeValueLabel.setText(Utils.formattedMillis(this.backend.getSendDuration()));
            }
        }
    }
   
    private void resetSentRowLabels(long numRows) {
        // Reset labels
        String totalRows =  String.valueOf(numRows);
        resetTimerLabels();
        this.sentRowsValueLabel.setText("0");
        this.remainingRowsValueLabel.setText(totalRows);
        this.rowsValueLabel.setText(totalRows);
    }

	@Override
	public void UGSEvent(com.geberl.gcodesender.model.UGSEvent evt) {
		
        this.cancelButton.setEnabled(backend.canCancel());
        this.pauseButton.setEnabled(backend.canPause() || backend.isPaused());
        this.pauseButton.setText(backend.getPauseResumeText());
        this.sendButton.setEnabled(backend.canSend());
        this.browseButton.setEnabled(backend.isIdle());
        
        boolean hasFile = backend.getGcodeFile() != null;
        if (hasFile) {
        	if (this.maxGCodeFile == 1) {
            	this.fileBackButton.setEnabled(false);
            	this.fileForewardButton.setEnabled(false);
        	}
        	else {
        		if (this.actualGCodeFile <= 1 ) {
        			this.fileBackButton.setEnabled(false);
        		}
        		else {
        			this.fileBackButton.setEnabled(backend.isIdle());
        		}
        		if (this.actualGCodeFile >= this.maxGCodeFile) {
        			this.fileForewardButton.setEnabled(false);
        		}
        		else {
        			this.fileForewardButton.setEnabled(backend.isIdle());
        		}
        	};
        	
        	
        }
        if (!hasFile) {
        	this.fileBackButton.setEnabled(false);
        	this.fileForewardButton.setEnabled(false);
        }

        if (evt.isFileChangeEvent()) {
            switch(evt.getFileState()) {
                case FILE_LOADING:
                    @SuppressWarnings("unused")
                    File f = backend.getGcodeFile();
                    this.fileNameLabel.setText(backend.getGcodeFile().getName());
                    this.fileNameLabel.setForeground(Color.RED);
                    processedGcodeFile = null;
                    gcodeFile = evt.getFile();
                    break;
                case FILE_LOADED:
                    processedGcodeFile = evt.getFile();
                    try {
                        try (IGcodeStreamReader gsr = new GcodeStreamReader(backend.getProcessedGcodeFile())) {
                            resetSentRowLabels(gsr.getNumRows());
                        }
                    } catch (IOException | GcodeStreamReader.NotGcodeStreamFile ex) {}
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

        if (success) {
            java.awt.EventQueue.invokeLater(new Runnable() { @Override public void run() {
            	
            	if (actualGCodeFile >= actualProject.getNumGcodeFiles()) {
            		// completed the last job
                	JOptionPane.showMessageDialog(new JFrame(),
                            "Last Job complete after " + Utils.formattedMillis(backend.getSendDuration()),
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    sendButton.setText("Send");
                    sendButton.setEnabled(false);    
            		
            	}
            	else {
            		// more jobs Left
            		
                	JOptionPane.showMessageDialog(new JFrame(),
                            "Job Nr. "+ Integer.toString(actualGCodeFile) + 
                            " complete after " + Utils.formattedMillis(backend.getSendDuration()) +
                            " change Tool to " + (actualProject.getNextToolString(actualGCodeFile)) + ", rezero Z and send next file.",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
            		
                	actualGCodeFile = actualGCodeFile + 1;
                	setGcodePartFileByIndex(actualGCodeFile);
            	}

            	
            	try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {}

 
            }});
        } else {
            displayErrorDialog("Job completed with error");
        }
		
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

	@Override
	public void statusStringListener(ControllerStatus status) {
		// TODO Auto-generated method stub
		
	}
}
    
    
