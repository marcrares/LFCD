import java.io.File
import java.util.Scanner

class LexicalScanner {

    private val program1Path = "src/main/resources/program1.txt"
    private val program2Path = "src/main/resources/program2.txt"
    private val program3Path = "src/main/resources/program3.txt"
    private val programErrorPath = "src/main/resources/programerror.txt"
    private val pifOutPath = "src/main/resources/pif.txt"
    private val sTableOutPath = "src/main/resources/table.txt"
    private val ls = LanguageSpec()
    private val symbolTable = SymbolTable()
    private val constTable = SymbolTable()
    private val pif = PIF()

    fun scan() {
        val tokenPairs = mutableListOf<Pair<String, Int>>()

        val file = File(program1Path)
        val reader = Scanner(file)
        var lineNr = 1;
        var tokens: List<String>

        while (reader.hasNextLine()) {
            val line = reader.nextLine()
            tokens = tokenize(line)
            tokens.forEach {
                tokenPairs.add(Pair(it, lineNr))
            }
            lineNr ++
        }

        reader.close()

        if (buildPIF(tokenPairs)) {
            println("Program is lexically correct")
            File(pifOutPath).writeText(pif.toString())
            File(sTableOutPath).writeText(symbolTable.toString())
        }
        else {
            File(pifOutPath).writeText("")
            File(sTableOutPath).writeText("")
        }

    }

    private fun tokenize(line: String) : List<String> {
        val tokens = mutableListOf<String>()
        var lastSeparator = -1
        var inString = false

        for (index in line.indices) {
            if (ls.getType(line[index].toString()) == Type.SEPARATOR && !inString) {
                if (index - lastSeparator > 1) {
                    val token = line.subSequence(lastSeparator + 1, index).toString()
                    tokens.add(token)
                }
                lastSeparator = index
                if (line[index] != ' ')
                    tokens.add(line[index].toString())
            }
            else if (index == line.length - 1) {
                val token = line.subSequence(lastSeparator + 1, index + 1).toString()
                tokens.add(token)
            }
            if (line[index] == '"')
                inString = !inString
        }

        return tokens;
    }

    private fun buildPIF(tokenPairs: MutableList<Pair<String, Int>>) : Boolean {
        var lexicallyCorrect = true
        tokenPairs.forEach {
            when (ls.getType(it.first)) {
                Type.IDENTIFIER -> {
                    symbolTable.put(it.first)
                    symbolTable.getPosition(it.first)?.let { pos ->
                        pif.addToken("identifier", pos)
                    }
                }
                Type.CONSTANT -> {
                    constTable.put(it.first)
                    constTable.getPosition(it.first)?.let { pos ->
                        pif.addToken("constant", pos)
                    }
                }
                Type.KEYWORD -> {
                    pif.addToken(it.first, -1)
                }
                Type.OPERATOR -> {
                    pif.addToken(it.first, -1)
                }
                Type.SEPARATOR -> {
                    pif.addToken(it.first, -1)
                }
                else -> {
                    lexicallyCorrect = false
                    println("Error on line " + it.second + ": invalid token " + it.first)
                }
            }
        }
        return lexicallyCorrect
    }

}


fun main() {
    val scanner = LexicalScanner()
    scanner.scan()
}