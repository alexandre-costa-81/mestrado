package boku;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class LogWriter {

    public static PrintStream out = System.out;
    private static PrintStream fileStream = null;
    private static File logFile = null;
    private static final NullWriter nullWriter = new NullWriter(System.out);

    public static final int MOVES_ONLY = 1;
    public static final int MOVES_EVAL = 2;
    public static final int BEST_LINES = 4;
    public static final int ALL_LINES = 8;
    public static final int PRINT_STAT = 256;
    private static int printModus = 257;

    public static final int NO_OUTPUT = 0;

    public static final int STD_OUT = 1;

    public static final int FILE_OUT = 2;

    public static String getFileName() {
        if (logFile == null) {
            return "";
        }
        return logFile.getName();
    }

    public static void setFile(File newLogFile) {
        logFile = newLogFile;
        try {
            fileStream = new PrintStream(new FileOutputStream(logFile));
            out = fileStream;
        } catch (IOException io) {
            System.err.println("Could not open " + logFile.getName());
            out = System.out;
        }
    }

    public static boolean fileAvailable() {
        return logFile != null;
    }

    public static void switchTo(int choice) {
        switch (choice) {
            case 2:
                if (fileAvailable()) {
                    out = fileStream;
                }
                break;
            case 1:
                out = System.out;
                break;
            case 0:
            default:
                out = nullWriter;
        }

    }

    public static boolean isSetTo(int choice) {
        switch (choice) {
            case 2:
                return (fileAvailable()) && (out == fileStream);
            case 1:
                return out == System.out;
            case 0:
                return out == nullWriter;
        }
        return false;
    }

    public static void enablePrintModus(int choice) {
        printModus |= choice;
    }

    public static void disablePrintModus(int choice) {
        if ((printModus != choice) || (choice >= 256)) {
            printModus &= (choice ^ 0xFFFFFFFF);
        }
    }

    public static void switchPrintModus(int choice) {
        if ((printModus != choice) || (choice >= 256)) {
            printModus ^= choice;
        }
    }

    public static boolean isPrintModus(int choice) {
        return (printModus & choice) == choice;
    }

    public static void println(Statistics stat) {
        if (isPrintModus(256)) {
            out.print(stat.toString());
        }
    }

    private static class NullWriter extends PrintStream {

        public NullWriter(OutputStream out) {
            super(out);
        }


        public void println(String str) {
        }

        public void print(String str) {
        }
    }
}


/* Location:              /home/alexandre/Boku1.21.jar!/boku/LogWriter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */
