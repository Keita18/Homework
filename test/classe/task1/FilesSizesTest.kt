package classe.task1

import org.junit.Test
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream
import kotlin.test.assertEquals

internal class FilesSizesTest {
    private var readable = false
    private var standard = 1024
    private var commonSize = true

    private val directoryInput = File("input")
    private val file1 = File("input/file1.txt")
    private val directoryFiles = File("input/directoryFiles")
    private val file2 = File("input/directoryFiles/file2.txt")
    private val file3 = File("input/directoryFiles/file3.txt")
    private val fileEmpty = File("")

    private val argsList = listOf(directoryFiles, directoryInput, file1, file2, file3)
    private var fileS = FilesSizes(argsList.map { it.toString() }, readable, standard)
    @Test
    fun getFileSize() {
        assertEquals(56682, fileS.getFileSize(file1))
        assertEquals(0, fileS.getFileSize(fileEmpty))
        assertEquals(fileS.getSumSize(listOf(file3)), fileS.getFileSize(file3))
        assertEquals(file1.length() + file3.length() + file2.length(), fileS.getFileSize(directoryInput))
    }

    @Test
    fun getSumSize() {
        assertEquals(396450, fileS.getSumSize(argsList))
        assertEquals(56682, fileS.getSumSize(listOf(file1)))
        assertEquals(197536, fileS.getSumSize(listOf(file1, file3, directoryFiles)))

    }

    @Test
    fun convertToKB() {
        assertEquals("45,40", fileS.convertToKB(fileS.getFileSize(file3)))
        assertEquals("55,35", fileS.convertToKB(fileS.getFileSize(file1)))

        standard = 1000
        fileS = FilesSizes(argsList.map { it.toString() }, readable, standard)
        assertEquals("46,49",fileS.convertToKB(fileS.getFileSize(file3)))
        assertEquals("56,68", fileS.convertToKB(fileS.getFileSize(file1)))

    }

    @Test
    fun convertToReadable() {
        assertEquals("45,40KB", fileS.convertToReadable(fileS.getFileSize(file3)))
        assertEquals("55,35KB", fileS.convertToReadable(fileS.getFileSize(file1)))
        assertEquals("387,16KB", fileS.convertToReadable(fileS.getSumSize(argsList)))

    }

    @Test
    fun display() {
        val baos = ByteArrayOutputStream()
        val ps = PrintStream(baos)
        val old = System.out
        System.setOut(ps)
        fileS.display(commonSize)
        System.out.flush()
        System.setOut(old)
        assertEquals("Common size: ${fileS.convertToKB(fileS.getSumSize(argsList))}", baos.toString())
    }

    @Test
    fun getConvertSize() {
        assertEquals("45,40", fileS.convertToKB(fileS.getFileSize(file3)))
        assertEquals("55,35", fileS.convertToKB(fileS.getFileSize(file1)))

        readable = true
        assertEquals("45,40KB", fileS.convertToReadable(fileS.getFileSize(file3)))
        assertEquals("55,35KB", fileS.convertToReadable(fileS.getFileSize(file1)))
        assertEquals("387,16KB", fileS.convertToReadable(fileS.getSumSize(argsList)))
    }

}