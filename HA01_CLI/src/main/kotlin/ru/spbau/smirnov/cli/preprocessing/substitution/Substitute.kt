package ru.spbau.smirnov.cli.preprocessing.substitution

import ru.spbau.smirnov.cli.Environment

class Substitute(private val environment: Environment) {
    fun doSubstitution(input: String): String {
        var stateMachine: StateMachineVertex = UsualState()
        val tokens = mutableListOf<Token>()

        for (symbol in input) {
            stateMachine = stateMachine.parseSymbol(symbol, tokens)
        }
        stateMachine.finishParsing(tokens)

        return tokens.joinToString(separator = "", transform = { it.toValue(environment) })
    }
}
