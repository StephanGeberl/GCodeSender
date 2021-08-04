/*
    Copyright 2016-2017 Will Winder

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
package com.geberl.gcodesender.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Scanner;
import java.util.Vector;

import com.google.common.io.Files;


public class ProjectFile {
	
	public Vector<GcodePart> gCodePartList = new Vector<>(); 
	private File gcodeFileAll;
	private File tempDir = null;
	private int numGcodeFiles = 0;
	
	
    public ProjectFile() {
    }

    public ProjectFile(File aGcodeFile) {
    	gcodeFileAll = aGcodeFile;
    	splitGcodeFile();
    }

    public File getGcodeFileAll() {
    	
    	return gcodeFileAll;
    }
    
    private void splitGcodeFile() {
    	
    	/* 
    	 * Files anhand der M00 Sequenz in einzelne Files auftrennen
    	 */
    	
    	String aFileName;
    	String aToolHint = "";
    	Scanner gcodeAll = null;
    	File aPartFile = null;
    	FileOutputStream fileOutStream = null;
    	
		try {
			
	    	aFileName = this.getTempDir() + File.separator + "partfile_ugs_" + System.currentTimeMillis();
	    	aPartFile = new File(aFileName);
	    	fileOutStream = new FileOutputStream(aPartFile);
			
	    	if (!aPartFile.exists()) { aPartFile.createNewFile(); }
	    	
			gcodeAll = new Scanner(gcodeFileAll);
			
			while(gcodeAll.hasNext()) {
				
			    String nextLine = gcodeAll.nextLine();
			    
			    // abfragen auf M00
			    if (nextLine.length() >= 3 && (nextLine.toUpperCase()).startsWith("M00")) {

				    // abspeichern altes File
			    	fileOutStream.flush();
				    fileOutStream.close();
				    numGcodeFiles = numGcodeFiles + 1;

				    gCodePartList.add(new GcodePart(aPartFile, aToolHint, aFileName));
				    
				    // anlegen neues File
				    aFileName = this.getTempDir() + File.separator + "partfile_ugs_" + System.currentTimeMillis();
			    	aPartFile = new File(aFileName);
			    	fileOutStream = new FileOutputStream(aPartFile);
			    	if (!aPartFile.exists()) { aPartFile.createNewFile(); }
			    	
			    	if (nextLine.length() >= 4) {
			    		aToolHint = nextLine.substring(3, nextLine.length());
			    		aToolHint = aToolHint.trim();
			    	}

			    	
			    } else {

				    // normale Zeile lesen und schreiben
			    	String printLine = System.lineSeparator() + nextLine;
				    byte[] writeBytes = printLine.getBytes();
				    fileOutStream.write(writeBytes);
				    fileOutStream.flush();
			    	
			    }
			    
			}			

	    	// letztes File speichern
			gCodePartList.add(new GcodePart(aPartFile, aToolHint, aFileName));

	    	fileOutStream.close();
		    numGcodeFiles = numGcodeFiles + 1;
	    	gcodeAll.close();
	    	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (fileOutStream != null) {
					fileOutStream.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
    	
    }

    public String getNextToolString(int aAktualGCodeFileIndex) {

    	String aToolString = "";
    	int aIndex = aAktualGCodeFileIndex - 1 + 1;
    	
    	if (aAktualGCodeFileIndex < numGcodeFiles) {
    		aToolString = (gCodePartList.get(aIndex)).toolText;
    	}
    	
    	return aToolString;
    }
    
    
    public int getNumGcodeFiles() {
    	return numGcodeFiles;
    }
    
    
    private File getTempDir() {
        if (tempDir == null) {
            tempDir = Files.createTempDir();
        }
        return tempDir;
    }
    
    
    
    // Simple data class used for the G-Code Parts.
    public class GcodePart {
        public GcodePart(
                File aGcodePart,
                String aToolText,
                String aFileName) {
            this.gcodePart = aGcodePart;
            this.toolText = aToolText;
            this.fileName = aFileName;
        }
        public File gcodePart;
        public String toolText;
        public String fileName;
 
    }
    
    
}
