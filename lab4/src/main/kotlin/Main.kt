import java.util.*
import kotlin.system.exitProcess

fun main() {
    val fa = FiniteAutomaton()
    println("0. Exit")
    println("1. Print states")
    println("2. Print alphabet")
    println("3. Print output states")
    println("4. Print in state")
    println("5. Print transitions")
    println("6. Check word")
    val scanner = Scanner(System.`in`)
    while (true) {
        print(">> ")
        when (scanner.nextLine()) {
            "1" -> fa.printStates()
            "2" -> fa.printAlphabet()
            "3" -> fa.printOutputStates()
            "4" -> fa.printInitialState()
            "5" -> fa.printTransitions()
            "6" -> {
                print("Word: ")
                val word = scanner.nextLine()
                println(fa.checkAccepted(word))
            }

            "0" -> exitProcess(0)
        }
    }
}