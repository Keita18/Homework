package task2

import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream
import kotlin.test.assertEquals

class FileSizeTest {
    private val outContent = ByteArrayOutputStream()
    private var fileSize = FileSize()

    @Before
    fun setUp() {
        System.setOut(PrintStream(outContent))
    }

    @After
    fun tearDown() {
        System.setOut(System.out)
    }

    @Test
    fun searchFile() {
        var directory = File("C:\\Users\\User\\Desktop\\Irodov.pdf").absoluteFile.toString()
        var findFile = fileSize.searchFile("Irodov.pdf", File("C:\\Users\\User\\Desktop"))[0]
        assertEquals(directory, findFile.toString())

        directory = File("imput/file1.txt").absoluteFile.toString()
        findFile = fileSize.searchFile("file1.txt", File("imput"))[0]
        assertEquals(directory, findFile.toString())

        directory = File("C:\\Users\\User\\Desktop\\KotlinAsFirst2018\\input\\src2\\src1\\src0").absoluteFile.toString()
        findFile = fileSize.searchFile("src0", File("C:\\Users\\User\\Desktop\\KotlinAsFirst2018"))[0]
        assertEquals(directory, findFile.toString())
    }

    @Test
    fun getSize() {
        var fileLength = File("C:\\Users\\User\\Desktop\\Irodov.pdf").length()
        var getFileSize = fileSize.getSize(File("C:\\Users\\User\\Desktop\\Irodov.pdf"))
        assertEquals(fileLength, getFileSize)

        fileLength = 329975
        getFileSize = fileSize.getSize(File("C:\\Users\\User\\Desktop\\KotlinAsFirst2018\\input"))
        assertEquals(fileLength, getFileSize)
    }

    @Test
    fun runMenu() {
    }
}