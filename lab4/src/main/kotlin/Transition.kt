class Transition(
    val from: String,
    val to: String,
    val symbol: String
) {
    override fun toString(): String {
        return "$from -> $to : $symbol"
    }
}
