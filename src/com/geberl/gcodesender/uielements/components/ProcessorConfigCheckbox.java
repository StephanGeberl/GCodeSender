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
package com.geberl.gcodesender.uielements.components;

import com.geberl.gcodesender.utils.GUIHelpers;
import com.geberl.gcodesender.utils.ControllerSettings.ProcessorConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author wwinder
 */
public class ProcessorConfigCheckbox extends JPanel {
    final public JCheckBox box;
    final private ProcessorConfig pc;

    private final static Gson GSON;
    private final static JsonParser PARSER = new JsonParser();

    static {
        GSON = new GsonBuilder().setPrettyPrinting().create();
    }

    public ProcessorConfigCheckbox(ProcessorConfig pc, String helpMessage) {
        this.pc = pc;

        setLayout(null);
        setBounds(0,0,370,30);
        
        if (helpMessage == null)
            throw new RuntimeException("Help message was not provided.");

        JButton help = new JButton("?");
        help.addActionListener((ActionEvent e) -> {
            GUIHelpers.displayHelpDialog(helpMessage);
        });
        help.setBounds(0,0,45,28);
        add(help);

        box = new JCheckBox(pc.name);
        box.setSelected(pc.enabled);
        box.addActionListener((ActionEvent e) -> {
                pc.enabled = box.isSelected();
            });
        if (!pc.optional) {
            box.setEnabled(false);
        }
        JButton edit = new JButton("edit");
        edit.addActionListener(evt -> editArgs());

        box.setBounds(55,0,220,28);
        add(box);

        if (pc.args != null) {
        	edit.setBounds(280,0,80,28);
            add(edit);
        }
    }

    public void editArgs() {
        JTextArea ta = new JTextArea(20, 20);
        ta.setText(GSON.toJson(pc.args));
        switch (JOptionPane.showConfirmDialog(null, new JScrollPane(ta))) {
            case JOptionPane.OK_OPTION:
                pc.args = PARSER.parse(ta.getText()).getAsJsonObject();
                System.out.println(ta.getText());
                break;
        }

    }

    public void setSelected(Boolean s) {box.setSelected(s); }
    public boolean getValue() { return box.isSelected(); }
}