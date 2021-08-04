/**
 * Expand an arc into smaller sections. You can configure the length of each
 * section, and whether it is expanded with a bunch of smaller arcs, or with
 * line segments.
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

import com.geberl.gcodesender.gcode.GcodeParser;
import com.geberl.gcodesender.gcode.GcodePreprocessorUtils;
import com.geberl.gcodesender.gcode.GcodeState;
import com.geberl.gcodesender.gcode.GcodeParser.GcodeMeta;
import com.geberl.gcodesender.gcode.GcodePreprocessorUtils.SplitCommand;
import com.geberl.gcodesender.gcode.util.Code;
import com.geberl.gcodesender.gcode.util.GcodeParserException;
import com.geberl.gcodesender.gcode.util.PlaneFormatter;
import com.geberl.gcodesender.model.Position;
import com.geberl.gcodesender.types.PointSegment;
import com.google.common.collect.Iterables;

import static com.geberl.gcodesender.gcode.util.Code.G1;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author wwinder
 */
public class ArcExpander implements CommandProcessor {
    final private boolean convertToLines;
    final private double length;
    final private DecimalFormat df;

    @Override
    public String getHelp() {
        return "Convert arcs to lines\\: Converts small arc commands (G2/G3) to a series of G1 commands." + "\n"
                + "Small arc segment length\\: The length (in mm) of segments in an expanded arc."
                + ": " + df.format(length);
    }

    /**
     * @param convertToLines toggles if smaller lines or arcs are returned.
     * @param length the length of each smaller segment.
     */
    public ArcExpander(boolean convertToLines, double length) {
        this.convertToLines = convertToLines;
        this.length = length;

        // Setup decimal formatter
        df = new DecimalFormat("#.##");
    }

    @Override
    public List<String> processCommand(String command, GcodeState state) throws GcodeParserException {
        if (state.currentPoint == null) throw new GcodeParserException("There is no starting point, unable to expand arc.");

        List<String> results = new ArrayList<>();

        List<GcodeMeta> commands = GcodeParser.processCommand(command, 0, state);

        // If this is not an arc, there is nothing to do.
        Code c = hasArcCommand(commands);
        if (c == null) {
            return Collections.singletonList(command);
        }

        SplitCommand sc = GcodePreprocessorUtils.extractMotion(c, command);
        if (sc.remainder.length() > 0) {
            results.add(sc.remainder);
        }

        GcodeMeta arcMeta = Iterables.getLast(commands);
        PointSegment ps = arcMeta.point;
        Position start = state.currentPoint;
        Position end = arcMeta.point.point();

        List<Position> points = GcodePreprocessorUtils.generatePointsAlongArcBDring(
                start, end, ps.center(), ps.isClockwise(),
                ps.getRadius(), 0, this.length, new PlaneFormatter(ps.getPlaneState()));

        // That function returns the first and last points. Exclude the first
        // point because the previous gcode command ends there already.
        points.remove(0);

        if (convertToLines) {
            // Tack the speed onto the first line segment in case the arc also
            // changed the feed value.
            String feed = "F" + arcMeta.point.getSpeed();
            for (Position point : points) {
                results.add(GcodePreprocessorUtils.generateLineFromPoints(G1, start, point, state.inAbsoluteMode, df) + feed);
                start = point;
                feed = "";
            }
        } else {
            // TODO: Generate arc segments.
            throw new UnsupportedOperationException("I have not implemented this.");
        }

        return results;
    }

    private static Code hasArcCommand(List<GcodeMeta> commands) {
        if (commands == null) return null;
        for (GcodeMeta meta : commands) {
            if (meta.point != null && meta.point.isArc()) {
                return meta.code;
            }
        }
        return null;
    }
}
