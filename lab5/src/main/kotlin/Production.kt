class Production(
    val start: String,
    val right: List<String>,
    val id: Int
) {

    override fun toString(): String {
        var out = "$start ->"
        right.forEach {
            out += " $it"
        }
        return out
    }

}