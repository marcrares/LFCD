import java.util.Stack
import java.util.stream.IntStream

class LR0Parser(val grammar: Grammar) {

    val canonicalCollection: MutableList<LR0State> = mutableListOf()
    val goToTable: MutableMap<Int, MutableMap<String, Int>> = mutableMapOf()
    val actionTable: MutableMap<Int, MutableMap<String, Action>> = mutableMapOf()
    var productionsResult = mutableListOf<Production>()
    var nrStates = 0

    fun parse(input: String) {
        var accepted = false
        val stack = Stack<String>()
        stack.push("0")
        var index = 0
        while (!accepted) {
            val currentSymbol = input[index].toString()
            val stackPeek = stack.peek().toInt()
            val action = actionTable[stackPeek]?.get(currentSymbol)
            when (action) {
                is Action.Shift -> {
                    stack.push(currentSymbol)
                    stack.push(action.stateID.toString())
                    index++
                }

                is Action.Reduce -> {
                    val production = grammar.getProductions().find { it.id == action.prodID }!!
                    val toPop = production.right.size * 2
                    val toPush = production.start
                    IntStream.range(0, toPop).forEach { stack.pop() }
                    productionsResult.add(0, production)
                    val toPush2 = goToTable[stack.peek().toInt()]!![toPush].toString()
                    stack.push(toPush)
                    stack.push(toPush2)
                }

                else -> accepted = true
            }
        }
    }

    fun createTables() {
        canonicalCollection.forEach { state ->
            goToTable[state.id] = mutableMapOf()
            actionTable[state.id] = mutableMapOf()
        }
        canonicalCollection.forEach { state ->
            state.nextStates.forEach { nextState ->
                if (grammar.isNonTerminal(nextState.symbol))
                    goToTable[state.id]?.put(nextState.symbol, nextState.id)
                else {
                    actionTable[state.id]?.put(nextState.symbol, Action.Shift(nextState.id))
                }
            }
        }
        canonicalCollection.filter { actionTable[it.id]!!.isEmpty() }.forEach { state ->
            val prodID = state.items.first().id
            if (prodID == 0)
                actionTable[state.id]?.put("$", Action.Accept)
            else
                grammar.getTerminals().forEach { symbol ->
                    actionTable[state.id]!![symbol]?.let {
                        println("Conflict in state ${state.id} for symbol $symbol")
                    } ?: actionTable[state.id]?.put(symbol, Action.Reduce(prodID))
                }
        }
    }

    fun createCanonicalCollection() {
        val start = setOf(LR0Item(grammar.getProductions().first()))
        val startState = LR0State(grammar, start, nrStates)
        val temp: MutableList<LR0State> = mutableListOf()
        temp.add(startState)
        while (temp.size > 0) {
            val currentState = temp.first()
            temp.remove(currentState)
            val initialItem = currentState.items.first().clone()
            currentState.closure()
            if (currentState in canonicalCollection) continue
            val copy = currentState.clone()
            currentState.items.forEach { item ->
                if (item.goTo()) {
                    nrStates++
                    val newState = LR0State(grammar, setOf(item), nrStates)
                    if (newState !in temp)
                        temp.add(newState)
                    else nrStates--
                    if (item == initialItem)
                        copy.nextStates.add(currentState)
                    else
                        copy.nextStates.add(temp.first { it == newState })
                }
            }
            canonicalCollection.add(copy)
        }
    }
}


sealed class Action {
    data class Shift(val stateID: Int) : Action() {
        override fun toString(): String {
            return "S$stateID"
        }
    }

    object Accept : Action() {
        override fun toString(): String {
            return "accept"
        }
    }

    data class Reduce(val prodID: Int) : Action() {
        override fun toString(): String {
            return "R$prodID"
        }
    }
}