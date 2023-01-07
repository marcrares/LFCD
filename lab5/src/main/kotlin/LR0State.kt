class LR0State(val grammar: Grammar, core: Set<LR0Item>, val id: Int) : Cloneable {

    val items: MutableSet<LR0Item> = mutableSetOf()
    val symbol: String
    val nextStates = mutableListOf<LR0State>()

    init {
        items.addAll(core)
        symbol = if (id > 0) {
            items.first().let {
                it.right[it.dot - 1]
            }
        } else ""
    }

    override fun equals(other: Any?): Boolean {
        if (other !is LR0State)
            return false
        return this.items == other.items
    }

    public override fun clone(): LR0State {
        val itemsCopy = mutableSetOf<LR0Item>()
        items.forEach { item ->
            itemsCopy.add(item.clone())
        }
        return LR0State(grammar, itemsCopy, id)
    }

    fun closure() {
        var done = false
        val temp = mutableSetOf<LR0Item>()
        while (!done) {
            done = true
            items.forEach {
                if (it.currentSymbol().isNotEmpty() && grammar.isNonTerminal(it.currentSymbol())) {
                    val productions = grammar.getProductionsForNonterminal(it.currentSymbol())
                    temp.addAll(productions.map { prod -> LR0Item(prod) })
                }
            }
            if (!items.containsAll(temp)) {
                items.addAll(temp)
                done = false
            }
            temp.clear()
        }
    }

    override fun hashCode(): Int {
        var result = grammar.hashCode()
        result = 31 * result + items.hashCode()
        return result
    }

    override fun toString(): String {
        return "$id $items $symbol"
    }

}