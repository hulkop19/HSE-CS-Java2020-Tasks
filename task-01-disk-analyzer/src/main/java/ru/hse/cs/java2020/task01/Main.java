package ru.hse.cs.java2020.task01;

import ru.hse.cs.java2020.task01.dataFormats.AnalysisResult;
import ru.hse.cs.java2020.task01.diskAnylyzer.DiskAnalyzer;
import ru.hse.cs.java2020.task01.tables.TablesFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    static final int MILLISEC_IN_SEC = 1000;

    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();

        if (!isValidArguments(args)) {
            return;
        }

        Path path = Paths.get(args[0]);

        AnalysisResult analysisResult = DiskAnalyzer.analyze(path);

        var discUsageTable = TablesFactory.createDiskUsageTable(analysisResult);
        System.out.println(discUsageTable);
        var biggestFilesTable = TablesFactory.createBiggestFilesTable(analysisResult);
        System.out.println(biggestFilesTable);

        long endTime = System.currentTimeMillis();

        System.out.println(String.format("time: %.3f sec", (double) (endTime - startTime) / MILLISEC_IN_SEC));
    }

    private static boolean isValidArguments(String[] args) {
        if (args.length != 1) {
            System.out.println("must be at least 1 argument");
            return false;
        }

        if (!Files.exists(Paths.get(args[0]))) {
            System.out.println("directory does not exist");
            return false;
        }

        if (!Files.isDirectory(Paths.get(args[0]))) {
            System.out.println("is not a directory");
            return false;
        }

        return true;
    }
}

