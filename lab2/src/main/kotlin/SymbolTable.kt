//1b

class SymbolTable<T> {

    private var size: Int = 1;
    private var storage: MutableList<Pair<String, T>?> = MutableList(size) { null }
    private var realSize = 0

    private fun hash(key: String, probe: Int): Int {
        var index = 0
        key.forEach {
            index += it.code * probe
        }
        return index % size
    }

    fun put(id: String, elem: T) {
        resize()
        var probe = 1
        var index = hash(id, probe)
        var item = storage[index]
        while (item != null && item.first != id) {
            probe *= 2
            index = hash(id, probe)
            item = storage[index]
        }
        if (item == null)
            realSize++
        storage[index] = Pair(id, elem)
    }

    fun remove(id: String): Boolean {
        resize()
        var probe = 1
        var index = hash(id, probe)
        var item = storage[index]
        while (item != null && item.first != id) {
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

    private fun resize() {
        val alpha = realSize.toDouble() / size
        if (alpha < 0.7)
            return
        size *= 2
        val newList = MutableList<Pair<String, T>?>(size) { null }
        newList.addAll(storage)
        storage = MutableList(size) {null}
        newList.filterNotNull().forEach {
            val id = it.first
            var probe = 1
            var index = hash(id, probe)
            var item = storage[index]
            while (item != null && item.first != id) {
                probe *= 2
                index = hash(id, probe)
                item = storage[index]
            }
            storage[index] = Pair(id, it.second)
        }
    }

    fun get(id: String): T? {
        var probe = 1
        var index = hash(id, probe)
        var item = storage[index]
        while (item != null && item.first != id) {
            probe *= 2
            index = hash(id, probe)
            item = storage[index]
        }
        return item?.second
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
    table.put("a", 40)
    table.put("b", 50)
    table.put("f", 90)
    table.remove("f")

    println(table.elements())
}