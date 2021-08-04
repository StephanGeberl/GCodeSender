/**
 * Removes a specified regex pattern from the command.
 */
/*
    Copyright 2016 Will Winder

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
package com.geberl.gcodesender.gcode.processors;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.geberl.gcodesender.gcode.GcodeState;

/**
 *
 * @author wwinder
 */
public class PatternRemover implements CommandProcessor {
    final private Pattern p;

    public PatternRemover(String regexPattern) {
        p = Pattern.compile(regexPattern);
    }
    
    @Override
    public String getHelp() {
        return "Removes text matching a regular expression pattern: \"" + p.pattern() + "\"";
    }

    @Override
    public List<String> processCommand(String command, GcodeState state) {
        List<String> ret = new ArrayList<>();
        ret.add(p.matcher(command).replaceAll(""));
        return ret;
    }
}
