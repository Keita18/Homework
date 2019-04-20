
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.DefaultParser
import org.apache.commons.cli.Option
import org.apache.commons.cli.Options
import java.io.File

class FilesSizes (listFilesStr: List<String>, private val readable: Boolean, private val standard: Int) {

    private val listFiles: MutableSet<File>

    init {

        if (listFilesStr.isEmpty())
            throw IllegalArgumentException("List of filenames is empty")

        listFiles = mutableSetOf()

        for (fileStr in listFilesStr) {
            val tmpFile = File(fileStr)
            if (!tmpFile.exists())
                throw IllegalArgumentException("File " + tmpFile.name + " does not exist")

            listFiles.add(tmpFile)
        }
    }

    fun getFileSize(file: File): Long {

        val listFiles = file.listFiles() ?: return file.length()

        var result: Long = 0
        for (someFile in listFiles)
            result +=  getFileSize(someFile)

        return result
    }

    fun getSumSize(listFiles: Iterable<File>): Long = listFiles.map { getFileSize(it) }.sum()


    fun convertToKB(bytes: Long): String {
        val conversionVal = standard

        return String.format("%.2f", bytes.toDouble() / conversionVal)
    }

    fun convertToReadable(bytes: Long): String {
        val conversionVal = standard

        var kBytes = bytes.toDouble() / conversionVal

        val prefix = arrayOf("KB", "MB", "GB", "TB")
        val maxIndex = prefix.size - 1
        var index = 0

        while (index < maxIndex && kBytes >= conversionVal) {
            kBytes /= conversionVal.toDouble()
            index++
        }

        return String.format("%.2f", kBytes) + prefix[index]
    }

    fun display(isCommon: Boolean) {
        val sumFile = "Common size: " + getConvertSize(getSumSize(listFiles))

        val eachFile = StringBuilder()
        for (file in listFiles)
            eachFile.append(String.format("%1$-40s", file.name)).append(getConvertSize(getFileSize(file))).append('\n')

        if (isCommon)
            println(sumFile)
        else println(eachFile.toString())

    }

    fun getConvertSize(bytes: Long): String {
        return if (readable) convertToReadable(bytes) else convertToKB(bytes)
    }
}

object CommandLine {
    @JvmStatic
    fun main(args: Array<String>) {

        val humanReadFormat = Option("h", false, "Human readable format")
        humanReadFormat.isRequired = false

        val sumAll = Option("c", false, "Calculate size of all files")
        sumAll.isRequired = false

        val siFormat = Option("si", false, "Use SI values of prefixes")
        siFormat.isRequired = false

        val options = Options()
        options.addOption(humanReadFormat)
        options.addOption(sumAll)
        options.addOption(siFormat)

        val clParser = DefaultParser()
        val commandLine: CommandLine

        try {
            commandLine = clParser.parse(options, args)

            val standard = if (commandLine.hasOption("si")) 1000 else 1024
            val readable = commandLine.hasOption("h")

            val fSize = FilesSizes(commandLine.argList, readable, standard)

            val isCommon = commandLine.hasOption("c")
            fSize.display(isCommon)

        } catch (e: Exception) {
            System.err.println(e.message)
            System.exit(1)
        }
    }
}