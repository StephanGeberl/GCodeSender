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

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

import com.geberl.gcodesender.listeners.MessageListener;
import com.geberl.gcodesender.listeners.MessageType;
import com.geberl.gcodesender.listeners.UGSEventListener;
import com.geberl.gcodesender.model.BackendAPI;
import com.geberl.gcodesender.model.UGSEvent;
import com.geberl.gcodesender.utils.SwingHelpers;

/**
 * A panel for displaying console messages and a command line for manually entering commands.
 * It will automatically register it self as a {@link MessageListener} and {@link UGSEventListener}.
 *
 * @author wwinder
 */
public class CommandPanel extends JPanel implements UGSEventListener, MessageListener {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final int CONSOLE_SIZE = 1024 * 1024;

    private final BackendAPI backend;

    private final JScrollPane scrollPane = new JScrollPane();
    private final JTextArea consoleTextArea = new JTextArea();
    private final CommandTextArea commandTextField;
    private final JLabel commandLabel = new JLabel("Command:");

    private final JPopupMenu menu = new JPopupMenu();
    private final JCheckBoxMenuItem showVerboseMenuItem = new JCheckBoxMenuItem("Show verbose output");
    private final JCheckBoxMenuItem scrollWindowMenuItem = new JCheckBoxMenuItem("Scroll output window");



    public CommandPanel(BackendAPI backend) {
        this.backend = backend;
        if (backend != null) {
            backend.addUGSEventListener(this);
            backend.addMessageListener(this);
        }
        commandTextField = new CommandTextArea(backend);
        initComponents();
        loadSettings();
    }


    private void initComponents() {
    	
        setLayout(null);

        consoleTextArea.setEditable(false);
        consoleTextArea.setColumns(20);
        consoleTextArea.setDocument(new LengthLimitedDocument(CONSOLE_SIZE));
        consoleTextArea.setRows(5);
        consoleTextArea.setMaximumSize(new java.awt.Dimension(32767, 32767));
        consoleTextArea.setMinimumSize(new java.awt.Dimension(0, 0));
        scrollPane.setViewportView(consoleTextArea);
        commandLabel.setEnabled(backend.isIdle());

        scrollWindowMenuItem.addActionListener(e -> checkScrollWindow());

        scrollPane.setBounds(0, 0, 590, 328);
        add(scrollPane);
        
        commandLabel.setBounds(0, 330, 80, 26);
        add(commandLabel);
        commandTextField.setBounds(80, 330, 510, 26);
        add(commandTextField);

        menu.add(showVerboseMenuItem);
        menu.add(scrollWindowMenuItem);
        SwingHelpers.traverse(this, (comp) -> comp.setComponentPopupMenu(menu));
    }

    @Override
    public void UGSEvent(UGSEvent evt) {
        if (evt.isSettingChangeEvent()) {
            loadSettings();
        }

        if (evt.isStateChangeEvent()) {
            commandLabel.setEnabled(backend.isIdle());
         }
    }

    /**
     * When new messages are created this method will be called.
     * It will decide using the {@code messageType} if the message should be written to the console.
     *
     * @param messageType the type of message to be written
     * @param message     the message to be written to the console
     */
    @Override
    public void onMessage(MessageType messageType, String message) {
        java.awt.EventQueue.invokeLater(() -> {
            boolean verbose = MessageType.VERBOSE.equals(messageType);
            if (messageType.equals(MessageType.ERROR)) {
                consoleTextArea.append("[" +  messageType.getLocalizedString() + "] " + message);
            } else if (!verbose || showVerboseMenuItem.isSelected()) {
                if (verbose) {
                    consoleTextArea.append("[" + messageType.getLocalizedString() + "] ");
                }
                consoleTextArea.append(message);

                if (consoleTextArea.isVisible() && scrollWindowMenuItem.isSelected()) {
                    consoleTextArea.setCaretPosition(consoleTextArea.getDocument().getLength());
                }
            }
        });
    }

    private void checkScrollWindow() {
        // Console output.
        DefaultCaret caret = (DefaultCaret) consoleTextArea.getCaret();
        if (scrollWindowMenuItem.isSelected()) {
            caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
            consoleTextArea.setCaretPosition(consoleTextArea.getDocument().getLength());
        } else {
            caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
        }
    }

    public void clearCommandList() {
    	consoleTextArea.setText("");
    	
    }
    
    public void loadSettings() {
        scrollWindowMenuItem.setSelected(backend.getSettings().isScrollWindowEnabled());
        showVerboseMenuItem.setSelected(backend.getSettings().isVerboseOutputEnabled());
        checkScrollWindow();
    }

    public void saveSettings() {
        backend.getSettings().setScrollWindowEnabled(scrollWindowMenuItem.isSelected());
        backend.getSettings().setVerboseOutputEnabled(showVerboseMenuItem.isSelected());
    }
}
