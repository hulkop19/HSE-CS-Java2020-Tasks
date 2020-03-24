package ru.hse.cs.java2020.task02.cache;

public class DataInDiskInfo {

    private int offset;
    private int length;

    public DataInDiskInfo(int offset, int length) {
        this.offset = offset;
        this.length = length;
    }

    public int getOffset() {
        return offset;
    }

    public int getLength() {
        return length;
    }
}
