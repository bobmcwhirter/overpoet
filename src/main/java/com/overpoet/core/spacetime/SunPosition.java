package com.overpoet.core.spacetime;

public class SunPosition {

    static int julienCentury(double julienDate) {
        return (int) ( julienDate - 2451545 ) / 36525;
    }

    static int julienMillenium(int julienCentury) {
        return julienCentury / 10;
    }

}
