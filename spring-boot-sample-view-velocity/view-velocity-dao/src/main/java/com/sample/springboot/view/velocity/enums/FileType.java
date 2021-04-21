package com.sample.springboot.view.velocity.enums;

public enum FileType implements Enums {

    IMAGE(1),
    AUDIO(2),
    VIDEO(3),
    EXCEL(4),
    WORD(5),
    PDF(6);

    private final int value;

    FileType(int value) {
        this.value = value;
    }

    @Override
    public int value() {
        return this.value;
    }

    public static FileType valueOf(int value){
        FileType fileType = resolve(value);
        if (fileType == null) {
            throw new IllegalArgumentException("No matching constant for [" + value + "]");
        }
        return fileType;
    }

    public static FileType resolve(int value) {
        for (FileType fileType : values()) {
            if (fileType.value == value) {
                return fileType;
            }
        }
        return null;
    }

}
