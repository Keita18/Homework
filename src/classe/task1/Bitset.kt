package classe.task1


import java.util.*
import kotlin.experimental.and
import kotlin.experimental.or
import kotlin.experimental.xor
import kotlin.math.*
import kotlin.math.min


internal class Bitset {
    private var byteArray: ByteArray? = null     // the array of bytes (8-bit integers)
    private var maxSize: Int = 0           // max # of set elements allowed

    /**
     * This method tests for an empty, or null, set, by walking through the
     * byteArray and checking each byte for inclusions (which produce a value
     * greater than 0).  If any are found, the Bitset is not a null set.
     *
     * @return       True if this is a null (empty) set, false if it is not.
     */
    val isNull: Boolean
        get() {
            for (i in byteArray!!.indices) {
                if (byteArray!![i].toInt() != 0) return false
            }
            return true
        }

    /***
     * No-arg constructor for the class, begins with a size of 0 and the
     * byte array set to null.
     */
    constructor()                // make an empty set of capacity zero.
    {
        maxSize = 0
        byteArray = null
    }

    /**
     * This constructor takes a size and creates a byte array that will accommodate
     * that number of elements.  Since there could possible be up to 7 bits needed
     * before we would have another complete byte, 7 is added to the requested size,
     * then when the size is divided by 8 for the number of bytes, the extra bits
     * have been allowed for.
     *
     * @param  size       The number of elements requested for the Bitset
     */
    constructor(size: Int) {
        maxSize = size
        val nbyte = (size + 7) / 8
        byteArray = ByteArray(nbyte)          // new array, all zeroes
    }

    /***
     * This constructor is a "copy constructor", which takes an existing Bitset
     * and makes a duplicate of it.  Since no elements are being modified the
     * param Bitset can be copied as bytes rather than doing individual bits.
     *
     * @param   setA    The existing Bitset to duplicate
     */
    constructor(setA: Bitset) {
        maxSize = setA.maxSize
        val nbyte = setA.byteArray!!.size
        byteArray = ByteArray(nbyte)          // new array, all zeroes
        System.arraycopy(setA.byteArray!!, 0,
                byteArray!!, 0, setA.byteArray!!.size)
    }

    /**
     * Sets the bit at given offset from address byteArray to 1
     * i.e., adds element 'offset' to the set
     *
     * @param   n   The bit to be set
     */
    private fun setBit(n: Int) {
        /*  Remember, we can find out which _byte_ we need using division:
	    e.g., 25 / 8 tells us that bit 25 is located in byte 3.
	    However, we then need to locate the _bit_ to modify and this uses
	    remainder:  25 % 8 leaves 1, so bit 25 is in location 1 in that byte
	    (NOTE: each byte indexes from 0!!)
	*/
        val whichByte = n / 8
        val whichBit = n % 8
        this!!.byteArray!![whichByte] = this!!.byteArray!![whichByte] or (1 shl whichBit).toByte()
    }

    /**
     * Returns true if the bit at given offset from address
     * .. byteArray is set; else false.
     * i.e., determines whether element 'offset' is in the set
     *
     * @param   n    The bit to check for inclusion in the set
     * @return       True if it's present, false if not
     */
    private fun getBit(n: Int): Boolean {
        val whichByte = n / 8
        val whichBit = n % 8
        return this.byteArray!![whichByte].toInt() and (1 shl whichBit) != 0
    }

    /**
     * Clears the bit at given offset from address byteArray to 0
     * i.e., removes element 'offset' from the set, if present.
     *
     * @param   n    The bit to be cleared (excluded) from the set
     */
    private fun clearBit(n: Int) {
        val whichByte = n / 8
        val whichBit = n % 8
        this.byteArray!![whichByte] = this.byteArray!![whichByte] and (1 shl whichBit xor 255).toByte()
    }

    /**
     * This method checks first that a Bitset has a byte array, and if so
     * it clears, or zeroes out, the existing array.
     */
    fun clear() {
        if (byteArray == null)
            error("clear: Can't clear a set that hasn't been constructed!")
        for (i in byteArray!!.indices)
            byteArray!![i] = 0
    }

    /**
     * Change existing set to capacity 'size'  (see comments for the constructor
     * for an explanation of the sizing math).
     *
     * @param    size    The requested size for the set
     */
    fun setSize(size: Int) {
        maxSize = size
        val nbyte = (size + 7) / 8
        byteArray = ByteArray(nbyte)    // new array, all zeroes
    }

    /**
     * Checks to see whether the parameter is currently a member of the set
     *
     * @param   i    The value to test for inclusion
     * @return       True if that value is included in the set, false if not
     */
    fun member(i: Int): Boolean {
        return if (i >= maxSize) false else getBit(i)
    }

    /**
     * Checks to see whether the parameter is currently a member of the set
     * This method is included for easier code readability in programs using
     * the Bitset class.
     *
     * @param   i    The value to test for inclusion
     * @return       True if that value is included in the set, false if not
     */
    operator fun contains(i: Int)  // same as member(), reads well:
            : Boolean {                                // e.g., mySet.contains(3);
        return member(i)
    }

    /**
     * This method will ensure that the parameter is within the allowable values
     * for the set, then will add it to the set.  If it was already present, it
     * remains so.
     *
     * @param   i    The element to add to the set
     */
    fun include(i: Int) {
        if (i >= maxSize)
            error("include: $i  is too large to fit inside the set")
        setBit(i)
    }

    /**
     * This method removes the parameter element from the set, after ensuring
     * that it is within the allowable limits of the set elements.  If it was
     * not previously present, the set remains unchanged.
     *
     * @param   i    The value to remove from the set
     */
    fun exclude(i: Int) {
        if (i >= maxSize)
            error("exclude: $i  is too large be inside the set")
        clearBit(i)
    }

    /**
     * Copies setA to receiver, without changing latter's capacity.  In other
     * words, when passed another Bitset, the values that it contains are copied
     * into _this_ Bitset's byte array.  If the parameter set is larger than
     * this set, an error message is printed and the program halts.
     *
     * @param   setA    The set that will be copied to this one.
     * @return          This Bitset containing the new values
     */
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

    /**
     * This method performs union on itself combined with setB, and produces
     * a new Bitset containing the values from both sets.
     *
     * @param   setB    The Bitset to add (union) with _this_ Bitset
     * @return          A new Bitset containing all the elements of the combined sets
     */
    fun union(setB: Bitset): Bitset {
        val temp = Bitset(if (maxSize > setB.maxSize) this else setB)

        val nbyte = min(byteArray!!.size, setB.byteArray!!.size)
        for (i in 0 until nbyte)
            temp.byteArray!![i] = byteArray!![i] or setB.byteArray!![i]
        return temp
    }

    /**
     * This method performs the difference between _this_ Bitset and the
     * parameter Bitset - it determines the elements in this set that are
     * NOT present in setB, and returns them as a new Bitset.
     *
     * @param   setB    The Bitset to "subtract" from _this_ Bitset
     * @return          A new Bitset containing the elements in this Bitset
     * that are NOT in the parameter Bitset
     */
    fun difference(setB: Bitset): Bitset {
        val temp = Bitset(this)
        val nbyte = min(byteArray!!.size, setB.byteArray!!.size)
        for (i in 0 until nbyte)
            temp.byteArray!![i] = byteArray!![i] and (setB.byteArray!![i] xor 255.toByte())
        return temp
    }

    /**
     * This method performs intersection on itself and the parameter Bitset.  A new
     * resulting Bitset is created which has the elements that are present in both
     * Bitsets and is returned.
     *
     * @param   setB   The Bitset to intersect with _this_ Bitset
     * @return         A new Bitset containing only the elements that are common to both sets
     */
    fun intersect(setB: Bitset): Bitset {
        val temp = Bitset(min(maxSize, setB.maxSize))
        val nbyte = min(byteArray!!.size, setB.byteArray!!.size)
        for (i in 0 until nbyte)
            temp.byteArray!![i] = byteArray!![i] and setB.byteArray!![i]
        return temp
    }

    /**
     * This method does content comparison to determine equality of sets.  Sets
     * of different lengths are allowed, and will result in true as long as the
     * larger set has no elements above the maxSize of the smaller set.
     *
     * @param   setB   The Bitset to test (compare) to _this_ Bitset
     * @return         True if the same elements are set in both Bitsets, false if not
     */
    fun equals(setB: Bitset): Boolean {
        /*  Begin by getting the length of the _shorter_ Bitset array...  */
        val nbyte = min(byteArray!!.size, setB.byteArray!!.size)

        /*  Now we test both sets for that length of elements  */
        for (i in 0 until nbyte) {
            if (byteArray!![i] != setB.byteArray!![i]) return false
        }

        /*  If the sets are not of equal length, is _this_ set the larger one?  If so,
	    go through and look for other elements above the maxSize of the other set...  */
        if (byteArray!!.size > nbyte) {
            for (i in nbyte until byteArray!!.size) {
                if (byteArray!![i].toInt() != 0) return false
            }
        }

        /*  Otherwise, is the _other_ set the larger one?  If so, go through and
	    look for other elements greater than the maxSize of _this_ Bitset  */
        if (setB.byteArray!!.size > nbyte) {
            for (i in nbyte until setB.byteArray!!.size) {
                if (setB.byteArray!![i].toInt() != 0) return false
            }
        }
        return true
    }

    /**
     * Utility method to read a set in from standard input via a Scanner.
     * Each element requested is checked to be between 0 and maxSize, and if
     * it meets this requirement, it is included in the current byteArray.
     *
     * @param   in    The Scanner to read from
     */
    fun readSet(`in`: Scanner) {
        clear()
        while (`in`.hasNextInt()) {
            val n = `in`.nextInt()
            if (n >= 0 && n < maxSize) include(n)
        }
        `in`.next()
    }

    /**
     * This creates a String representation of a Bitset object in standard
     * set notation:  { x  x  x  x }  Elements present are listed.
     *
     * @return       A String representing the Bitset in printable format
     */
    override fun toString(): String {
        var str = "{  "
        for (i in 0 until maxSize) {
            if (member(i)) str += "$i  "
        }
        return "$str}"
    }

    fun cardinality(): Int {
        var count = 0
        for (i in 0 until maxSize) {
            if (member(i)) count++
        }
        return count
    }

    fun isSubset(b: Bitset): Boolean {
        for (i in 0 until maxSize) {
            if (member(i) && !b.member(i)) {
                return false
            }
        }
        return true
    }

    /**
     * A method to print a custom error message to the user, then exit
     *
     * @param   msg   The message to print to the user
     */
    private fun error(msg: String) {
        print(" $msg")
        System.exit(1)
    }
}

fun main() {
    val x = Bitset(8)
   val y = Scanner(System.`in`)


    println(x.readSet(y))
}