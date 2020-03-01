package ru.hse.cs.java2020.task01.tables;

import java.util.ArrayList;

public class Table {

    private String topic;
    private ArrayList<Line> lines;

    public Table(String topic) {
        this.topic = topic;
        lines = new ArrayList<>();
    }

    public void addLine(Line line) {
        lines.add(line);
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        int lineNumber = 0;

        sb.append("==========").append(topic).append("==========\n");

        for (var line: lines) {
            ++lineNumber;
            sb.append(lineNumber).append(".\t").append(line).append("\n");
        }

        return sb.toString();
    }
}

