package classe.task1


import kotlin.experimental.and
import kotlin.experimental.or
import kotlin.experimental.xor
import kotlin.math.min

class HashtableException : Exception {
    constructor() : super() {}
    constructor(s: String) : super(s) {}

}

class Bitset {
    private var byteArray: ByteArray? = null     // the array of bytes (8-bit integers)
    private var maxSize: Int = 0           // max # of set elements allowed

    constructor()                // make an empty set of capacity zero.
    {
        maxSize = 0
        byteArray = null
    }

    constructor(size: Int) {        //  the number of size requested foe the BitSet
        maxSize = size
        val nbyte = (size + 7) / 8
        byteArray = ByteArray(nbyte)          // new array, all zeroes
    }


    constructor(setA: Bitset) {             //  this duplicate the existing BitSet
        maxSize = setA.maxSize
        val nbyte = setA.byteArray!!.size
        byteArray = ByteArray(nbyte)          // new array, all zeroes
        System.arraycopy(setA.byteArray!!, 0,
                byteArray!!, 0, setA.byteArray!!.size)
    }

    fun add(element: Int) {                      // this fun add element to the set
        if (element in 0..(maxSize - 1)) {

            val byteIndex = element / 8 //  piking the byte
            val bitInByteIndex = element % 8  // piking the bits
            this.byteArray!![byteIndex] = this.byteArray!![byteIndex] or (1 shl bitInByteIndex).toByte()
        }
    }

    fun addMassive(elements: Set<Int>) {
        val actualSize = maxSize - cardinality()
        if (elements.size > actualSize) throw HashtableException("you can't not put all this elements in this set")
        for (elemToAdd in elements) {
            if (elemToAdd < this.maxSize && !contains(elemToAdd))
                add(elemToAdd)
        }
    }

    fun remove(n: Int) {  // this fun remove the value "n" from the array
        if (n >= maxSize) throw HashtableException("element must always lower than maxSize")
        val whichByte = n / 8
        val whichBit = n % 8
        this.byteArray!![whichByte] = this.byteArray!![whichByte] and (1 shl whichBit xor 255).toByte()
    }

    fun removeMassive(elements: Set<Int>) {
        val cardinality = cardinality()
        if (elements.size > cardinality) throw HashtableException("this set does'nt have many elements to remove")
        for (elemToRem in elements)
            if (elemToRem < this.maxSize && contains(elemToRem))
                remove(elemToRem)
    }


    fun clear() {  // this fun clear all element from set
        if (byteArray == null) throw throw HashtableException("this set was not constructed ")

        for (i in byteArray!!.indices)
            byteArray!![i] = 0
    }

    operator fun contains(i: Int)  // this fun checks to see if element(i) is in set
            : Boolean {
        return if (i >= maxSize) false else {   // element must always lower than maxSize
            val whichByte = i / 8
            val whichBit = i % 8
            this.byteArray!![whichByte].toInt() and (1 shl whichBit) != 0 // return true if that value in the set!
        }
    }

    fun intersect(setB: Bitset): Bitset {      // this fun makes a new set with values which are present in both
        val temp = Bitset(min(maxSize, setB.maxSize))
        val nbyte = min(byteArray!!.size, setB.byteArray!!.size)
        for (i in 0 until nbyte)
            temp.byteArray!![i] = byteArray!![i] and setB.byteArray!![i]
        return temp
    }

    fun union(setB: Bitset): Bitset {           // this fun makes a new set with values from both sets
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


    fun equals(Other: Bitset): Boolean {      // this fun verify if this set is equals to the other set

        val minSize = min(byteArray!!.size, Other.byteArray!!.size) //  take of the shorter set

        for (i in 0 until minSize) {
            if (byteArray!![i] != Other.byteArray!![i]) return false  // verify the both sets
        }

        // if the sizes are not the same///////////////////////
        if (byteArray!!.size > minSize) {      // if this set bigger than other
            for (i in minSize until byteArray!!.size) {     // check if there are other elements above the
                if (byteArray!![i].toInt() != 0) return false     // maxSize of other
            }
        }

        if (Other.byteArray!!.size > minSize) {          // do the same if other is bigger than this
            for (i in minSize until Other.byteArray!!.size) {
                if (Other.byteArray!![i].toInt() != 0) return false
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
        for (i in 0 until maxSize) {
            if (contains(i)) count++
        }
        return count
    }

}