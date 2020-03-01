package ru.hse.cs.java2020.task01.tables;

import java.util.LinkedList;

public class Line {

    private LinkedList<Cell> cells;

    public Line() {
        cells = new LinkedList<>();
    }

    public void addCell(Cell cell) {
        cells.add(cell);
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();

        for (int i = 0; i < cells.size() - 1; ++i) {
            sb.append(cells.get(i).toString()).append("|");
        }

        sb.append(cells.getLast());

        return sb.toString();
    }
}
