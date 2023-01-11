import Utils.tryParse
import java.io.File
import java.util.Scanner

class Grammar {
    private val nonTerminals: MutableList<String> = mutableListOf()
    private val terminals: MutableSet<String> = mutableSetOf()
    private val productions: MutableList<Production> = mutableListOf()
    private var startingSymbol: String? = null

    init {
        grammarFromFile()
        val augmentedStart = "$startingSymbol'"
        nonTerminals.add(augmentedStart)
        productions.add(0, Production(augmentedStart, listOf(startingSymbol!!), 0))
    }

    private fun grammarFromFile() {
        val filePath = "src/main/resources/g1.txt"
        val file = File(filePath)
        val reader = Scanner(file)
        val nonTerminalsInput = reader.nextLine().split(" ")
        nonTerminalsInput.forEach { nonTerminals.add(it) }
        val terminalsInput = reader.nextLine().split(" ")
        terminalsInput.forEach { terminals.add(it) }
        terminals.add("$")
        startingSymbol = reader.nextLine()
        var index = 1
        while (reader.hasNextLine()) {
            val sides = reader.nextLine().split(" -> ")
            val left = sides[0]
            val right = sides[1].split(" ")
            productions.add(Production(left, right, index))
            index ++
        }
    }

    fun getProductionsForNonterminal(nonterminal: String): List<Production> {
        val productionsForNonterminal = mutableListOf<Production>()
        for (production in productions) {
            if (production.start == nonterminal) {
                productionsForNonterminal.add(production)
            }
        }
        return productionsForNonterminal
    }

    fun getNonTerminals(): List<String> {
        return nonTerminals
    }

    fun isNonTerminal(symbol : String) : Boolean {
        return nonTerminals.contains(symbol)
    }

    fun getTerminals(): Set<String> {
        return terminals
    }

    fun getProductions(): List<Production> {
        return productions
    }

    fun checkCFG(): Boolean {
        return productions.stream().allMatch {
            it.start.length == 1 && nonTerminals.contains(it.start)
        }
    }

    override fun toString(): String {
        return "G =( " + nonTerminals.toString() + ", " + terminals.toString() + ", " +
                productions.toString() + ", " + startingSymbol + " )"
    }
}

fun main() {
    val g = Grammar()
    val parser = LR0Parser(g)
    parser.createCanonicalCollection()
    parser.createTables()
    parser.parse("abbc$")
    parser.productionsResult.forEach { println(it) }
}