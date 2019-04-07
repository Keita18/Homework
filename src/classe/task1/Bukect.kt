package classe.task1

import kotlin.math.abs


interface CollisionManagement {
    fun nextIndex(h: Int, i: Int): Int
}

class LinearProbing : CollisionManagement {
    override fun nextIndex(h: Int, i: Int): Int {
        return h + i
    }
}


internal class HashTableException : Exception {
    constructor() : super() {}
    constructor(s: String) : super(s) {}
}

interface SymbolTable {
    @Throws(HashTableException::class)
    fun put(key: Int, value: Any)

    @Throws(HashTableException::class)
    operator fun get(key: Int): Any?

    @Throws(HashTableException::class)
    fun remove(key: Int)

    operator fun contains(key: Int): Boolean
    fun size(): Int
}

class Entry(private var key: Int, private var value: Any){

    // Accesseurs
    fun getKey(): Int? = key
    fun getValue(): Any? { return value; }

    // Mutateurs
    fun setValue(value: Any?)  { this.value= value!! }
}

class Bucket() {
    private var slot: Entry? = null
    private var free = true

    constructor(e: Entry) : this() {
        this.slot = e
        this.free = false
    }

    fun clean() {
        slot?.setValue(null)
        free = true
    }

    fun getKey(): Int? {
        return slot!!.getKey()
    }

    fun getValue(): Any? {
        return slot?.getValue()
    }

    fun isFree(): Boolean {
        return free
    }

    fun setValue(value: Any) {
        slot?.setValue(value)
        free = false
    }

}

class HashTable : SymbolTable {
    private var bucketArray: Array<Bucket?>? = null        // Tableau contenant les paires "clé-valeur"

    private var nbrObject = 0           // Nombre d'éléments insérés

    private var manager: CollisionManagement? = null // Gestionnaire de collision


    constructor(size: Int): this(size, LinearProbing())


    constructor(size: Int, manager: CollisionManagement) {
        this.manager = manager
        bucketArray = Array(size) { Bucket() }
    }

    @Throws(HashTableException::class)
    override fun put(key: Int, value: Any) {
        if(bucketArray?.size == nbrObject) // D'abord vérifier s'il reste de la place
            throw HashTableException("Table pleine.")
        val index = getBucketIndex(key) // L'emplacement de la paire "clé-valeur"

        var inc = true
        if (bucketArray!![index] == null)
        // Si l'emplacement est vide...
            bucketArray!![index] = Bucket(Entry(key, value)) // ... alors on y place la paire !
        else // Si l'emplacement est déjà occupé ou libre...
        {
            inc = bucketArray!![index]!!.isFree(); // On incrémente que dans le cas où la case est libre
            bucketArray!![index]?.setValue(value); // On remplace la valeur (choix d'implémentation)
        }
        if(inc)
            this.nbrObject++
    }

    @Throws(HashTableException::class)
    override fun get(key: Int): Any? {
        if (nbrObject == 0)
        // Si la table est vide, cela ne sert à rien de continuer.
            throw HashTableException("Table vide.")

        val bucket = bucketArray?.get(getBucketIndex(key)) // Case dans laquelle devrait se trouver la clé
        if(bucket != null && !bucket.isFree()) // Si la case est occupée, on a trouvé notre clé !
            return bucket.getValue()
        else
            throw  HashTableException("Clé non trouvée");
    }

    @Throws(HashTableException::class)
    override fun remove(key: Int) {
        if (nbrObject == 0)
        // Si la table est vide, cela ne sert à rien de continuer
            throw HashTableException("Table vide.")

        val delete = bucketArray?.get(getBucketIndex(key)) // La case à nettoyer

        if(delete == null || delete.isFree()) // Si la case est vide ou déjà nettoyée, on peut s'arrêter
            throw HashTableException("Clée non trouvée");

        delete.clean();
        nbrObject--;
    }

    override fun contains(key: Int): Boolean {
        val bucket = bucketArray?.get(getBucketIndex(key));
        return (bucket != null && !bucket.isFree())
    }

    override fun size(): Int {

            return nbrObject
    }

    private fun hash(s: String): Int {

        val char = s.toCharArray()
        // Etape 1 - hachage
        var h = 0

        // Etape 1 - Hachage
        for (i in 0 until s.length)
            h += char[i].toInt()
        return abs(h) % (bucketArray?.size ?: 1)
    }

    private fun getBucketIndex(key: Int): Int {
        val h = hash((key).toString()) // Calcul du hach code
        var index = h // index sera l'indice de la clé
        var i = bucketArray!![0]


        return index
    }
}


fun main(args: Array<String>) {
     fun isValidChar(c: Char): Boolean {
        return Character.isJavaIdentifierPart(c)
    }
    println(isValidChar('-'))
}
