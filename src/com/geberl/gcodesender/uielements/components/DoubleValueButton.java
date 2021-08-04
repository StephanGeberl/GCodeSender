/*
    Copyright 2021 Stephan Geberl

    This file is part of WireCutter/Mill GCode - Sender (WGS) (5 Axis-Version).
    WGS is derived from UGS by Will Winder (2012 - 2018)

    WGS is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    UGS is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with WGS.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.geberl.gcodesender.uielements.components;

import static com.geberl.gcodesender.utils.GUIHelpers.displayErrorDialog;
import com.geberl.gcodesender.uielements.dialog.DoubleValueButtonDialog;

import java.awt.event.ActionEvent;
import java.math.RoundingMode;
import java.math.BigDecimal;

import javax.swing.*;

public class DoubleValueButton extends JButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    public static final double MIN_VALUE = 0.001;
    public static final double MAX_VALUE = 100000.0;

	private double minValue;
	private double maxValue;
    private double currentValue = 0.0;
	private String valueLabel = "";
	

    public DoubleValueButton() {
    	this.setButtonValueAndColor();
    }

    public DoubleValueButton(String aValueLabel, double initialValue, double aMaxValue, double aMinValue) {
    	this.valueLabel = aValueLabel;
    	this.currentValue = initialValue;
    	this.minValue = aMinValue;
    	this.maxValue = aMaxValue;
    	
    	this.setButtonValueAndColor();
    }


	public void setValue(double aValue) {
		currentValue = aValue;
    	this.setButtonValueAndColor();
	}

    
	public double getValue() {
	   return this.currentValue;
    }

	private void setButtonValueAndColor() {
		
		BigDecimal bd = new BigDecimal(Double.toString(currentValue)).setScale(2, RoundingMode.HALF_EVEN);
		String aValueText = bd.toString();
		this.setText(valueLabel + ": " + aValueText);

	}
	
	@Override 
	public void fireActionPerformed( ActionEvent e ) { 

		JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);

        try {
            int dialogHeight = 390;
            int dialogWidth = 340;
        	DoubleValueButtonDialog changeValueDialog = new DoubleValueButtonDialog("Set Value", currentValue, topFrame, maxValue, minValue);
        		changeValueDialog.setBounds(topFrame.getX() + 20, topFrame.getY() + 20, dialogWidth, dialogHeight);
        		changeValueDialog.setVisible(true);
        		
        	if (changeValueDialog.getChangeValue()) { this.setValue(changeValueDialog.getNewValue()); };
        	
        } catch (Exception ex) {
            displayErrorDialog(ex.getMessage());
        }

        super.fireActionPerformed( e );
	}

	@Override 
	public void setEnabled(boolean b) { 
		this.setContentAreaFilled(b);
		super.setEnabled(b);
	}

}
