/**
 * Configure the controller settings.
 */
/*
    Copywrite 2016 Will Winder

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

import com.geberl.gcodesender.gcode.util.CommandProcessorLoader;
import com.geberl.gcodesender.uielements.components.ProcessorConfigCheckbox;
import com.geberl.gcodesender.utils.FirmwareUtils;
import com.geberl.gcodesender.utils.GUIHelpers;
import com.geberl.gcodesender.utils.Settings;
import com.geberl.gcodesender.utils.ControllerSettings.ProcessorConfig;
import com.geberl.gcodesender.utils.ControllerSettings.ProcessorConfigGroups;
import com.geberl.gcodesender.utils.FirmwareUtils.ConfigTuple;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

/**
 *
 * @author wwinder
 */
public class ControllerProcessorSettingsPanel extends AbstractSettingsPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Map<String,ConfigTuple> configFiles;
	private final JTable customRemoverTable;
	private final JButton add = new JButton("Add");
	private final JButton remove = new JButton("Remove Selected");

    public ControllerProcessorSettingsPanel(Settings settings, IChanged changer, Map<String,ConfigTuple> configFiles) {
        super(settings, changer);
        this.configFiles = configFiles;

        this.customRemoverTable = initCustomRemoverTable(new JTable());
        updateComponentsInternal(settings);
    }

    public ControllerProcessorSettingsPanel(Settings settings, Map<String,ConfigTuple> configFiles) {
        this(settings, null, configFiles);
    }

	@Override
	public int getHeight() { return 385; }
    @Override
	public int getWidth() { return 365; }
    
    
    private void addNewPatternRemover() {
        DefaultTableModel model = (DefaultTableModel) this.customRemoverTable.getModel();
        model.addRow(new Object[]{true, ""});
    }

    private void removeSelectedPatternRemover() {
        int[] rows = customRemoverTable.getSelectedRows();
        Arrays.sort(rows);

        DefaultTableModel model = (DefaultTableModel) this.customRemoverTable.getModel();
        for (int i = rows.length; i > 0; i--) {
            int row = rows[i-1];
            model.removeRow(row);
        }
    }

    @Override
    public void save() {
        // In case there are in-progress changes.
        TableCellEditor editor = customRemoverTable.getCellEditor();
        if (editor != null) {
            editor.stopCellEditing();
        }

        ConfigTuple ct = configFiles.get("GRBL");
        ct.loader.getProcessorConfigs().Custom.clear();

        // Roll up the pattern processors.
        DefaultTableModel model = (DefaultTableModel) this.customRemoverTable.getModel();
        for (int i = 0; i < customRemoverTable.getRowCount(); i++) {
            JsonObject args = new JsonObject();
            String pattern = model.getValueAt(i, 1).toString();
            if (!StringUtils.isEmpty(pattern)) {
                args.addProperty("pattern", pattern);
                ProcessorConfig pc = new ProcessorConfig(
                        "PatternRemover",
                        (Boolean) model.getValueAt(i, 0),
                        true,
                        args);
                ct.loader.getProcessorConfigs().Custom.add(pc);
            }
        }

        try {
            FirmwareUtils.save(ct.file, ct.loader);
        } catch (IOException ex) {
            GUIHelpers.displayErrorDialog("Problem saving controller config: " + ex.getMessage());
        }
    }

    @Override
    synchronized public void restoreDefaults() throws Exception {

    }

    @Override
    public String getHelpMessage() {
        StringBuilder message = new StringBuilder()
        .append("These filters are used to process gcode prior to sending it to the controller.").append("\n")
        .append("Some of them must be enabled, others may be disabled. Most are configured").append("\n")
        .append("by global settings in the sender settings menu.").append("\n")
        .append("Custom filters can be added to remove specific patterns,").append("\n")
        .append("for example \\\"Td+\\\" will remove any instance").append("\n")
        .append("of \\\"T1\\\", \\\"T12\\\", etc.").append("\n");
        return message.toString();
    }

    /**
     *  ------------------------------
     *  |  [      controller      ]  |
     *  | [ ] front processor 1      |
     *  | [ ] front processor 2      |
     *  | [ ] end processor 1        |
     *  | [ ] end processor 2        |
     * 
     *  | [+]                   [-]  |
     *  |  ________________________  |
     *  | | Enabled | Pattern      | |
     *  | |  [y]    | T\d+         | |
     *  | |  [n]    | M30          | |
     *  |  ------------------------  |
     *  |____________________________|
     */
    @Override
    protected void updateComponentsInternal(Settings s) {
        
    	this.removeAll();
    	
        initCustomRemoverTable(customRemoverTable);
        
        setLayout(null);
        setBounds(0,0,340,400);


        ConfigTuple ct = configFiles.get("GRBL");
        ProcessorConfigGroups pcg = ct.loader.getProcessorConfigs();

        int aY = -20;
        for (ProcessorConfig pc : pcg.Front) {
        	ProcessorConfigCheckbox aCheckbox = new ProcessorConfigCheckbox(pc, CommandProcessorLoader.getHelpForConfig(pc));
        	aY = aY + 30;
        	aCheckbox.setBounds(5,aY,400,30);
            add(aCheckbox);
        }

        for (ProcessorConfig pc : pcg.End) {
        	ProcessorConfigCheckbox aCheckbox = new ProcessorConfigCheckbox(pc, CommandProcessorLoader.getHelpForConfig(pc));
        	aY = aY + 30;
        	aCheckbox.setBounds(5,aY,400,30);
            add(aCheckbox);
        }


        add.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
            	addNewPatternRemover();
            }
        });
        
        remove.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
            	removeSelectedPatternRemover();
            }
        });
        
        aY = aY + 40;
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(null);
        buttonPanel.setBounds(5,aY,350,30);
        add.setBounds(0,0,100,30);
        add(buttonPanel, add);
        remove.setBounds(150,0,180,30);
        add(buttonPanel, remove);
        addIgnoreChanges(buttonPanel);

        DefaultTableModel model = (DefaultTableModel) this.customRemoverTable.getModel();
        for (ProcessorConfig pc : pcg.Custom) {
            Boolean enabled = pc.enabled;
            String pattern = "";
            if (pc.args != null && !pc.args.get("pattern").isJsonNull()) {
                pattern = pc.args.get("pattern").getAsString();
            }
            model.addRow(new Object[]{enabled, pattern});
        }
        aY = aY + 35;
        JScrollPane aTable = new JScrollPane(customRemoverTable);
        aTable.setBounds(5,aY,330,80);
        addIgnoreChanges(aTable, "height 100");
        
        SwingUtilities.updateComponentTreeUI(this);
    }

    private JTable initCustomRemoverTable(JTable table) {
        final String[] columnNames = {
            "Enabled",
            "Removes text matching a regular expression pattern"
        };

        final Class[] columnTypes =  {
            Boolean.class,
            String.class
        };

        DefaultTableModel model = new DefaultTableModel(null, columnNames) {
            @Override
            public Class<?> getColumnClass(int idx) {
                return columnTypes[idx];
            }
        };

        table.setModel(model);
        table.getTableHeader().setReorderingAllowed(false);
        table.getModel().addTableModelListener((TableModelEvent e) -> change());

        return table;
    }
}
