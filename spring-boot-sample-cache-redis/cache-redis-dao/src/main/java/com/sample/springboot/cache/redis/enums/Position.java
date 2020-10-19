package com.sample.springboot.cache.redis.enums;

public enum Position implements Enums {

    POSITION_A(3, "职位A"),
    POSITION_B(6, "职位B"),
    POSITION_C(9, "职位C");

    private final int value;

    private final String label;

    Position(int value, String label) {
        this.value = value;
        this.label = label;
    }

    @Override
    public int value() {
        return this.value;
    }

    public static Position valueOf(int value){
        Position positionRole = resolve(value);
        if (positionRole == null) {
            throw new IllegalArgumentException("No matching constant for [" + value + "]");
        }
        return positionRole;
    }

    public static Position resolve(int value) {
        for (Position positionRole : values()) {
            if (positionRole.value == value) {
                return positionRole;
            }
        }
        return null;
    }

}
