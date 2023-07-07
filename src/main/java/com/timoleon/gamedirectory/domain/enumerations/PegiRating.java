package com.timoleon.gamedirectory.domain.enumerations;

public enum PegiRating {
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
                return "Three";
            case 1:
                return "Seven";
            case 2:
                return "Twelve";
            case 3:
                return "Sixteen";
            case 4:
                return "Eighteen";
            default:
                return null;
        }
    }
}
