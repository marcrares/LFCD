class SymbolTable {

    private var size: Int = 70;
    private var storage: MutableList<String?> = MutableList(size) { null }
    private var realSize = 0

    private fun hash(key: String, probe: Int): Int {
        var index = 0
        key.forEach {
            index += it.code
        }
        return (index + probe) % size
    }

    fun put(id: String) {
        resize()
        var probe = 1
        var index = hash(id, probe)
        var item = storage[index]
        while (item != null && item != id) {
            probe += 1
            index = hash(id, probe)
            item = storage[index]
        }
        if (item == null)
            realSize++
        storage[index] = id
    }

    fun remove(id: String): Boolean {
        resize()
        var probe = 1
        var index = hash(id, probe)
        var item = storage[index]
        while (item != null && item != id) {
            probe += 1
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
        val newList = MutableList<String?>(size) { null }
        newList.addAll(storage)
        storage = MutableList(size) {null}
        newList.filterNotNull().forEach {
            val id = it
            var probe = 1
            var index = hash(id, probe)
            var item = storage[index]
            while (item != null && item != id) {
                probe += 1
                index = hash(id, probe)
                item = storage[index]
            }
            storage[index] = id
        }
    }

    fun getPosition(id: String): Int? {
        var probe = 1
        var index = hash(id, probe)
        var item = storage[index]
        while (item != null && item != id) {
            probe += 1
            index = hash(id, probe)
            item = storage[index]
        }
        return item?.let {
            index
        }
    }

    fun elements(): List<String> {
        return storage.filterNotNull()
    }

    override fun toString(): String {
        var outString = "The SymbolTable uses a HashMap with linear probing as conflict resolution method\n"
        storage.forEachIndexed { index, value ->
            if (value != null)
                outString += "$index - $value\n"
        }
        return outString
    }

}