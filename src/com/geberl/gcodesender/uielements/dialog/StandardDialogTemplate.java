/*
    Copywrite 2013-2018 Will Winder

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

import java.awt.Frame;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.geberl.gcodesender.utils.GUIHelpers;

/**
 * Simple dialog for configuring Sender settings.
 *
 * @author wwinder
 */
public class StandardDialogTemplate extends JDialog {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean saveChanges;
    private final AbstractSettingsPanel settingsPanel;

    private final JButton restoreButton = new JButton("Restore");
    private final JButton closeWithSaveButton = new JButton("Save");
    private final JButton closeWithoutSaveButton = new JButton("Close");
    private final JButton helpButton = new JButton("Help");
    
    private int dialogHeight = 0;
	private int dialogWidth = 0;

	/**
     * Creates new form GrblSettingsDialog
     */
    public StandardDialogTemplate(String settingsTitle, AbstractSettingsPanel panel, Frame parent, boolean modal) {
        super(parent, modal);
        this.setTitle(settingsTitle);
        this.settingsPanel = panel;
        
        dialogHeight = panel.getHeight() + 80;
        // if (dialogHeight > 450) { dialogHeight = 450; };

        dialogWidth = panel.getWidth() + 12;
        // if (dialogWidth < 348) { dialogWidth = 348; };
        // if (dialogWidth > 750) { dialogWidth = 750; };

        this.restoreButton.addActionListener(event -> restoreDefaultSettings());
        this.closeWithSaveButton.addActionListener(event -> closeWithSaveActionPerformed());
        this.closeWithoutSaveButton.addActionListener(event -> closeWithoutSaveActionPerformed());
        this.helpButton.addActionListener(event -> helpButtonActionPerformed());
        
        this.initComponents();
        
        this.setLocationRelativeTo(parent);
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.saveChanges = false;
    }
    
    /**
     * Return status.
     */
    public boolean saveChanges() {
        return saveChanges;
    }

    private void initComponents() {
    	
    	setLayout(null);

        settingsPanel.setBounds(1, 1, dialogWidth - 12, dialogHeight -70 );
        add(settingsPanel);
        
        helpButton.setBounds(5, settingsPanel.getHeight() + 2, 70, 32);
        add(helpButton);
        restoreButton.setBounds(80, settingsPanel.getHeight() + 2, 90, 32);
        add(restoreButton);
        closeWithoutSaveButton.setBounds(175, settingsPanel.getHeight() + 2, 80, 32);
        add(closeWithoutSaveButton);
        closeWithSaveButton.setBounds(260, settingsPanel.getHeight() + 2, 80, 32);
        add(closeWithSaveButton);
        
        pack();
    }

    public void toggleRestoreButton(Boolean isEnabled) {
        this.restoreButton.setEnabled(isEnabled);
    }

    private void closeWithoutSaveActionPerformed() {
        this.saveChanges = false;
        setVisible(false);
    }

    private void restoreDefaultSettings() {
        try {
            settingsPanel.restoreDefaults();
        } catch (Exception e) {
            String message = "An error occurred while restoring defaults:" + e.getLocalizedMessage();
            GUIHelpers.displayErrorDialog(message);
        }
    }

    private void closeWithSaveActionPerformed() {
        this.settingsPanel.save();
        this.saveChanges = true;
        setVisible(false);
    }

    private void helpButtonActionPerformed() {
        String message = settingsPanel.getHelpMessage();
        
        JOptionPane.showMessageDialog(new JFrame(), 
                message, 
                "Sender Setting Help",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public int getDialogHeight() {
		return dialogHeight;
	}
    
    public int getDialogWidth() {
		return dialogWidth;
	}

}
