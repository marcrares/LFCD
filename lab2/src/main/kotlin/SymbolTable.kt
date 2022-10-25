//1b

class SymbolTable<T> {

    private val size: Int = 200;
    private var storage: MutableList<Pair<String, T>?> = MutableList(size) { null }
    private var realSize = 0

    private fun hash(key: String, probe: Int): Int {
        var index = 0
        key.forEach {
            index += it.code * probe
        }
        return index % 200
    }

    fun put(id: String, elem: T) {
        var probe = 1
        var index = hash(id, probe)
        var item = storage[index]
        while (item != null && item.first != id) {
            probe *= 2
            index = hash(id, probe)
            item = storage[index]
        }
        if (item == null)
            realSize ++
        storage[index] = Pair(id, elem)
    }

    fun remove(id: String) : Boolean {
        var probe = 1
        var index = hash(id, probe)
        var item = storage[index]
        while (item != null && item.first != id){
            probe *= 2
            index = hash(id, probe)
            item = storage[index]
        }
        return item?.let {
            storage.removeAt(index)
            realSize--
            return@let true
        } ?: false

    }

    fun resize() {
    }

    fun isPresent(id: String): Boolean {
        var probe = 1
        var index = hash(id, probe)
        var item = storage[index]
        while (item != null && item.first != id){
            probe *= 2
            index = hash(id, probe)
            item = storage[index]
        }
        return item?.let {
            return@let true
        } ?: false
    }

    fun elements(): List<Pair<String, T>> {
        return storage.filterNotNull()
    }

}

fun main() {
    val table = SymbolTable<Int>()
    table.put("ana", 10)
    table.put("ana", 20)
    table.put("ionut", 30)
    table.remove("ana")
    println(table.elements())
}