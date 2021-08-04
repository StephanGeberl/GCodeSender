/**
 * Overrides any F commands with the given percentage.
 */
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
package com.geberl.gcodesender.gcode.processors;

import java.util.ArrayList;
import java.util.List;

import com.geberl.gcodesender.gcode.GcodePreprocessorUtils;
import com.geberl.gcodesender.gcode.GcodeState;

/**
 *
 * @author wwinder
 */
public class FeedOverrideProcessor implements CommandProcessor {
    private final double percentOverride;

    public FeedOverrideProcessor(double percentOverride) {
        this.percentOverride = percentOverride;
    }

    @Override
    public String getHelp() {
        return "Enable speed override\\: Enables or disables speed overriding." + "\n"
                + "Speed override percent\\: Factor that speeds will be scaled by." + "\n"
                + "Speed override percent: " + percentOverride;
    }

    @Override
    public List<String> processCommand(String command, GcodeState state) {
        List<String> ret = new ArrayList<>();
        if (percentOverride > 0) {
            ret.add(GcodePreprocessorUtils.overrideSpeed(command, percentOverride));
        } else {
            ret.add(command);
        }
        return ret;
    }
}