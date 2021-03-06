/**
 * http://linuxcnc.org/docs/html/gcode/g-code.html#gcode:g17-g19.1
 */
/*
    Copywrite 2013-2016 Will Winder

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

import static com.geberl.gcodesender.gcode.util.Code.*;

/**
 *
 * @author wwinder
 */
public enum Plane {
    XY(G17),
    ZX(G18),
    YZ(G19),
    UV(G17_1),
    WU(G18_1),
    VW(G19_1);

    public final Code code;
    Plane(Code c) {
        code = c;
    }

    public static Plane lookup(Code c) {
      for (Plane p: values()) {
        if (p.code == c) {
          return p;
        }
      }
      return null;
    }
}
