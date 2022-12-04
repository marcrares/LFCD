import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

class Grammar {
    private val nonTerminals: MutableList<String> = mutableListOf()
    private val terminals: MutableSet<String> = mutableSetOf()
    private val productions: MutableList<Production> = mutableListOf()
    private var startingSymbol: String? = null

    init {
        grammarFromFile()
    }

    private fun grammarFromFile() {
        for ((i, line) in Files.readAllLines(Paths.get("grammar.txt")).withIndex()) {
            if (i <= 2) {
                val tokens = line.split(" ".toRegex())
                for (j in tokens.indices) {
                    if (i == 0) {
                        nonTerminals.add(tokens[j])
                    }
                    if (i == 1) {
                        terminals.add(tokens[j])
                    }
                    if (i == 2) {
                        startingSymbol = tokens[j]
                    }
                }
            }
        }
    }

    fun getProductionsForNonterminal(nonterminal: String?): List<Production> {
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

    fun getTerminals(): Set<String> {
        return terminals
    }

    fun getProductions(): List<Production> {
        return productions
    }

    override fun toString(): String {
        return "G =( " + nonTerminals.toString() + ", " + terminals.toString() + ", " +
                productions.toString() + ", " + startingSymbol + " )"
    }
}