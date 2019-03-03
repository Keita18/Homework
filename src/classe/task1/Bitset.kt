package classe.task1


import java.lang.IndexOutOfBoundsException
import java.util.*
import kotlin.experimental.and
import kotlin.experimental.or
import kotlin.experimental.xor
import kotlin.math.*
import kotlin.math.min


internal class Bitset {
    private var byteArray: ByteArray? = null     // the array of bytes (8-bit integers)
    private var maxSize: Int = 0           // max # of set elements allowed

    constructor()                // make an empty set of capacity zero.
    {
        maxSize = 0
        byteArray = null
    }

    constructor(size: Int) {
        maxSize = size
        val nbyte = (size + 7) / 8
        byteArray = ByteArray(nbyte)          // new array, all zeroes
    }


    constructor(setA: Bitset) {
        maxSize = setA.maxSize
        val nbyte = setA.byteArray!!.size
        byteArray = ByteArray(nbyte)          // new array, all zeroes
        System.arraycopy(setA.byteArray!!, 0,
                byteArray!!, 0, setA.byteArray!!.size)
    }


    fun clearElement(n: Int) {
        if (n >= maxSize) throw IndexOutOfBoundsException()
        val whichByte = n / 8
        val whichBit = n % 8
        this.byteArray!![whichByte] = this.byteArray!![whichByte] and (1 shl whichBit xor 255).toByte()
    }


    fun clear() {
        if (byteArray == null)
            error("clear: Can't clear a set that hasn't been constructed!")
        for (i in byteArray!!.indices)
            byteArray!![i] = 0
    }

    fun setSize(size: Int) {
        maxSize = size
        val nbyte = (size + 7) / 8
        byteArray = ByteArray(nbyte)    // new array, all zeroes
    }


    operator fun contains(i: Int)  // same as member(), reads well:
            : Boolean {                                // e.g., mySet.contains(3);
        return if (i >= maxSize) false else {
            val whichByte = i / 8
            val whichBit = i % 8
            this.byteArray!![whichByte].toInt() and (1 shl whichBit) != 0
        }
    }


    fun getSet(setA: Bitset): Bitset {
        if (byteArray!!.size < setA.byteArray!!.size)
            error("getSet: source set larger than dest. set")
        clear()

        val nbyte = setA.byteArray!!.size
        for (i in 0 until nbyte)
        // copy byteArray from arg.
            byteArray!![i] = setA.byteArray!![i]
        return this                    // return receiver, updated
    }

    fun union(setB: Bitset): Bitset {
        val temp = Bitset(if (maxSize > setB.maxSize) this else setB)

        val nbyte = min(byteArray!!.size, setB.byteArray!!.size)
        for (i in 0 until nbyte)
            temp.byteArray!![i] = byteArray!![i] or setB.byteArray!![i]
        return temp
    }

    fun difference(setB: Bitset): Bitset {
        val temp = Bitset(this)
        val nbyte = min(byteArray!!.size, setB.byteArray!!.size)
        for (i in 0 until nbyte)
            temp.byteArray!![i] = byteArray!![i] and (setB.byteArray!![i] xor 255.toByte())
        return temp
    }

    fun intersect(setB: Bitset): Bitset {
        val temp = Bitset(min(maxSize, setB.maxSize))
        val nbyte = min(byteArray!!.size, setB.byteArray!!.size)
        for (i in 0 until nbyte)
            temp.byteArray!![i] = byteArray!![i] and setB.byteArray!![i]
        return temp
    }

    fun equals(setB: Bitset): Boolean {

        val nbyte = min(byteArray!!.size, setB.byteArray!!.size)

        for (i in 0 until nbyte) {
            if (byteArray!![i] != setB.byteArray!![i]) return false
        }

        if (byteArray!!.size > nbyte) {
            for (i in nbyte until byteArray!!.size) {
                if (byteArray!![i].toInt() != 0) return false
            }
        }

        if (setB.byteArray!!.size > nbyte) {
            for (i in nbyte until setB.byteArray!!.size) {
                if (setB.byteArray!![i].toInt() != 0) return false
            }
        }
        return true
    }

    fun readSet(element: Int) {
        if (element in 0..(maxSize - 1)) {

            val whichByte = element / 8
            val whichBit = element % 8
            this.byteArray!![whichByte] = this.byteArray!![whichByte] or (1 shl whichBit).toByte()
        }
    }

    override fun toString(): String {
        var str = "{  "
        for (i in 0 until maxSize) {
            if (contains(i)) str += "$i  "
        }
        return "$str}"
    }

    fun cardinality(): Int {
        var count = 0
        for (i in 0 until maxSize) {
            if (contains(i)) count++
        }
        return count
    }

    fun isSubset(b: Bitset): Boolean {
        for (i in 0 until maxSize) {
            if (contains(i) && !b.contains(i)) {
                return false
            }
        }
        return true
    }

 private fun error(msg: String) {
        print(" $msg")
        System.exit(1)
    }
}

fun main() {
    val x = Bitset(8)
    x.readSet(2)
    x.readSet(3)
    val y = Bitset(9)
    y.readSet(5)
    y.readSet(3)
    y.clear()
    println(y.toString())
    println(y.contains(5))
}