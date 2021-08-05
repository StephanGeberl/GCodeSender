/*
    Copyright 2020 Stephan Geberl

    This file is part of WireCutter/Mill GCode - Sender (WGS) (5 Axis-Version).
    WGS is derived from UGS by Will Winder (2012 - 2018)
    Major Changes:
    - should work with OpenJava
    - changed to Eclipse development
    - deleted all not needed parts (sorry, only English)
    - optimized for 4 Axis (Hot-Wire-Cutter) plus 1 Axis (Mill)
    - optimized (only) for GRBL special version (switch between WireCutter (M88) and Mill (M89) mode)
    - optimized for 800 x 480 Raspberry PI Touchscreen

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


package com.geberl.gcodesender;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

import java.awt.Toolkit;
import javax.swing.text.DefaultEditorKit;

import com.geberl.gcodesender.model.BackendAPI;
import com.geberl.gcodesender.model.GUIBackend;
import com.geberl.gcodesender.services.JogService;
import com.geberl.gcodesender.uielements.components.MoveCommandPanel;
import com.geberl.gcodesender.uielements.components.ShowCommandPanel;
import com.geberl.gcodesender.uielements.components.ConnectionPanel;
import com.geberl.gcodesender.uielements.components.MachineStatusPanel;
import com.geberl.gcodesender.uielements.components.ProjectFilePanel;
import com.geberl.gcodesender.uielements.components.SettingsPanel;
import com.geberl.gcodesender.utils.GUIHelpers;
import com.geberl.gcodesender.utils.KeepAwakeUtils;
import com.geberl.gcodesender.utils.Settings;
import com.geberl.gcodesender.utils.SettingsFactory;
import java.awt.Font;

/**
 * Main window for Universal Gcode Sender 5 Axis Version
 * Kiosk Mode for Rasberry with Standard Display
 * @author Stephan Geberl
 */
public class GrblSenderWindow extends JFrame {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger(GrblSenderWindow.class.getName());

    private Settings settings;
    private BackendAPI backend;
    private static Boolean isKioskMode;
    
    // My Variables
    private static final int mainWindowHeight = 480 + 40;
    private static final int mainWindowWidth = 800 + 10;

    // links
    private ConnectionPanel connectionPanel;
    private MachineStatusPanel machineStatusPanel;
    private ProjectFilePanel projectFilePanel;
    // rechts oben
    private MoveCommandPanel moveCommandPanel;
	// rechts unten
    private ShowCommandPanel showCommandPanel;
    private SettingsPanel settingsPanel;
	
	
    /** Creates new form MainWindow */
    public GrblSenderWindow(BackendAPI backend) {
 
    	this.backend = backend;
        this.settings = SettingsFactory.loadSettings();

        try {
            backend.applySettings(settings);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        
        initComponents();
        initProgram();
        KeepAwakeUtils.start(backend);
        
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down...");
            this.connectionPanel.saveSettings();
            this.showCommandPanel.saveSettings();
 
            SettingsFactory.saveSettings(settings);

        }));
    
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
    	
    	isKioskMode = false;
    	if ( args.length >= 1 && args[0].equals("kiosk") ) { 
    		isKioskMode = true;
    	};
        
       try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Metal".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }

         /* Create the form */
        GUIBackend backend = new GUIBackend();
        final GrblSenderWindow mw = new GrblSenderWindow(backend);
        
        mw.setSize(mainWindowWidth, mainWindowHeight);
        mw.setLocation(mw.settings.getMainWindowSettings().xLocation, mw.settings.getMainWindowSettings().yLocation);

        mw.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent ce) {

            }

            @Override
            public void componentMoved(ComponentEvent ce) {
                mw.settings.getMainWindowSettings().xLocation = ce.getComponent().getLocation().x;
                mw.settings.getMainWindowSettings().yLocation = ce.getComponent().getLocation().y;
            }

            @Override
            public void componentShown(ComponentEvent ce) {}
            @Override
            public void componentHidden(ComponentEvent ce) {}
        });

        /* Display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                mw.setVisible(true);
            }
        });

        // Check command line for a file to open.
        boolean open = false;
        for (String arg : args) {
            if (open) {
                try {
                    GUIHelpers.openGcodeFile(new File(arg), backend);
                    open = false;
                } catch (Exception ex) {
                    Logger.getLogger(GrblSenderWindow.class.getName()).log(Level.SEVERE, null, ex);
                    System.exit(1);
                }
            }
            if (arg.equals("--open") || arg.equals("-o")) {
                open = true;
            }
        }
    }

    private void initComponents() {

       	
    	GraphicsEnvironment graphics = GraphicsEnvironment.getLocalGraphicsEnvironment();
    	GraphicsDevice device = graphics.getDefaultScreenDevice();
        getContentPane().setLayout(null);

        JogService jogService = new JogService(backend);

        this.setTitle("HotWire/Mill 5 Axis GCode-Sender" + " (1.0)");
        
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(800, 480));
         
        this.connectionPanel = new ConnectionPanel(backend);
        // connectionPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        connectionPanel.setBorder(javax.swing.BorderFactory.createLoweredBevelBorder());
        connectionPanel.setBounds(0,0,190,345);
        getContentPane().add(connectionPanel);

        this.machineStatusPanel = new MachineStatusPanel(backend);
        // machineStatusPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        machineStatusPanel.setBorder(javax.swing.BorderFactory.createLoweredBevelBorder());
        machineStatusPanel.setBounds(0,345,190,135);
        getContentPane().add(machineStatusPanel);
        
        this.projectFilePanel = new ProjectFilePanel(backend, logger);
		this.moveCommandPanel = new MoveCommandPanel(backend, jogService, true);
        this.showCommandPanel = new ShowCommandPanel(backend);
		this.settingsPanel = new SettingsPanel(backend, jogService, true, settings);
        
        
        JTabbedPane tabbedPanelMain = new JTabbedPane();
        tabbedPanelMain.setFont(new Font("DejaVu Sans", Font.BOLD, 18));
        tabbedPanelMain.setBorder(javax.swing.BorderFactory.createLoweredBevelBorder());
        tabbedPanelMain.setBounds(190,0,610,480);
        tabbedPanelMain.addTab("Command Log",this.showCommandPanel);
        tabbedPanelMain.addTab("Manual Move",this.moveCommandPanel);
        tabbedPanelMain.addTab("Process File",this.projectFilePanel);
        tabbedPanelMain.addTab("Settings",this.settingsPanel);
        
        
        getContentPane().add(tabbedPanelMain);
        
        setSize(settings.getMainWindowSettings().width, settings.getMainWindowSettings().height);
        setSize(mainWindowWidth, mainWindowHeight);
        setLocation(settings.getMainWindowSettings().xLocation, settings.getMainWindowSettings().yLocation);
        
         if (isKioskMode) {
	        this.setUndecorated(true);
	        this.setResizable(false);
	        device.setFullScreenWindow(this);
        }
    
    }
        
    private void initProgram() {
        

        // Add keyboard listener for manual controls.
        KeyboardFocusManager.getCurrentKeyboardFocusManager()
        .addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                // Check context.
                if (e.getID() == KeyEvent.KEY_PRESSED) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_RIGHT:
                        case KeyEvent.VK_KP_RIGHT:
                        case KeyEvent.VK_NUMPAD6:
                            moveCommandPanel.xzPlusButtonActionPerformed();
                            e.consume();
                            return true;
                        case KeyEvent.VK_LEFT:
                        case KeyEvent.VK_KP_LEFT:
                        case KeyEvent.VK_NUMPAD4:
                            moveCommandPanel.xzMinusButtonActionPerformed();
                            e.consume();
                            return true;
                        case KeyEvent.VK_UP:
                        case KeyEvent.VK_KP_UP:
                        case KeyEvent.VK_NUMPAD8:
                            moveCommandPanel.yaPlusButtonActionPerformed();
                            e.consume();
                            return true;
                        case KeyEvent.VK_DOWN:
                        case KeyEvent.VK_KP_DOWN:
                        case KeyEvent.VK_NUMPAD2:
                            moveCommandPanel.yaMinusButtonActionPerformed();
                            e.consume();
                            return true;
                        case KeyEvent.VK_END:
                        case KeyEvent.VK_NUMPAD1:
                            moveCommandPanel.bMinusButtonActionPerformed();
                            e.consume();
                            return true;
                        case KeyEvent.VK_PAGE_DOWN:
                        case KeyEvent.VK_NUMPAD3:
                            moveCommandPanel.bPlusButtonActionPerformed();
                            e.consume();
                            return true;
/*                        case KeyEvent.VK_ADD:
                            moveCommandPanel.increaseStepActionPerformed();
                            e.consume();
                            return true;
                        case KeyEvent.VK_SUBTRACT:
                            moveCommandPanel.decreaseStepActionPerformed();
                            e.consume();
                            return true;
                        case KeyEvent.VK_DIVIDE:
                            moveCommandPanel.divideStepActionPerformed();
                            e.consume();
                            return true;
                        case KeyEvent.VK_MULTIPLY:
                            moveCommandPanel.multiplyStepActionPerformed();
                            e.consume();
                            return true;
*/                        
                        default:
                            break;
                    }
                }

                return false;
            }
        });
    }



}
