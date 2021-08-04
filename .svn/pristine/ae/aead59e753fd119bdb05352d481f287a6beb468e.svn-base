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

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.geberl.gcodesender.utils.GUIHelpers;

/**
 * Simple dialog for configuring Sender settings.
 *
 * @author wwinder
 */
public class DoubleValueButtonDialog extends JDialog {

    private boolean saveChanges;
    private double startValue = 0.0;
    private double newValue = 0.0;
    private double maxValue = 1000.0;
    private double minValue = 0.0;


	private JLabel actualValueLabel = new JLabel("0.00", JLabel.RIGHT);
	private JLabel newValueLabel = new JLabel("0.00", JLabel.RIGHT);

    private final JButton restoreButton = new JButton("Restore");
    private final JButton closeWithSaveButton = new JButton("Save");
    private final JButton closeWithoutSaveButton = new JButton("Cancel");
    private final JButton helpButton = new JButton("Help");
    
    private final JButton plus01Button = new JButton("+0.1");
    private final JButton plus1Button = new JButton("+1");
    private final JButton plus10Button = new JButton("+10");
    private final JButton plus50Button = new JButton("+50");
    private final JButton plus100Button = new JButton("+100");
    private final JButton plus200Button = new JButton("+200");
    
    private final JButton minus01Button = new JButton("-0.1");
    private final JButton minus1Button = new JButton("-1");
    private final JButton minus10Button = new JButton("-10");
    private final JButton minus50Button = new JButton("-50");
    private final JButton minus100Button = new JButton("-100");
    private final JButton minus200Button = new JButton("-200");
     
    private final JButton set01Button = new JButton("0.1");
    private final JButton set1Button = new JButton("1");
    private final JButton set10Button = new JButton("10");
    private final JButton set50Button = new JButton("50");
    private final JButton set100Button = new JButton("100");
    private final JButton set200Button = new JButton("200");

	/**
     * Creates new form GrblSettingsDialog
     */
    public DoubleValueButtonDialog(String settingsTitle, double aCurrentValue, Frame parent, double aMaxValue, double aMinValue) {
    	
        super(parent, true);
        this.setTitle(settingsTitle);
        startValue = aCurrentValue;
        newValue = aCurrentValue;
        maxValue = aMaxValue;
        minValue = aMinValue;

        this.restoreButton.addActionListener(event -> restoreDefaultSettings());
        this.closeWithSaveButton.addActionListener(event -> closeWithSaveActionPerformed());
        this.closeWithoutSaveButton.addActionListener(event -> closeWithoutSaveActionPerformed());
        
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

    	JLabel textDescLabel = new JLabel("Wert alt/neu");
    	textDescLabel.setFont(new Font("DejaVu Sans", Font.PLAIN, 16));
    	textDescLabel.setForeground(Color.BLACK);
    	textDescLabel.setBounds(10, 5, 100, 35);
        add(textDescLabel);

    	JLabel textBorderLabel = new JLabel(this.getBorderValueString());
    	textBorderLabel.setFont(new Font("DejaVu Sans", Font.PLAIN, 12));
    	textBorderLabel.setForeground(Color.BLACK);
    	textBorderLabel.setBounds(10, 280, 320, 20);
        add(textBorderLabel);

    	
    	actualValueLabel.setFont(new Font("DejaVu Sans", Font.PLAIN, 16));
    	actualValueLabel.setForeground(Color.BLACK);
    	actualValueLabel.setBounds(120, 5, 100, 35);
    	actualValueLabel.setText(this.getActualValueString());
        add(actualValueLabel);

    	newValueLabel.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
    	newValueLabel.setForeground(Color.RED);
    	newValueLabel.setBounds(230, 5, 100, 35);
    	newValueLabel.setText(this.getActualValueString());
        add(newValueLabel);
    	
    	// ---------------------------
        
        plus01Button.setBounds(10, 45, 100, 35);   	
    	plus01Button.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
    	plus01Button.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		changeActualValue(0.10, false);
        	}
        });
        add(plus01Button);
        
        plus1Button.setBounds(10, 85, 100, 35);   	
    	plus1Button.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
    	plus1Button.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		changeActualValue(1.00, false);
        	}
        });
        add(plus1Button);
    	
    	plus10Button.setBounds(10, 125, 100, 35);   	
    	plus10Button.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
    	plus10Button.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		changeActualValue(10.00, false);
        	}
        });
        add(plus10Button);
     	
    	plus50Button.setBounds(10, 165, 100, 35);   	
    	plus50Button.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
    	plus50Button.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		changeActualValue(50.00, false);
        	}
        });
        add(plus50Button);
    	
    	plus100Button.setBounds(10, 205, 100, 35);   	
    	plus100Button.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
    	plus100Button.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		changeActualValue(100.00, false);
        	}
        });
        add(plus100Button);
    	
    	plus200Button.setBounds(10, 245, 100, 35);   	
    	plus200Button.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
    	plus200Button.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		changeActualValue(200.00, false);
        	}
        });
        add(plus200Button);
    	
        // ------------------------
        
    	minus01Button.setBounds(120, 45, 100, 35);   	
    	minus01Button.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
    	minus01Button.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		changeActualValue(-0.10, false);
        	}
        });
        add(minus01Button);

        minus1Button.setBounds(120, 85, 100, 35);   	
    	minus1Button.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
    	minus1Button.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		changeActualValue(-1.00, false);
        	}
        });
        add(minus1Button);
    	
        minus10Button.setBounds(120, 125, 100, 35);   	
        minus10Button.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
        minus10Button.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		changeActualValue(-10.00, false);
        	}
        });
        add(minus10Button);
     	
        minus50Button.setBounds(120, 165, 100, 35);   	
        minus50Button.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
        minus50Button.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		changeActualValue(-50.00, false);
        	}
        });
        add(minus50Button);
    	
        minus100Button.setBounds(120, 205, 100, 35);   	
        minus100Button.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
        minus100Button.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		changeActualValue(-100.00, false);
        	}
        });
        add(minus100Button);
    	
        minus200Button.setBounds(120, 245, 100, 35);   	
        minus200Button.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
        minus200Button.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		changeActualValue(-200.00, false);
        	}
        });
        add(minus200Button);
   	
        // ------------------------

    	set01Button.setBounds(230, 45, 100, 35);   	
    	set01Button.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
    	set01Button.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		changeActualValue(0.10, true);
        	}
        });
        add(set01Button);
        
    	set1Button.setBounds(230, 85, 100, 35);   	
    	set1Button.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
    	set1Button.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		changeActualValue(1.00, true);
        	}
        });
        add(set1Button);
    	
        set10Button.setBounds(230, 125, 100, 35);   	
        set10Button.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
        set10Button.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		changeActualValue(10.00, true);
        	}
        });
        add(set10Button);
     	
        set50Button.setBounds(230, 165, 100, 35);   	
        set50Button.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
        set50Button.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		changeActualValue(50.00, true);
        	}
        });
        add(set50Button);
    	
        set100Button.setBounds(230, 205, 100, 35);   	
        set100Button.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
        set100Button.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		changeActualValue(100.00, true);
        	}
        });
        add(set100Button);
    	
        set200Button.setBounds(230, 245, 100, 35);   	
        set200Button.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
        set200Button.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		changeActualValue(200.00, true);
        	}
        });
        add(set200Button);

        // ------------------------
        
        restoreButton.setBounds(10, 310, 100, 40);
        restoreButton.setFont(new Font("DejaVu Sans", Font.BOLD, 14));
        add(restoreButton);
        closeWithoutSaveButton.setBounds(120, 310, 100, 40);
        closeWithoutSaveButton.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
        add(closeWithoutSaveButton);
        closeWithSaveButton.setBounds(230, 310, 100, 40);
        closeWithSaveButton.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
        add(closeWithSaveButton);
        
        pack();
    }

    private void closeWithoutSaveActionPerformed() {
        this.saveChanges = false;
        setVisible(false);
    }

    private void closeWithSaveActionPerformed() {
    	//        this.settingsPanel.save();
        this.saveChanges = true;
        setVisible(false);
    }

    private void restoreDefaultSettings() {
    	this.resetActualValue();
    }

    private void changeActualValue(double aValue, boolean aAbsolute) {
    	
    	if ( aAbsolute ) {
    		if (aValue <= maxValue && aValue >= minValue) { newValue = aValue; };
    	}
    	else {
       		if (newValue + aValue <= maxValue && newValue + aValue >= minValue) { newValue = newValue + aValue; };
    	}
    	
    	this.newValueLabel.setText(this.getNewValueString());
    }

    private void resetActualValue() {
     	newValue = startValue;
    	this.newValueLabel.setText(this.getNewValueString());
    }

    private String getActualValueString() {
    	
		BigDecimal bd = new BigDecimal(Double.toString(startValue)).setScale(2, RoundingMode.HALF_EVEN);
		String aValueText = bd.toString();

		return aValueText;
    }

    private String getNewValueString() {
    	
		BigDecimal bd = new BigDecimal(Double.toString(newValue)).setScale(2, RoundingMode.HALF_EVEN);
		String aValueText = bd.toString();

		return aValueText;
    }

    private String getBorderValueString() {
    	
		BigDecimal minVal = new BigDecimal(Double.toString(minValue)).setScale(2, RoundingMode.HALF_EVEN);
		BigDecimal maxVal = new BigDecimal(Double.toString(maxValue)).setScale(2, RoundingMode.HALF_EVEN);
		
		String aValueText = "Value must be between " + minVal.toString() + " and " + maxVal.toString() + ".";

		return aValueText;
    }

    public boolean getChangeValue() {
		return saveChanges;
    }

    public double getNewValue() {
		return newValue;
    }

    
}
