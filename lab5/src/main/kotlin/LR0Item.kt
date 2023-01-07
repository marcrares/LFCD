class LR0Item(production: Production) : Cloneable {

    val start: String
    val right: List<String>
    var dot: Int
    val id: Int

    init {
        start = production.start
        right = production.right
        dot = 0
        id = production.id
    }

    constructor(item: LR0Item) : this(Production(item.start, item.right, item.id)){
        dot = item.dot
    }

    fun goTo(): Boolean {
        if (dot >= right.size) return false
        dot++
        return true
    }

    override fun equals(other: Any?): Boolean {
        if (other !is LR0Item)
            return false
        return start == other.start && right == other.right && dot == other.dot
    }

    fun currentSymbol(): String {
        return if (dot >= right.size) ""
        else right[dot]
    }

    override fun toString(): String {
        var out = "$start ->"
        right.forEachIndexed { index, element ->
            out += if (index == dot) " .$element"
            else " $element"
        }
        if (dot == right.size)
            out += "."
        return out
    }

    public override fun clone(): LR0Item = LR0Item(this)

    override fun hashCode(): Int {
        var result = start.hashCode()
        result = 31 * result + right.hashCode()
        result = 31 * result + dot
        return result
    }

}