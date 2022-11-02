class PIF {

    private val tokensRead : MutableList<Pair<String, Int>> = mutableListOf()

    fun addToken(token: String, index: Int) {
        tokensRead.add(Pair(token, index))
    }

    fun getPIF() = tokensRead

    override fun toString(): String {
        var outString = "";
        tokensRead.forEach {
            outString += it.first + "," + it.second + "\n"
        }
        return outString
    }

}