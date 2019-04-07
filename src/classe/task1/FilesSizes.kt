package classe.task1

import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.DefaultParser
import org.apache.commons.cli.Option
import org.apache.commons.cli.Options
import java.io.File
import java.util.*


class FilesSizes (listFilesStr: List<String>) {

    private val listFiles: MutableSet<File>
    var readable: Boolean = false
    var standard: Int = 1024

    init {

        if (listFilesStr.isEmpty())
            throw IllegalArgumentException("List of filenames is empty")

        listFiles = mutableSetOf()

        for (fileStr in listFilesStr) {
            val tmpFile = File(fileStr)
            if (!tmpFile.exists())
                throw IllegalArgumentException("File " + tmpFile.name + " is not exist")

            listFiles.add(tmpFile)
        }
    }
    private fun getFileSize(file: File): Long {

        val listFiles = file.listFiles() ?: return file.length()

        var result: Long = 0
        for (someFile in listFiles)
            result += if (someFile.isFile)
                someFile.length()
            else
                getFileSize(someFile)

        return result
    }

    private fun getSumSize(listFiles: Iterable<File>): Long {

        var result: Long = 0
        for (file in listFiles) {
            result += getFileSize(file)
        }

        return result
    }

    private fun convertToKB(bytes: Long): String {
        val conversionVal = standard

        return String.format("%.2f", bytes.toDouble() / conversionVal)
    }

    private fun convertToReadable(bytes: Long): String {
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


    val eachFile: String
        get() {
            val strBuff = StringBuilder()
            for (file in listFiles)
                strBuff.append(String.format("%1$-40s", file.name)).append(getConvertSize(getFileSize(file))).append('\n')

            return strBuff.toString()
        }

    val sumFile: String
        get() = "Common size: " + getConvertSize(getSumSize(listFiles))

    private fun getConvertSize(bytes: Long): String {
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
        val arg = Scanner(System.`in`)

        try {
            val y = arg.nextLine().split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            commandLine = clParser.parse(options, y)
            val fSize = FilesSizes(commandLine.argList)

            fSize.standard = if (commandLine.hasOption("si")) 1000 else 1024
            fSize.readable = commandLine.hasOption("h")
            val isCommon = commandLine.hasOption("c")

            if (isCommon)
                println(fSize.sumFile)
            else
                println(fSize.eachFile)
        } catch (e: Exception) {
            System.err.println(e.message)

            try {
                Thread.sleep(2000L)
            } catch (ex: InterruptedException) {
                Thread.currentThread().interrupt()
                throw RuntimeException(ex)
            }

            System.exit(1)
        }

    }
}