package classe.task1

import org.junit.Test

import org.junit.Assert.*
import org.junit.jupiter.api.assertThrows


class BitsetTest {
    private val setA = Bitset(24)
    private val setB = Bitset(16)
    private val setC = Bitset(8)

    @Test
    fun add() {
        for (i in 1..9) {
            if (i % 2 == 0)
                setA.add(i)
            if (i % 3 == 0)
                setB.add(i)
        }
        assertEquals("{ 2 4 6 8 }", setA.toString())
        assertEquals("{ 3 6 9 }", setB.toString())
    }

    @Test
    fun addMassive() {
        setB.addMassive(setOf(1, 10, 11, 12, 15))
        setA.addMassive(setOf(17, 19, 21, 23))

        assertEquals("{ 1 10 11 12 15 }", setB.toString())
        assertEquals("{ 17 19 21 23 }", setA.toString())
        assertThrows<Exception> { setC.addMassive(setOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)) } // setCSize = 8
    }

    @Test
    fun remove() {
        for (i in 0..7)
            setA.add(i)                                            // setA = { 0 1 2 3 4 5 6 7 }
        setA.remove(3)                                            // setA = { 0 1 2 4 5 6 7} we removed 3
        assertEquals("{ 0 1 2 4 5 6 7 }", setA.toString())
        setA.remove(1); setA.remove(7); setA.remove(5)    // we remove 1 5 and 7
        assertEquals("{ 0 2 4 6 }", setA.toString())
    }

    @Test
    fun removeMassive() {
        setA.addMassive(setOf(1, 2, 3, 4, 5))
        assertThrows<Exception> { setA.removeMassive(setOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)) }
        setA.removeMassive(setOf(2, 3, 4))
        assertEquals("{ 1 5 }", setA.toString())
    }

    @Test
    fun clear() {
        setA.addMassive(setOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
        setA.clear()
        assertEquals("{ }", setA.toString())
    }

    @Test
    fun contains() {
        for (i in 10..20)
            setA.add(i)
        assertTrue(setA.contains(19))
        assertTrue(setA.contains(13))
        assertFalse(setA.contains(9))
        setA.removeMassive(setOf(10, 11, 12, 13, 14, 15))
        assertFalse(setA.contains(13))
        assertFalse(setA.contains(10))
    }

    @Test
    fun intersect() {
        setA.addMassive(setOf(1, 2, 3, 4, 5, 6, 7))
        setB.addMassive(setOf(5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15))
        setC.addMassive(setOf(5, 6, 7))
        assertTrue(setC.equals(setA.intersect(setB)))
        setB.remove(6)
        setC.remove(6)
        assertTrue(setC.equals(setB.intersect(setA)))
    }

    @Test
    fun union() {
        setA.addMassive(setOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15))
        setB.addMassive(setOf(5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15))
        setC.addMassive(setOf(1, 2, 3, 4, 5, 6, 7))
        assertTrue(setA.equals(setB.union(setC)))
    }

    @Test
    fun difference() {
        setA.addMassive(setOf(8, 9, 10, 11, 12, 13, 14, 15))
        setB.addMassive(setOf(5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15))
        setC.addMassive(setOf(1, 2, 3, 4, 5, 6, 7))
        assertEquals(setA.toString(), (setB.difference(setC)).toString())
    }

    @Test
    fun equals() {
        setA.addMassive(setOf(1, 2, 3, 4, 5, 6, 7))
        setB.addMassive(setOf(1, 2, 3, 4, 5, 6, 7))
        assertTrue(setA.equals(setB))
        var other = setB
        assertTrue(setA.equals(other))
        other = Bitset(setA)
        assertTrue(other.equals(setB))
    }


    @Test
    fun cardinality() {
        for (i in 0..10)
            setA.add(i)
        assertEquals(11, setA.cardinality())
        setA.remove(0)
        assertEquals(10, setA.cardinality())
    }
}