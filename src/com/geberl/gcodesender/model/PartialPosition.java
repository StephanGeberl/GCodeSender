package com.geberl.gcodesender.model;


import com.geberl.gcodesender.Utils;
import com.google.common.collect.ImmutableMap;

import java.text.NumberFormat;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a maybe partial coordinate (ie. only certain axis-values are known, eg. for moves where only some axis
 * are changed)
 */
public class PartialPosition {
    private final Double x;
    private final Double y;
    private final Double z;
    private final Double a;
    private final Double b;
    private final UnitUtils.Units units;

    public PartialPosition(Double x, Double y) {
        this.x = x;
        this.y = y;
        this.z = null;
        this.a = null;
        this.b = null;
        this.units = UnitUtils.Units.UNKNOWN;
    }

    public PartialPosition(Double x, Double y, UnitUtils.Units units) {
        this.x = x;
        this.y = y;
        this.z = null;
        this.a = null;
        this.b = null;
        this.units = units;
    }

    public PartialPosition(Double x, Double y, Double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.a = null;
        this.b = null;
        this.units = UnitUtils.Units.UNKNOWN;
    }

    public PartialPosition(Double x, Double y, Double z, UnitUtils.Units units) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.a = null;
        this.b = null;
       this.units = units;
    }

    public PartialPosition(Double x, Double y, Double z, Double a) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.a = a;
        this.b = null;
        this.units = UnitUtils.Units.UNKNOWN;
    }

    public PartialPosition(Double x, Double y, Double z, Double a, UnitUtils.Units units) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.a = a;
        this.b = null;
        this.units = units;
    }

    public PartialPosition(Double x, Double y, Double z, Double a, Double b) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.a = a;
        this.b = b;
        this.units = UnitUtils.Units.UNKNOWN;
    }

    public PartialPosition(Double x, Double y, Double z, Double a, Double b, UnitUtils.Units units) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.a = a;
        this.b = b;
        this.units = units;
    }
    
    
    
    public PartialPosition() {
        this.x = null;
        this.y = null;
        this.z = null;
        this.a = null;
        this.b = null;
       this.units = UnitUtils.Units.UNKNOWN;
    }


    // a shortcut to builder (needed, because of final coords)
    public static PartialPosition from(Axis axis, Double value) {
        return new Builder().setValue(axis, value).build();
    }

    public static PartialPosition from(Position position) {
        return new PartialPosition(position.getX(), position.getY(), position.getZ(), position.getA(), position.getB(), position.getUnits());
    }

    public static PartialPosition fromXY(Position position) {
        return new PartialPosition(position.getX(), position.getY(), position.getUnits());
    }


    public boolean hasX() {
        return x != null;
    }

    public boolean hasY() {
        return y != null;
    }

    public boolean hasZ() {
        return z != null;
    }

    public boolean hasA() {
        return a != null;
    }

    public boolean hasB() {
        return b != null;
    }

    
    public Double getX() {
        if (x == null) {
            throw new IllegalArgumentException("Tried to get x-axis which is not set");
        }
        return x;
    }

    public Double getY() {
        if (y == null) {
            throw new IllegalArgumentException("Tried to get y-axis which is not set");
        }
        return y;
    }

    public Double getZ() {
        if (z == null) {
            throw new IllegalArgumentException("Tried to get y-axis which is not set");
        }
        return z;
    }

    public Double getA() {
        if (a == null) {
            throw new IllegalArgumentException("Tried to get a-axis which is not set");
        }
        return a;
    }

    public Double getB() {
        if (b == null) {
            throw new IllegalArgumentException("Tried to get b-axis which is not set");
        }
        return b;
    }

    public UnitUtils.Units getUnits(){
        return units;
    }

    public Map<Axis, Double> getAll() {
        ImmutableMap.Builder<Axis, Double> allSetCoords = ImmutableMap.builder();
        if (hasX()) {
            allSetCoords.put(Axis.X, x);
        }
        if (hasY()) {
            allSetCoords.put(Axis.Y, y);
        }
        if (hasZ()) {
            allSetCoords.put(Axis.Z, z);
        }
        if (hasA()) {
            allSetCoords.put(Axis.A, a);
        }
        if (hasB()) {
            allSetCoords.put(Axis.B, b);
        }
        return allSetCoords.build();
    }

    public double getAxis(Axis axis) {
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

    public PartialPosition getPositionIn(UnitUtils.Units units) {
        double scale = UnitUtils.scaleUnits(this.units, units);
        Builder builder = new Builder();
        for (Map.Entry<Axis, Double> axis : getAll().entrySet()) {
            builder.setValue(axis.getKey(), axis.getValue()*scale);
        }
        builder.setUnits(units);
        return builder.build();
    }


    public static final class Builder {
        private Double x = null;
        private Double y = null;
        private Double z = null;
        private Double a = null;
        private Double b = null;
        private UnitUtils.Units units = UnitUtils.Units.UNKNOWN;

        public PartialPosition build() {
            return new PartialPosition(x, y, z, a, b, units);
        }

        public Builder setValue(Axis axis, Double value) {
            switch (axis) {
                case X:
                    this.x = value;
                    break;
                case Y:
                    this.y = value;
                    break;
                case Z:
                    this.z = value;
                    break;
                case A:
                    this.a = value;
                    break;
                case B:
                    this.b = value;
                    break;
            }
            return this;
        }

        public Builder setX(Double x) {
            this.x = x;
            return this;
        }

        public Builder setY(Double y) {
            this.y = y;
            return this;
        }

        public Builder setZ(Double z) {
            this.z = z;
            return this;
        }

        public Builder setA(Double a) {
            this.a = a;
            return this;
        }

        public Builder setB(Double b) {
            this.b = b;
            return this;
        }
        
        public Builder copy(Position position) {
            this.x = position.getX();
            this.y = position.getY();
            this.z = position.getZ();
            this.a = position.getA();
            this.b = position.getB();
            this.units = position.getUnits();
            return this;
        }

        public Builder setUnits(UnitUtils.Units units) {
            this.units = units;
            return this;
        }

        public Builder copy(PartialPosition position) {
            this.x = position.getX();
            this.y = position.getY();
            this.z = position.getZ();
            this.a = position.getA();
            this.b = position.getB();
            this.units = position.getUnits();
            return this;
        }
    }

    public String getFormattedGCode() {
        return getFormattedGCode(Utils.formatter);

    }

    public String getFormattedGCode(NumberFormat formatter) {
        StringBuilder sb = new StringBuilder();
        if (this.hasX()) {
            sb.append("X").append(formatter.format(this.getX()));
        }
        if (this.hasY()) {
            sb.append("Y").append(formatter.format(this.getY()));
        }
        if (this.hasZ()) {
            sb.append("Z").append(formatter.format(this.getZ()));
        }
        if (this.hasA()) {
            sb.append("A").append(formatter.format(this.getA()));
        }
        if (this.hasB()) {
            sb.append("B").append(formatter.format(this.getB()));
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PartialPosition)) return false;
        PartialPosition that = (PartialPosition) o;
        return Objects.equals(x, that.x) &&
                Objects.equals(y, that.y) &&
                Objects.equals(z, that.z) &&
                Objects.equals(a, that.a) &&
                Objects.equals(b, that.b) &&
                Objects.equals(units, that.units);

    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, a, b);
    }

    @Override
    public String toString() {
        return "PartialPosition{" + getFormattedGCode() + " ["+ units + "]}";
    }
}
