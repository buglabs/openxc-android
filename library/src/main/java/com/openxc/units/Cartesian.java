package com.openxc.units;

import java.lang.reflect.Array;


public class Cartesian<T extends Number> extends Unit {

    private T valX;
    private T valY;
    private T valZ;

    public Cartesian(T[] valueArray) {
        if (valueArray.length == 3) {
            valX = valueArray[0];
            valY = valueArray[1];
            valZ = valueArray[2];
        }
    }

    public Cartesian(T x, T y, T z) {
        valX = x;
        valY = y;
        valZ = z;
    }

    public T getX_value() {
        if (valX != null) {
            return valX;
        }
        else return null;
    }
    public T getY_value() {
        if (valY != null) {
            return valY;
        }
        else return null;
    }
    public T getZ_value() {
        if (valZ != null) {
            return valZ;
        }
        else return null;
    }

    @Override
    public Object getSerializedValue() {
        T[] serialized = (T[]) Array.newInstance(Float.class, 3);
        serialized[0] = valX;
        serialized[1] = valY;
        serialized[2] = valZ;
        return serialized;
    }

    @Override
    public String toString() {
        return "X:"+valX+", Y:"+valY+", Z:"+valZ;
    }
}
