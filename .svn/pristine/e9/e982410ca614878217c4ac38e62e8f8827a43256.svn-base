/*
    Copyright 2021 Stephan Geberl

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

import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;

import javax.swing.JPanel;

/**
 * A panel with a Background Picture}.
 *
 * @author wwinder
 */
public class JPanelBackground extends JPanel {
	private static final long serialVersionUID = 1L;
	private Image backgroundImage = null;


	public JPanelBackground() { }

	public void setBackgroundImagePath(String iPath) {
		try {
			backgroundImage = ImageIO.read(getClass().getResource(iPath));
		} catch (IOException e) {
			backgroundImage = null;
		}
	}

	@Override
	public void paintComponent(Graphics g)
	{
	    super.paintComponent(g);
	    if (backgroundImage != null)
	    {
  			g.drawImage(backgroundImage, 0, 0, this);
	    }
	}}
