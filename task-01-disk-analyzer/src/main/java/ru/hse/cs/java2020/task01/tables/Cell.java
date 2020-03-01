package ru.hse.cs.java2020.task01.tables;

public class Cell {

    private String data;
    private String measure;
    private int width;
    private boolean isRightAlignment;

    public Cell(String data, String measure, int width) {
        this.data = data;
        this.measure = measure;
        this.width = width;
        isRightAlignment = true;
    }

    public Cell(String data, String measure) {
        this.data = data;
        this.measure = measure;
        isRightAlignment = false;
    }

    @Override
    public String toString() {
        if (isRightAlignment && width > data.length()) {
            return String.format("%-" + (width - data.length()) + "s", "") + data + measure;
        } else {
            return "  " + data + measure;
        }
    }
}
