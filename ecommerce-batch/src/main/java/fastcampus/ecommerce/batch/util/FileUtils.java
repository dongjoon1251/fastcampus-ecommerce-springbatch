package fastcampus.ecommerce.batch.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FileUtils {

  public static List<File> splitCsv(File csvFile, long fileCount) {
    return splitFileAfterLineCount(csvFile, fileCount, true, ".csv");
  }

  public static List<File> splitFileAfterLineCount(File inputFile, long fileCount,
      boolean ignoreFirstLine, String suffix) {
    long lineCount;
    try (Stream<String> stream = Files.lines(inputFile.toPath(), StandardCharsets.UTF_8)) {
      lineCount = stream.count();
      return splitFile(inputFile, (long) Math.ceil((double) lineCount / fileCount), ignoreFirstLine,
          suffix);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static List<File> splitFile(File inputFile, long linesPerFile, boolean ignoreFirstLine,
      String suffix)
      throws IOException {
    List<File> splitFiles = new ArrayList<>();
    BufferedReader reader = new BufferedReader(new FileReader(inputFile));

    String line;
    int fileIndex = 0;
    int lineCount = 0;
    File splitFile;
    BufferedWriter writer = null;
    boolean firstLine = true;
    boolean shouldCreateFile = true;

    while ((line = reader.readLine()) != null) {

      if (ignoreFirstLine && firstLine) {
        firstLine = false;
        continue;
      }
      if (shouldCreateFile) {
        splitFile = createTempFile("split_" + (fileIndex++) + "_", suffix);
        writer = new BufferedWriter(new FileWriter(splitFile));
        splitFiles.add(splitFile);
        lineCount = 0;
        shouldCreateFile = false;
      }
      writer.write(line);
      writer.newLine();
      lineCount++;

      if (lineCount >= linesPerFile) {
        writer.close();
        shouldCreateFile = true;
      }
    }

    writer.close();
    reader.close();

    return splitFiles;
  }

  public static File createTempFile(String prefix, String suffix) throws IOException {
    File tempFile = File.createTempFile(prefix, suffix);
    tempFile.deleteOnExit();
    return tempFile;
  }


  public static void mergeFiles(String header, List<File> files, File outputFile) {
    try (BufferedOutputStream outputStream = new BufferedOutputStream(
        new FileOutputStream(outputFile))) {
      outputStream.write((header + "\n").getBytes());
      for (File partFile : files) {
        System.out.println("병합 중: " + partFile.getName());
        Files.copy(partFile.toPath(), outputStream);
      }
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static List<File> splitLog(File logFile, long fileCount) {
    return splitFileAfterLineCount(logFile, fileCount, false, ".log");
  }
}