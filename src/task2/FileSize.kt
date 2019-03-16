package task2

import java.io.ByteArrayInputStream
import java.io.File

class FileSize {
    private val result = mutableMapOf<String, Long>()
    private var baseByte = 1024
    private var baseDirectory: File = File("C:\\Users\\User\\Desktop\\KotlinAsFirst2018")

    /**
     * Display all available function .
     */
    private fun displayAvailableFun() {
        println("TYPE")
        println("-h : to see files with sizes")
        println("-c : sum of sizes")
        println("--si : to take 1000 as units of measurement")
    }

    /**
     * search the file path or directory path
     */

    fun searchFile(name: String, directory: File): MutableList<File> {
        val result = mutableListOf<File>()
        if (!directory.isDirectory) {
            if (directory.isFile)
                result.add(directory)
        } else {
            for (temp in directory.listFiles()) {
                if (temp.name == name) {
                    result.add(temp.absoluteFile)
                    if (result.isNotEmpty())
                        break
                } else if (temp.isDirectory) {
                    val answr = searchFile(name, temp)

                    if (answr.isNotEmpty())

                        result.add(answr.first())
                }
            }
        }
        return result
    }

    /**
     * return the file's size or directory's size
     */
    fun getSize(directory: File): Long {

        var length: Long = 0
        if (!directory.isDirectory) {
            length = directory.length()
        } else if (directory.isDirectory) {

            for (file in directory.listFiles()) {
                length += if (file.isFile)
                    file.length()
                else
                    getSize(file)
            }
        }
        return length
    }

    private fun use(name: String): Boolean {

        if (searchFile(name, baseDirectory).isEmpty())
            return false
        val paths = searchFile(name, baseDirectory).first().absoluteFile
        result[name] = getSize(paths) / 1024
        return true
    }

    private fun convertiser(kiloByte: Long, base: Int): String {
        val byte = kiloByte.toDouble() * base
        val megaB = kiloByte.toDouble() / base
        val gigaB = kiloByte / Math.pow(kiloByte.toDouble(), 2.0)

        return when {
            gigaB > 1.0 -> "$gigaB GB"
            megaB > 1.0 -> "$megaB MB"
            byte < 1000.0 -> "$byte B"
            else -> kiloByte.toDouble().toString() + "KB"
        }
    }

    fun runMenu() {
        var i = 5
        println("Welcome to FileFolderSearcher\n" +
                "the currently file is: $baseDirectory")
        do {
            println("Press")
            println("1 - to find in this directory")
            println("2 - to change directory")
            println("0 - at any time to exit of the system")
            val sc = readLine()
            if (sc == "1" || sc == "2" || sc == "0")
                i = sc.toInt()
            if (i !in 0..2)
                System.err.println("please press one of the proposed directive")
            if (i == 0)
                System.exit(0)
        } while (i !in 1..2)

        var nameFile: String
        var size: String
        var boolean = false

        when (i) {
            1 -> {
                println("Put the names of files and Type DONE when all files done!")
                readSet()
                if (result.isEmpty()) {
                    System.err.println("you did not type any file")
                    System.exit(1)
                }
                displayAvailableFun()
                do {
                    val sc = readLine()
                    if (sc == "0")
                        System.exit(0)
                    if (sc == "-h") {
                        boolean = true
                        for ((file, sizeFile) in result) {
                            nameFile = file
                            size = convertiser(sizeFile, baseByte)
                            println("$nameFile : $size")
                        }
                    }
                    if (sc == "-c") {
                        nameFile = result.keys.toList().joinToString()
                        if (boolean) {
                            size = convertiser(result.values.sum(), baseByte)
                            println("sum of sizes : $nameFile = $size")
                        } else {
                            size = (result.values.sum().toDouble()).toString()
                            println("sum of sizes : $nameFile = $size KB")
                        }
                    }
                    if (sc == "--si") {
                        this.baseByte = 1000
                    }

                } while (sc != "DONE")

            }
            2 -> {
                do {
                    var verifier = true
                    println("Put the directory")
                    val sc = readLine()
                    if (sc == "0")
                        System.exit(0)
                    if (sc != null) {
                        if (File(sc).canRead())
                            this.baseDirectory = File(sc)
                        else {
                            System.err.println("this directory can not read\n" +
                                    "Please retry or type 0 to exit")
                            verifier = false
                        }
                    }
                } while (sc == null || !verifier)
                runMenu()
            }
        }
    }

    private fun readSet() {
        var any: Boolean
        while (true) {

            val n = readLine()
            if (n == "0")
                System.exit(0)
            if (n == "DONE") {
                break
            }
            any = use(n!!)
            if (!any) {
                System.err.println("this file can't found; put the correct file or type 0 to exit!")
                println("type DONE when all done!")

            }
        }

    }

}

fun main() {
}