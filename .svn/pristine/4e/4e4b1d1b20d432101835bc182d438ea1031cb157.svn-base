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

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.*;

public class J2StateButton extends JButton {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean currentValue = false;
	private String stringStateTrue = "ON";
	private String stringStateFalse = "OFF";
	private Color colorStateTrue = Color.GREEN;
	private Color colorStateFalse = Color.RED;
	private boolean isColored = false;
	

    public J2StateButton() {
    	this.isColored = false;
    	this.setButtonValueAndColor();
    }

    public J2StateButton(boolean initialValue) {
    	this.isColored = false;
    	this.currentValue = initialValue;
    	this.setButtonValueAndColor();
    }

    public J2StateButton(boolean initialValue, 
    					 String stringStateTrue, 
    					 String stringStateFalse)
    {
    	this.isColored = false;
    	this.currentValue = initialValue;
    	this.stringStateTrue = stringStateTrue;
    	this.stringStateFalse = stringStateFalse;
    	this.setButtonValueAndColor();
    }

    public J2StateButton(boolean initialValue, 
			 String stringStateTrue, 
			 String stringStateFalse,
			 Color colorStateTrue,
			 Color colorStateFalse)
	{
    	this.isColored = true;
		this.currentValue = initialValue;
		this.stringStateTrue = stringStateTrue;
		this.stringStateFalse = stringStateFalse;
		this.colorStateTrue = colorStateTrue;
		this.colorStateFalse = colorStateFalse;
		this.setButtonValueAndColor();
	}

    
	public boolean getState() {
	   return this.currentValue;
    }

	public void setState(boolean value) {
	   this.currentValue = value;
	   this.setButtonValueAndColor();
	}

	private void setButtonValueAndColor() {
		
		if (this.currentValue == true) {
			if (this.isColored) { super.setBackground(this.colorStateTrue); };
			super.setText(this.stringStateTrue);
		};
		if (this.currentValue == false) {
			if (this.isColored) { super.setBackground(this.colorStateFalse); };
			super.setText(this.stringStateFalse);
		};
		if (this.isColored) { this.setFocusPainted(false); };
	}
	
	@Override 
	public void fireActionPerformed( ActionEvent e ) { 
		this.currentValue = ! this.currentValue;
		this.setButtonValueAndColor();
		super.fireActionPerformed( e );
	}

	@Override 
	public void setEnabled(boolean b) { 
		this.setContentAreaFilled(b);
		super.setEnabled(b);
	}

}
