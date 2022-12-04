import java.io.File
import java.util.*

class FiniteAutomaton {

    private val transitions: MutableList<Transition> = mutableListOf()
    private val states: MutableList<String> = mutableListOf()
    private val alphabet: MutableList<String> = mutableListOf()
    private val outputStates: MutableList<String> = mutableListOf()
    private lateinit var initialState: String

    init {
        readAutomaton()
    }

    private fun readAutomaton() {
        val filePath = "src/main/resources/fa.in"
        val file = File(filePath)
        val reader = Scanner(file)
        val statesInput = reader.nextLine().split(",")
        statesInput.forEach { states.add(it) }
        initialState = reader.nextLine()
        val finalStatesInput = reader.nextLine().split(",")
        finalStatesInput.forEach { outputStates.add(it) }
        val alphabetInput = reader.nextLine().split(",")
        alphabetInput.forEach { alphabet.add(it) }
        while (reader.hasNextLine()) {
            val transitionInput = reader.nextLine().split(",")
            transitions.add(Transition(transitionInput[0], transitionInput[1], transitionInput[2]))
        }
    }

    fun checkAccepted(sequence: CharSequence) : Boolean {
        val list = sequence.map { it.toString() }
        var currState = initialState
        var found = false
        for (letter in list) {
            for (transition in transitions) {
                found = transition.from == currState && letter == transition.symbol
                if (found) {
                    currState = transition.to
                    break
                }
            }
            if (!found) return false
        }
        return found && currState in outputStates
    }

    fun printStates() { println("States: $states") }

    fun printAlphabet() { println("Alphabet: $alphabet") }

    fun printInitialState() { println("InitialState: $initialState") }

    fun printOutputStates() { println("OutputStates: $outputStates") }

    fun printTransitions() {
        println("Transitions: ")
        transitions.forEach(System.out::println)
    }

}