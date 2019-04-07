package classe.task1

import java.util.*
import kotlin.experimental.and
import kotlin.experimental.or
import kotlin.experimental.xor
import kotlin.math.min

class Bitset {
    private var byteArray: ByteArray                                 // the array of bytes (8-bit integers)
    private var maxSize: Int = 0                                    // max # of set elements allowed

    constructor(size: Int) {                                      //  the number of size requested foe the BitSet
        require(size > 0) { "size must be greater than 0" }
        maxSize = size
        val nbyte = (size + 7) / 8
        byteArray = ByteArray(nbyte)                           // new array, all zeroes
    }


    constructor(setA: Bitset) {                                   //  this duplicate the existing BitSet
        maxSize = setA.maxSize
        val nbyte = setA.byteArray.size
        byteArray = ByteArray(nbyte)                            // new array, all zeroes
        System.arraycopy(setA.byteArray, 0,
                byteArray, 0, setA.byteArray.size)
    }

    fun add(element: Int) {                                  // this fun add element to the set
        require(element in 0 until maxSize) { "element must be between 0 and maxSize" }

        val byteIndex = element / 8                        //  piking the byte
        val bitInByteIndex = element % 8                  // piking the bits
        this.byteArray[byteIndex] = this.byteArray[byteIndex] or (1 shl bitInByteIndex).toByte()
    }

    fun addMassive(elements: Set<Int>) {
        for (elemToAdd in elements) {
            add(elemToAdd)
        }
    }

    fun remove(n: Int) {                        // this fun remove the value "n" from the array
        require(n in 0 until maxSize) { "element must be between 0 and maxSize" }
        val whichByte = n / 8
        val whichBit = n % 8
        this.byteArray[whichByte] = this.byteArray[whichByte] and (1 shl whichBit xor 255).toByte()
    }

    fun removeMassive(elements: Set<Int>) {

        for (elemToRem in elements)
            remove(elemToRem)
    }


    fun clear() {  // this fun clear all element from set
        if (cardinality() == 0) throw Exception("this set was not constructed ")
        for (i in byteArray.indices)
            byteArray[i] = 0
    }

    operator fun contains(i: Int): Boolean {                       // this fun checks to see if element(i) is in set
        if (i >= maxSize || i < 0) return false
        val whichByte = i / 8
        val whichBit = i % 8
        return this.byteArray[whichByte].toInt() and (1 shl whichBit) != 0 // return true if that value in the set!
    }

    fun intersect(setB: Bitset): Bitset {            // this fun makes a new set with values which are present in both
        val temp = Bitset(min(maxSize, setB.maxSize))
        val nbyte = min(byteArray.size, setB.byteArray.size)
        for (i in 0 until nbyte)
            temp.byteArray[i] = byteArray[i] and setB.byteArray[i]
        return temp
    }

    fun union(setB: Bitset): Bitset {                       // this fun makes a new set with values from both sets
        val temp = Bitset(if (maxSize > setB.maxSize) this else setB)

        val nbyte = min(byteArray.size, setB.byteArray.size)
        for (i in 0 until nbyte)
            temp.byteArray[i] = byteArray[i] or setB.byteArray[i]
        return temp
    }

    fun difference(setB: Bitset): Bitset {
        val temp = Bitset(this)
        val nbyte = min(byteArray.size, setB.byteArray.size)
        for (i in 0 until nbyte)
            temp.byteArray[i] = byteArray[i] and (setB.byteArray[i] xor 255.toByte())
        return temp
    }

    override fun hashCode(): Int = Arrays.hashCode(byteArray) + maxSize


    override fun equals(other: Any?): Boolean {               // this fun verify if this set is equals to the other set
        if (other !is Bitset) return false

        val minSize = min(byteArray.size, other.byteArray.size)            //  take of the shorter set
        for (i in 0 until minSize) {
                if (byteArray[i] != other.byteArray[i]) return false     // verify the both sets
            }

            //---------- if the sizes are not the same ----------
        if (byteArray.size > minSize) {                               // if this set bigger than other
                for (i in minSize until byteArray.size) {            // check if there are other elements above the
                    if (byteArray[i].toInt() != 0) return false     // maxSize of other
                }
            }

        if (other.byteArray.size > minSize) {                   // do the same if other is bigger than this
                for (i in minSize until other.byteArray.size) {
                    if (other.byteArray[i].toInt() != 0) return false
                }
            }

        return true
    }

    override fun toString(): String {  // this fun creates the String representation
        var str = "{ "
        for (i in 0 until maxSize) {
            if (contains(i)) str += "$i "
        }
        return "$str}"
    }

    fun cardinality(): Int {   // this fun return the number of elements in the set
        var count = 0
        for (elem in byteArray) {
            count += countBits(elem)
        }
        return count
    }

    private fun countBits(test: Byte): Int {
        var i = test

        val mask1: Byte = 0b01010101
        val mask2: Byte = 0b00110011
        val mask3: Byte = 0b00001111

        i = ((i and mask1) + ((i.toInt() ushr 1).toByte() and mask1)).toByte()
        i = ((i and mask2) + ((i.toInt() ushr 2).toByte() and mask2)).toByte()
        i = ((i and mask3) + ((i.toInt() ushr(4)).toByte() and mask3)).toByte()
        return i.toInt()
    }
}