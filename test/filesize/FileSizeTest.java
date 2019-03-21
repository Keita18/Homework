package filesize;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class FileSizeTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @BeforeEach
    void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(System.out);
    }

    private FileSize fileSize = new FileSize();
    @Test
    void runMenu() {
        System.setIn(new ByteArrayInputStream("1\nIrodov.pdf\nDONE\n-c\n-h\n-c\n--si\n-h\n-c\n-0\n".getBytes()));
        fileSize = new FileSize();
        fileSize.runMenu();
        String[] output = outContent.toString().replace("\r\n", "\n").split("\n");

        assertEquals("Put the names of files and Type DONE when all files done!", output[6]);
        assertEquals("TYPE", output[7]);
        assertEquals("-h : to see files with sizes", output[8]);
        assertEquals("-c : sum of sizes", output[9]);
        assertEquals("--si : to take 1000 as units of measurement", output[10]);
        assertEquals("sum of sizes : [Irodov.pdf] = 17644 KB", output[11]);
        assertEquals("Irodov.pdf : 17.23 MB", output[12]);
        assertEquals("sum of sizes : [Irodov.pdf] = 17.23 MB", output[13]);
        assertEquals("Irodov.pdf : 18.07 MB", output[14]);
        assertEquals("sum of sizes : [Irodov.pdf] = 18.07 MB", output[15]);
    }
    @Test
    void run(){
        System.setIn(new ByteArrayInputStream("2\n33\n-0\nIrodov.pdf\nDONE\n-c\n-h\n-c\n--si\n-h\n-c\n-0\n".getBytes()));

        fileSize = new FileSize();
        fileSize.runMenu();
        String[] output = outContent.toString().replace("\r\n", "\n").split("\n");
        assertNotEquals("yyyyyyyy", Arrays.toString(output));
    }
}