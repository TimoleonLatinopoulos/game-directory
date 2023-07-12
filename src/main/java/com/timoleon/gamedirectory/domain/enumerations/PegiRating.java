package com.timoleon.gamedirectory.domain.enumerations;

public enum PegiRating {
    Zero(0),
    Three(3),
    Seven(7),
    Twelve(12),
    Sixteen(16),
    Eighteen(18);

    private int numVal;

    PegiRating(int numVal) {
        this.numVal = numVal;
    }

    public int getNumVal() {
        return numVal;
    }

    @Override
    public String toString() {
        switch (this.ordinal()) {
            case 0:
                return "Zero";
            case 1:
                return "Three";
            case 2:
                return "Seven";
            case 3:
                return "Twelve";
            case 4:
                return "Sixteen";
            case 5:
                return "Eighteen";
            default:
                return null;
        }
    }
}
