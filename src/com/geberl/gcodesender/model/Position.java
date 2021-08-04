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

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.geberl.gcodesender.model.UnitUtils.Units;

import java.util.Objects;

public class Position {

    public static final Position ZERO = new Position(0, 0, 0, 0, 0, Units.MM);

    public double x;
    public double y;
    public double z;
    public double a;
    public double b;
    private final Units units;

    public Position() {
    	this.x = 0.0;
    	this.y = 0.0;
    	this.z = 0.0;
    	this.a = 0.0;
    	this.b = 0.0;
        this.units = Units.UNKNOWN;

    }

    public Position(Position other) {
        this(other.x, other.y, other.z, other.a, other.b, other.units);
    }

    public Position(double x, double y, double z, double a, double b, Units units) {
    	this.x = x;
    	this.y = y;
    	this.z = z;
    	this.a = a;
    	this.b = b;
        this.units = units;
    }

    
     public boolean equals(final Object other) {
        if (other instanceof Position) {
            return equals((Position) other);
        }
        return false;
    }

    public boolean equals(final Position o) {
        try {
            return(this.x == o.x && this.y == o.y && this.z == o.z && this.a == o.a && this.b == o.b && units == o.units);
          	}
          	catch (NullPointerException e2) {return false;}
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    /**
     * Check that the positions are the same ignoring units.
     */
    public boolean isSamePositionIgnoreUnits(final Position o) {
        if (units != o.getUnits()) {
            return equals(o.getPositionIn(units));
        }
        return equals(o);
    }

    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + Objects.hashCode(this.units);
        return hash;
    }

    public Units getUnits() {
        return units;
    }

    public Position getPositionIn(Units units) {
        double scale = UnitUtils.scaleUnits(this.units, units);
        return new Position(x*scale, y*scale, z*scale, a*scale, b*scale, units);
    }

    public double get(Axis axis) {
        switch (axis) {
            case X:
                return getX();
            case Y:
                return getY();
            case Z:
                return getZ();
            case A:
                return getA();
            case B:
                return getB();
            default:
                return 0;
        }
    }
    

    /**
	 * Get the <i>x</i> coordinate.
	 */
	public final double getX() {
		return x;
	}

	
	/**
	 * Set the <i>x</i> coordinate.
	 */
	public final void setX(double x) {
		this.x = x;
	}


	/**
	 * Get the <i>y</i> coordinate.
	 */
	public final double getY() {
		return y;
	}


	/**
	 * Set the <i>y</i> coordinate.
	 */
	public final void setY(double y) {
		this.y = y;
	}

	/**
	 * Get the <i>z</i> coordinate.
	 */
	public final double getZ() {
		return z;
	}


	/**
	 * Set the <i>z</i> coordinate.
	 */
	public final void setZ(double z) {
		this.z = z;
	}
	
	/**
	 * Get the <i>a</i> coordinate.
	 */
	public final double getA() {
		return a;
	}


	/**
	 * Set the <i>a</i> coordinate.
	 */
	public final void setA(double a) {
		this.a = a;
	}	   
    
	/**
	 * Get the <i>b</i> coordinate.
	 */
	public final double getB() {
		return b;
	}


	/**
	 * Set the <i>b</i> coordinate.
	 */
	public final void setB(double b) {
		this.b = b;
	}	   
    
    
    
    
    
    
}
