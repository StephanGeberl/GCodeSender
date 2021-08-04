/*
    Copyright 2016-2018 Will Winder

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
package com.geberl.gcodesender.services;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.geberl.gcodesender.model.BackendAPI;
import com.geberl.gcodesender.model.PartialPosition;
import com.geberl.gcodesender.model.UnitUtils.Units;
import com.geberl.gcodesender.utils.Settings;

/**
 *
 * @author wwinder
 */
public class JogService {
    private static final Logger logger = Logger.getLogger(JogService.class.getSimpleName());

    private final BackendAPI backend;

    public JogService(BackendAPI backend) {
        this.backend = backend;
    }

    public static double increaseSize(double size) {
        if (size >= 1) {
            return size + 1;
        } else if (size >= 0.1) {
            return size + 0.1;
        } else if (size >= 0.01) {
            return size + 0.01;
        } else {
            return 0.01;
        }
    }

    public static double decreaseSize(double size) {
        if (size > 1) {
            return size - 1;
        } else if (size > 0.1) {
            return size - 0.1;
        } else if (size > 0.01) {
            return size - 0.01;
        }
        return size;
    }

    private static double divideSize(double size) {
        if (size > 100) {
            return 100;
        } else if (size <= 100 && size > 10) {
            return 10;
        } else if (size <= 10 && size > 1) {
            return 1;
        } else if (size <= 1 && size > 0.1) {
            return 0.1;
        } else if (size <= 0.1 ) {
            return 0.01;
        }
        return size;
    }

    private static double multiplySize(double size) {
        if (size < 0.01) {
            return 0.01;
        } else if (size >= 0.01 && size < 0.1) {
            return 0.1;
        }  else if (size >= 0.1 && size < 1) {
            return 1;
        }  else if (size >= 1 && size < 10) {
            return 10;
        }  else if (size >= 10) {
            return 100;
        }
        return size;
    }

    public void increaseStepSize() {
        setStepSize(increaseSize(getStepSize()));
    }

    public void decreaseStepSize() {
        setStepSize(decreaseSize(getStepSize()));
    }
    
    public void divideStepSize() {
        setStepSize(divideSize(getStepSize()));
    }

    public void multiplyStepSize() {
        setStepSize(multiplySize(getStepSize()));
    }
    
    public void multiplyFeedRate() {
        setFeedRate(multiplySize(getFeedRate()));
    }

    public void divideFeedRate() {
        setFeedRate(divideSize(getFeedRate()));
    }

    public void increaseFeedRate() {
        setFeedRate(increaseSize(getFeedRate()));
    }

    public void decreaseFeedRate() {
        setFeedRate(decreaseSize(getFeedRate()));
    }
    
    public void setStepSize(double size) {
        getSettings().setManualModeStepSize(size);
    }
    
    private Settings getSettings() {
        return backend.getSettings();
    }

    public void setFeedRate(double rate) {
        if( rate < 1 ) {
            getSettings().setJogFeedRate(1);
        } else {
            getSettings().setJogFeedRate(rate);
        }
    }

    public int getFeedRate() {
        return Double.valueOf(getSettings().getJogFeedRate()).intValue();
    }

    public void setUnits(Units units) {
        if (units != null) {
            getSettings().setPreferredUnits(units);
        }
    }
    
    public Units getUnits() {
        return getSettings().getPreferredUnits();
    }

    /**
     * Adjusts the axis location.
     * @param x direction.
     * @param y direction.
     * @param z direction.
     * @param a direction.
     */
    public void adjustManualLocation(int x, int y, int z, int a) {
        try {
            double feedRate = getFeedRate();
            double stepSize = getStepSize();
            Units preferredUnits = getUnits();
            backend.adjustManualLocation(x, y, z, a, stepSize, feedRate, preferredUnits);
        } catch (Exception e) { }
    }

    /**
     * Adjusts the axis location.
     * @param x direction.
     * @param y direction.
     * @param z direction.
     * @param a direction.
     * @param b direction.
    */
    public void adjustManualLocation(int x, int y, int z, int a, int b) {
        try {
            double feedRate = getFeedRate();
            double stepSize = getStepSize();
            Units preferredUnits = getUnits();
            backend.adjustManualLocation(x, y, z, a, b, stepSize, feedRate, preferredUnits);
        } catch (Exception e) { }
    }

    
    public boolean canJog() {
        return backend.isConnected() &&
                !backend.isSendingFile() &&
                backend.getController().getCapabilities().hasJogging();
    }

    public double getStepSize() {
        return getSettings().getManualModeStepSize();
    }

    public void cancelJog() {
        try {
            backend.getController().cancelSend();
        } catch (Exception e) {
            logger.log(Level.WARNING, "Couldn't cancel the jog", e);
        }
    }

    public void jogTo(PartialPosition position) {
        try {
            backend.getController().jogMachineTo(position, getFeedRate());
        } catch (Exception e) {
            logger.log(Level.WARNING, "Couldn't jog to position " + position, e);
        }
    }
}