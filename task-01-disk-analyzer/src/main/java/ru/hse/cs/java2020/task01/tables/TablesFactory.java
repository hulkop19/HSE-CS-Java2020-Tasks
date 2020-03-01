package ru.hse.cs.java2020.task01.tables;

import ru.hse.cs.java2020.task01.dataFormats.AnalysisResult;

import java.nio.file.Files;

public final class TablesFactory {

    static final int BYTES_IN_KILOBYTE = 1024;
    static final int PERCENTS = 100;
    static final int NAME_COLUMN_WIDTH = 30;
    static final int SIZE_COLUMN_WIDTH = 12;
    static final int PERCENT_COLUMN_WIDTH = 6;
    static final int ITEMS_COLUMN_WIDTH = 10;

    private TablesFactory() {

    }

    public static Table createDiskUsageTable(AnalysisResult analysisResult) {
        var table = new Table("Disc Usage");

        for (var currentFileInfo : analysisResult.getFirstLevelFileInfos()) {
            var line = new Line();

            if (Files.exists(currentFileInfo.getFilePath())) {
                long currentFileSize = currentFileInfo.getSize() / BYTES_IN_KILOBYTE;
                long rootFileSize = analysisResult.getRootInfo().getSize() / BYTES_IN_KILOBYTE;
                double percents = ((double) currentFileSize / (double) rootFileSize) * PERCENTS;

                line.addCell(new Cell(currentFileInfo.getFilePath().getFileName().toString(), "", NAME_COLUMN_WIDTH));
                line.addCell(new Cell(String.valueOf(currentFileSize), " Kb", SIZE_COLUMN_WIDTH));
                line.addCell(new Cell(String.format("%.2f", percents), "%", PERCENT_COLUMN_WIDTH));

                if (Files.isDirectory(currentFileInfo.getFilePath())) {
                    line.addCell(new Cell(String.valueOf(currentFileInfo.getItemsCount()), " items", ITEMS_COLUMN_WIDTH));
                } else {
                    line.addCell(new Cell("", "", ITEMS_COLUMN_WIDTH));
                }
            }

            table.addLine(line);
        }

        return table;
    }

    public static Table createBiggestFilesTable(AnalysisResult analysisResult) {
        var table = new Table("BIGGEST FILES");

        for (var fileInfo : analysisResult.getBiggestFileInfos()) {
            var line = new Line();

            line.addCell(new Cell(String.valueOf(fileInfo.getSize() / BYTES_IN_KILOBYTE), " Kb", SIZE_COLUMN_WIDTH));
            line.addCell(new Cell(fileInfo.getFilePath().toString(), ""));

            table.addLine(line);
        }

        return table;
    }
}
