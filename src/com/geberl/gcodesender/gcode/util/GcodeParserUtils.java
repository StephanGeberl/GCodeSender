/*
    Copyright 2017 Will Winder

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
package com.geberl.gcodesender.gcode.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;

import com.geberl.gcodesender.gcode.GcodeParser;
import com.geberl.gcodesender.gcode.GcodePreprocessorUtils;
import com.geberl.gcodesender.types.GcodeCommand;
import com.geberl.gcodesender.utils.GcodeStreamReader;
import com.geberl.gcodesender.utils.GcodeStreamWriter;
import com.geberl.gcodesender.utils.IGcodeStreamReader;

/**
 *
 * @author wwinder
 */
public class GcodeParserUtils {
    private static final Logger logger = Logger.getLogger(GcodeParserUtils.class.getName());

    /**
     * Helper method to apply processors to gcode.
     */
    public static void processAndExport(GcodeParser gcp, File input, File output, Boolean isMillMode)
            throws IOException, GcodeParserException {
        try(BufferedReader br = new BufferedReader(new FileReader(input))) {
            if (processAndExportGcodeStream(gcp, br, output)) {
                return;
            }
        }

        try(BufferedReader br = new BufferedReader(new FileReader(input))) {
            processAndExportText(gcp, br, output, isMillMode);
        }
    }

    /**
     * Common logic in processAndExport* methods.
     */
    private static void write(GcodeParser gcp, GcodeStreamWriter gsw, String original, String command, String comment, int idx) throws GcodeParserException {
        if (idx % 100000 == 0) {
            logger.log(Level.FINE, "gcode processing line: " + idx);
        }

        if (StringUtils.isEmpty(command)) {
            gsw.addLine(original, command, comment, idx);
        }
        else {
            // Parse the gcode for the buffer.
            Collection<String> lines = gcp.preprocessCommand(command, gcp.getCurrentState());

            for(String processedLine : lines) {
                gsw.addLine(original, processedLine, comment, idx);
            }

            gcp.addCommand(command);
        }
    }

    /**
     * Attempts to read the input file in GcodeStream format.
     * @return whether or not we succeed processing the file.
     */
    private static boolean processAndExportGcodeStream(GcodeParser gcp, BufferedReader input, File output)
            throws IOException, GcodeParserException {

        // Preprocess a GcodeStream file.
        try {
            IGcodeStreamReader gsr = new GcodeStreamReader(input);
            try (GcodeStreamWriter gsw = new GcodeStreamWriter(output)) {
                int i = 0;
                while (gsr.getNumRowsRemaining() > 0) {
                    i++;
                    GcodeCommand gc = gsr.getNextCommand();
                    write(gcp, gsw, gc.getOriginalCommandString(), gc.getCommandString(), gc.getComment(), i);
                }

                // Done processing GcodeStream file.
                return true;
            }
        } catch (GcodeStreamReader.NotGcodeStreamFile ex) {
            // File exists, but isn't a stream reader. So go ahead and try parsing it as a raw gcode file.
        }
        return false;
    }

    /**
     * Attempts to read the input file in gcode-text format.
     * @return whether or not we succeed processing the file.
     */
    private static void processAndExportText(GcodeParser gcp, BufferedReader input, File output, Boolean isMillMode)
            throws IOException, GcodeParserException {
        // Preprocess a regular gcode file.
        try(BufferedReader br = input) {
            try (GcodeStreamWriter gsw = new GcodeStreamWriter(output)) {
                int i = 0;
                for(String line; (line = br.readLine()) != null; ) {
                    i++;

                    String comment = GcodePreprocessorUtils.parseComment(line);
                    String commentRemoved = GcodePreprocessorUtils.removeComment(line);

                    if (isMillMode) { commentRemoved = transformToFiveAxis(commentRemoved); };
                    
                    write(gcp, gsw, line, commentRemoved, comment, i);
                }
            }
        }
    }
    
    
    private static String transformToFiveAxis(String line) {


        String tempCommand = line.toUpperCase();
        int xPos = 0; int yPos = 0; int zPos = 0; int fPos = 0; int sPos = 0; int mPos = 0; int gPos = 0;
        boolean doCancel = false;
        int minIndex = 0;
        String outCommand = "";

    	// Kommentare
        if (line.contains("(") && line.contains(")")) { return line; }
        
        // Wenn der Command X, Y, Z, F, S, M, G enthaelt und kein A oder B
    	// - aus enthaltenem Z wird Y und A
    	// - aus enthaltenem Y wird X und Z
    	// - aus enthaltenem X wird B
        // - aus enthaltenem F wird F
        // - aus enthaltenem S wird S
        // - aus enthaltenem M wird M
    	
        if ((  tempCommand.contains("X")
        	|| tempCommand.contains("Y")
        	|| tempCommand.contains("Z")
        	|| tempCommand.contains("F")
        	|| tempCommand.contains("S")
        	|| tempCommand.contains("M")
        	|| tempCommand.contains("G"))
        	&& !(   tempCommand.contains("A")
        		 || tempCommand.contains("B")
        		)) {
        	
        	xPos = tempCommand.indexOf("X", 0);
        	yPos = tempCommand.indexOf("Y", 0);
        	zPos = tempCommand.indexOf("Z", 0);
        	fPos = tempCommand.indexOf("F", 0);
        	sPos = tempCommand.indexOf("S", 0);
        	mPos = tempCommand.indexOf("M", 0);
        	gPos = tempCommand.indexOf("G", 0);
       	
        	if (gPos >= 0) {
        		String tempValue = tempCommand.substring(gPos+1);
        		String[] parts = tempValue.split("X|Y|Z|F|S|M|G");
        		int howLong = parts[0].length();
        		if ( howLong < 1 ) { doCancel = true; }
        		if (minIndex < 1 || gPos < minIndex) {minIndex = gPos; }
        		outCommand = outCommand + "G" + (parts[0]).trim() + " ";
        	};

        	if (mPos >= 0) {
        		String tempValue = tempCommand.substring(mPos+1);
        		String[] parts = tempValue.split("X|Y|Z|F|S|M|G");
        		int howLong = parts[0].length();
        		if ( howLong < 1 ) { doCancel = true; }
        		if (minIndex < 1 || mPos < minIndex) {minIndex = mPos; }
        		outCommand = outCommand + "M" + (parts[0]).trim() + " ";
        	};

        	if (sPos >= 0) {
        		String tempValue = tempCommand.substring(sPos+1);
        		String[] parts = tempValue.split("X|Y|Z|F|S|M|G");
        		int howLong = parts[0].length();
        		if ( howLong < 1 ) { doCancel = true; }
        		if (minIndex < 1 || sPos < minIndex) {minIndex = sPos; }
        		// Drehzahl wird auf 12000 normiert (EIN/AUS)
        		String speed = "12000";
         		outCommand = outCommand + "S" + speed + " ";
        		// outCommand = outCommand + "S" + (parts[0]).trim() + " ";
        	};

        	if (fPos >= 0) {
        		String tempValue = tempCommand.substring(fPos+1);
        		String[] parts = tempValue.split("X|Y|Z|F|S|M|G");
        		int howLong = parts[0].length();
        		if ( howLong < 1 ) { doCancel = true; }
        		if (minIndex < 1 || fPos < minIndex) {minIndex = fPos; }
        		outCommand = outCommand + "F" + (parts[0]).trim() + " ";
        	};

        	if (xPos >= 0) {
        		String tempValue = tempCommand.substring(xPos+1);
        		String[] parts = tempValue.split("X|Y|Z|F|S|M|G");
        		int howLong = parts[0].length();
        		if ( howLong < 1 ) { doCancel = true; }
        		if (minIndex < 1 || xPos < minIndex) {minIndex = xPos; }
        		outCommand = outCommand + "B" + (parts[0]).trim() + " ";
        	};
        	
        	if (yPos >= 0) {
        		String tempValue = tempCommand.substring(yPos+1);
        		String[] parts = tempValue.split("X|Y|Z|F|S|M|G");
        		int howLong = parts[0].length();
        		if ( howLong < 1 ) { doCancel = true; }
        		if (minIndex < 1 || yPos < minIndex) {minIndex = yPos; }
        		outCommand = outCommand + "X" + (parts[0]).trim() + " Z" + (parts[0]).trim() + " ";
        	};
        	
        	if (zPos >= 0) {
        		String tempValue = tempCommand.substring(zPos+1);
        		String[] parts = tempValue.split("X|Y|Z|F|S|M|G");
        		int howLong = parts[0].length();
        		if ( howLong < 1 ) { doCancel = true; }
        		if (minIndex < 1 || zPos < minIndex) {minIndex = zPos; }
           		outCommand = outCommand + "Y" + (parts[0]).trim() + " A" + (parts[0]).trim() + " ";
           	};
           	
           	if (!doCancel) {
           		// write the Prefix (no Change)
           		// outCommand = tempCommand.substring(0, minIndex) + outCommand;
           		return outCommand.trim();
            }
           	else {
           		return line;
           	}
        }
        else {
        	return line;
        }
    	
    }
    
}
